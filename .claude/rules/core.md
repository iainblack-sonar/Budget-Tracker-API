---
paths:
  - "budgetly-core/**"
---

# Core Module Rules

## Transactions
- `@Transactional(readOnly = true)` on all read methods
- `@Transactional` on all write methods (create, update, delete)
- Never put `@Transactional` on controllers — it belongs here only

## Entity construction
- Use the Lombok builder — never call setters after construction for new entities
- Do NOT call `repository.save()` redundantly inside a `@Transactional` method — JPA manages dirty checking

```java
@Transactional
public AccountEntity createAccount(...) {
    AccountEntity account = AccountEntity.builder()
        .userId(userId)
        .name(name)
        .type(type)
        .balance(balance)
        .build();
    return accountRepository.save(account);
}
```

## Error handling
- Throw custom exceptions from `budgetly-common` — never return `null` or `Optional` from service methods
- `ResourceNotFoundException` for missing entities
- `ValidationException` for invalid business state
- `UnauthorizedException` when user doesn't own the resource

## User isolation
- Every service method that accesses user data must accept `userId` as a parameter
- Use `findByIdAndUserId(id, userId)` repository methods — never `findById` alone
- Throw `ResourceNotFoundException` (not `UnauthorizedException`) when the record doesn't exist for that user — don't leak existence

## Business rule validation
- Encapsulate rules in private methods (e.g., `validateBalance()`)
- Keep validation logic out of entities and repositories
