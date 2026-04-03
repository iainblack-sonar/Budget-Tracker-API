# Budgetly — Personal Budget Tracking API

Multi-module Spring Boot 3.4.1 / Java 21 Maven project.

## Module Structure

```
budgetly-api/            → REST controllers, DTOs, request validation
budgetly-core/           → Business logic, budget calculations, spending rules
budgetly-auth/           → JWT authentication, security filter chain
budgetly-persistence/    → JPA entities, repositories, Flyway migrations
budgetly-notifications/  → Email/Slack alerts on budget thresholds
budgetly-import/         → CSV/bank file parsing, transaction ingestion
budgetly-common/         → Shared exceptions, utilities, constants
```

## Architecture Rules (ENFORCED)

### Allowed Dependencies
```
api → core, auth
core → persistence, notifications, common
auth → common, persistence (for user lookup in AuthenticationService)
persistence → common
import → core
```

### Forbidden Dependencies
- `api → persistence` — API must go through core, never access DB directly
- `api → import` — import is a background process
- `import → persistence` — import hands data to core, never writes DB directly
- `notifications → auth` — no cross-cutting
- `persistence → core` — no reverse dependencies
- `anything → api` — api is the entry point only

## Coding Conventions

- **Constructor injection only** — never `@Autowired` on fields
- **DTOs** live in `budgetly-api` as Java `record` types
- **Entities** live in `budgetly-persistence`
- **All business logic** goes in `budgetly-core` services, never in controllers
- **Custom exceptions** must extend `BudgetlyException` in `budgetly-common`
- **Tests required** for all service-layer logic
- **Base package:** `com.budgetly` — each module uses `com.budgetly.<module>`
- **Import module package:** `com.budgetly.importer` (since `import` is a reserved word)

## Build Commands

```bash
./mvnw clean install          # Full build
./mvnw test                   # Run tests
./mvnw spring-boot:run -pl budgetly-api   # Start the app
./mvnw spotless:apply         # Fix formatting
./mvnw spotless:check         # Check formatting (pre-commit hook)
```

## Module Ownership

| Module | Owns | Does NOT own |
|--------|------|-------------|
| `budgetly-api` | Controllers, DTOs, request validation, exception handler, `application.yml` | Business logic, entities, DB access |
| `budgetly-core` | Service classes, business rules, validation logic | Controllers, entities, SQL |
| `budgetly-auth` | JWT generation/validation, security filter chain, `AuthenticationService` | User entity (that's in persistence) |
| `budgetly-persistence` | JPA entities, Spring Data repositories, Flyway migrations | Business logic, DTOs |
| `budgetly-notifications` | Alert dispatching (email, Slack) | Business rules for when to alert (that's core) |
| `budgetly-import` | File parsing, CSV mapping | Direct DB writes (hands off to core) |
| `budgetly-common` | Base exceptions, shared constants, utilities | Anything Spring-specific |

## Git Hooks

Located in `.hooks/` — activate with `git config core.hooksPath .hooks`

- **pre-commit:** `./mvnw spotless:check` — blocks commit if formatting is wrong
- **pre-push:** `./mvnw test` — blocks push if tests fail

## Key Versions

- Spring Boot 3.4.1
- Java 21 (target), Java 25 on build machine
- Lombok 1.18.38 (overridden from BOM for Java 25 compat)
- jjwt 0.12.6
- Spotless with google-java-format 1.27.0 (overridden for Java 25 compat)
- Flyway (managed by Spring Boot BOM)
- H2 for local dev, PostgreSQL for prod

## JWT Configuration

- Secret and expiration configured in `application.yml`
- Tokens carry userId and email as claims
- `Authorization: Bearer <token>` header required for protected endpoints
- `/auth/**` endpoints are public
