# SonarQube CAG MCP Tools — Usage Directive

## Before editing any Java source code, you MUST:

1. Call `get_guidelines` with `mode: "project_based"` for project-specific coding standards
2. Locate existing code with `search_by_signature_patterns` or `search_by_body_patterns`
3. Read the relevant implementation with `get_source_code`

## When changing architecture or module dependencies, you MUST additionally:

- Call `get_intended_architecture` to check allowed dependencies
- Call `get_current_architecture` to understand the current module structure
- Trace call impact using `get_upstream_call_flow` and `get_downstream_call_flow`

## After generating or modifying code, you SHOULD:

- Run `run_advanced_code_analysis` on changed files to catch quality/security issues early

## Guidelines mode selection

| Situation | Mode |
|-----------|------|
| Default / bug fix / small change | `project_based` |
| New feature in a specific area | `combined` with relevant `categories` |
| Specific rule categories known | `category_based` |

## Relevant categories for this project (Java/Spring Boot)

- `Complexity & Maintainability`
- `Naming & Code Style`
- `Exception & Error Handling`
- `Type Systems & Logic Safety`
- `Auth & Identity` (for `budgetly-auth` changes)
- `Data Modeling & Persistence` (for `budgetly-persistence` changes)
- `Web Service & API Design` (for `budgetly-api` changes)
