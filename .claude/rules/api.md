---
paths:
  - "budgetly-api/**"
---

# API Module Rules

## Controllers
- No business logic in controllers — delegate everything to a service in `budgetly-core`
- Always return `ResponseEntity<T>` for type-safe responses
- Use `@Valid` on every `@RequestBody` parameter
- Inject the authenticated user via `@AuthenticationPrincipal AuthenticatedUser user` — never read from SecurityContext manually
- Always pass `user.userId()` to service methods to enforce user isolation

```java
@PostMapping
public ResponseEntity<AccountResponse> create(
    @AuthenticationPrincipal AuthenticatedUser user,
    @RequestBody @Valid CreateAccountRequest request) {
  return ResponseEntity.ok(AccountResponse.from(accountService.createAccount(user.userId(), ...)));
}
```

## DTOs
- All DTOs are Java `record` types — never classes
- Request records use Bean Validation annotations (`@NotBlank`, `@Email`, `@Size`, `@NotNull`)
- Response records provide a static factory: `public static FooResponse from(FooEntity entity)`
- DTOs live in `com.budgetly.api.dto` — never in core or persistence

## Exception handling
- Do NOT add try-catch in controllers
- `GlobalExceptionHandler` catches all `BudgetlyException` subclasses automatically
- To add a new error response shape, modify `GlobalExceptionHandler` only

## HTTP conventions
- DELETE returns `ResponseEntity.noContent().build()` (204)
- POST of a new resource returns `ResponseEntity.ok(...)` (200) or `ResponseEntity.status(201).body(...)`
- Use `@PathVariable` for resource IDs, `@RequestParam` for optional filters
