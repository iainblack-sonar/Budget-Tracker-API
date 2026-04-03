---
paths:
  - "budgetly-persistence/**"
---

# Persistence Module Rules

## Entities
- Required Lombok annotations on every entity: `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`
- Always specify `@Table(name = "...")` and `@Column(name = "...")` explicitly — never rely on defaults
- Primary key: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` with `Long id`
- Financial amounts: `BigDecimal` with `@Column(precision = 19, scale = 2)`
- Timestamps: `LocalDateTime` managed by `@PrePersist` / `@PreUpdate` hooks — never set manually
- Enums: `@Enumerated(EnumType.STRING)` — never `ORDINAL`
- Creation timestamps: `@Column(updatable = false)`

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
}
```

## Repositories
- Extend `JpaRepository<EntityType, Long>`
- Use Spring Data method naming conventions — avoid `@Query` unless the derived name becomes unreadable
- Always include `userId` in query methods that fetch user-owned data:
  - `findByIdAndUserId(Long id, Long userId)` — returns `Optional<Entity>`
  - `findAllByUserId(Long userId)` — returns `List<Entity>`
- Annotate with `@Repository`

## This module does NOT own
- Business logic or validation — that belongs in `budgetly-core`
- DTOs — those live in `budgetly-api`
- No service classes here; repositories only
