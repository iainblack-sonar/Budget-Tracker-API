Scaffold a new REST endpoint for the Budgetly API. Follow these steps in order, respecting the module architecture (api → core → persistence):

**Input required:** The user should provide the resource name and HTTP method(s).

## Steps

1. **Create DTO records in `budgetly-api`** (`src/main/java/com/budgetly/api/dto/`)
   - Create request record(s) with Jakarta Bean Validation annotations (`@NotBlank`, `@NotNull`, `@Email`, `@Size`, etc.)
   - Create response record with a `static from(Entity)` factory method
   - Use Java `record` types

2. **Create or update the repository in `budgetly-persistence`** (`src/main/java/com/budgetly/persistence/repository/`)
   - Add any new query methods needed (e.g., `findByXAndUserId`)
   - Follow Spring Data JPA naming conventions

3. **Create or update the service in `budgetly-core`** (`src/main/java/com/budgetly/core/`)
   - All methods must accept `userId` as a parameter and enforce ownership
   - Add business validation logic here, not in the controller
   - Use `@Transactional` annotations appropriately
   - Throw `BudgetlyException` subtypes for error conditions

4. **Create the controller method in `budgetly-api`** (`src/main/java/com/budgetly/api/controller/`)
   - Use `@AuthenticationPrincipal AuthenticatedUser user` to get the current user
   - Apply `@Valid` on request bodies
   - Map to appropriate HTTP methods and status codes
   - Convert entities to DTOs before returning

5. **Update `GlobalExceptionHandler`** if new exception types are needed
   - All custom exceptions must extend `BudgetlyException` in `budgetly-common`

6. **Write a unit test for the service logic** in the appropriate test directory
   - Mock the repository
   - Test happy path and error cases
   - Test ownership validation

## Architecture reminders
- Controllers NEVER access repositories directly — always go through core services
- Business logic NEVER goes in controllers
- Constructor injection only
