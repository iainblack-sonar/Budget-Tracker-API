Run the full Budgetly verification suite. Execute these steps in order and report any failures:

## Steps

1. **Fix formatting**
   ```bash
   ./mvnw spotless:apply
   ```

2. **Run all tests**
   ```bash
   ./mvnw test
   ```

3. **Full build lifecycle** (compile, test, package, verify)
   ```bash
   ./mvnw verify
   ```

4. **Report results**
   - If all steps pass: report success with test count
   - If any step fails: report which step failed and the error output
   - If Spotless made changes: list the files that were reformatted
