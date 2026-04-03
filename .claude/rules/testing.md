# Testing Rules

## Unit Tests (service layer)
- Use `@ExtendWith(MockitoExtension.class)` — never `@SpringBootTest` for unit tests
- Inject mocks via `@Mock` and construct the service under test manually in `@BeforeEach`
- Never mock the class under test; only mock its dependencies (repositories, other services)
- Use AssertJ: `assertThat(result).isEqualTo(expected)` — not JUnit `assertEquals`
- Verify repository interactions with `verify(repo).methodName(...)`

```java
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository);
    }
}
```

## Integration Tests (API layer)
- Use `@SpringBootTest @AutoConfigureMockMvc` — full Spring context
- Use `MockMvc` for HTTP calls, never call services directly
- Always assert status code AND response body JSON paths
- Extract and reuse JWT tokens across test methods via `@BeforeEach`

## Test naming
Pattern: `methodName_condition_expectedOutcome`
Examples: `createAccount_validChecking_succeeds`, `createAccount_negativeBalance_throwsValidationException`

## Coverage requirement
All service-layer business logic must have unit tests. Controllers are covered by integration tests.
