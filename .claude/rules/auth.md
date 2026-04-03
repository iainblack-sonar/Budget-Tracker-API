---
paths:
  - "budgetly-auth/**"
---

# Auth Module Rules

## JWT
- JWT config (`secret`, `expiration-ms`) must come from `application.yml` via `@Value` — never hardcoded
- Token claims: `userId` as subject, `email` as a custom claim
- Use jjwt 0.12.x API (`Jwts.builder()`, `Jwts.parserBuilder()`)

## Filters
- Extend `OncePerRequestFilter` — guarantees single execution per request
- On invalid/missing token: do NOT throw — call `filterChain.doFilter()` and return silently
- Set authentication in `SecurityContextHolder` only after full token validation

## Security configuration
- CSRF must remain disabled (stateless API)
- Session creation policy: `STATELESS`
- `/auth/**` is the only public path — all other endpoints require authentication
- Password encoding: BCrypt strength 10 (`new BCryptPasswordEncoder(10)`)

## AuthenticatedUser
- `AuthenticatedUser` is a record carrying `userId` (Long) and `email` (String)
- It is set as the principal in the `UsernamePasswordAuthenticationToken`
- Controllers retrieve it via `@AuthenticationPrincipal AuthenticatedUser user` — do not change this contract

## This module does NOT own
- `UserEntity` — that lives in `budgetly-persistence`
- Business rules for what authenticated users can do — that's `budgetly-core`
