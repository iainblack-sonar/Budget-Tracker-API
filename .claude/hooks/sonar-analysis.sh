#!/bin/bash
# PostToolUse hook: runs SonarQube analysis after writing/editing Java files.
# Uses the sonar CLI to verify the file and injects results as additionalContext.

input=$(cat)
f=$(echo "$input" | jq -r '.tool_input.file_path // empty' 2>/dev/null)

# Only act on .java files inside this project
if [ -z "$f" ] || ! echo "$f" | grep -qE 'Budget-Tracker-API/.+\.java$'; then
  exit 0
fi

# Run sonar verify and capture output
output=$(/Users/Iain.black/.local/share/sonarqube-cli/bin/sonar verify \
  --file "$f" \
  --branch main \
  --project iainblack-sonar_Budget-Tracker-API 2>&1)
exit_code=$?

if [ $exit_code -ne 0 ] || echo "$output" | grep -qiE 'issue|warning|error|violation'; then
  # Escape output for JSON
  escaped=$(echo "$output" | jq -Rs .)
  cat <<EOF
{"hookSpecificOutput":{"hookEventName":"PostToolUse","additionalContext":"SonarQube analysis completed for $f. Results:\n$output\nAddress any issues before proceeding."}}
EOF
fi
