---
paths:
  - "budgetly-common/**"
---

# Common Module Rules

## Exceptions
- All custom exceptions must extend `BudgetlyException`
- Constructor takes only `message` — pass the HTTP status code to `super` internally
- Never expose `statusCode` as a constructor parameter to callers

```java
public class NotFoundException extends BudgetlyException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
```

- Existing exceptions and their status codes:
  - `ValidationException` → 400
  - `UnauthorizedException` → 401
  - `ResourceNotFoundException` → 404
- Add a new exception only if none of the above fit

## Constants
- Utility class pattern: `private` no-arg constructor, all fields `public static final`
- No Spring annotations (`@Component`, `@Bean`, etc.) — this module has no Spring dependency
- Group related constants; add a comment block if the class grows beyond ~20 fields

## This module does NOT own
- Anything Spring-specific
- DTOs, entities, or service logic
- This is a zero-dependency leaf module — it must not import from any other `budgetly-*` module
