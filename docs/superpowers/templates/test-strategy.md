# Test Strategy Template

**Purpose:** Standard testing strategy for all modules. Reference this template in design specs instead of duplicating content.

**Lessons Learned:** Account module worktree merge build failures (2026-03-20).

---

## 1. Merge Failure Prevention

The Account module experienced significant merge failures from worktree. All modules must prevent these 4 failure categories:

| # | Failure Category | Prevention Rule |
|---|-----------------|-----------------|
| 1 | Stale Gradle cache — build passes in worktree but fails on clean merge | **Mandatory `./gradlew clean build`** before merge. CI must run clean build, never incremental. |
| 2 | Duplicate exception handler classes — Spring bean conflict from handlers in multiple packages | **Single exception handler per module** in `controller` package only. No handler in `exception/` package. Verify with `grep -r "XxxExceptionHandler" src/` returns exactly 1 result. |
| 3 | Gradle configuration syntax errors (Kotlin DSL) | **No Gradle changes** unless strictly needed. If needed, copy exact syntax from working module config. |
| 4 | Test mock mismatches | **Strict test conventions** (see Section 2) |

---

## 2. Test Conventions

### Error Code Consistency

All tests must reference error codes from exception class constants, not hardcoded strings:

```java
// WRONG — typo-prone, breaks if exception changes
assertEquals("PROD001", response.getErrorCode());

// CORRECT — reference the constant
assertEquals(ProductNotFoundException.ERROR_CODE, response.getErrorCode());
```

Each exception class must define its error code as a constant:
```java
public class ProductNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "PROD-001";
    // ...
}
```

### HTTP Status Code Verification

Tests must match the controller's actual return status. Document expected statuses per controller:

| HTTP Method | Success Status | Failure Status |
|-------------|---------------|----------------|
| POST (create) | 201 Created | 400/409 |
| GET | 200 OK | 404 |
| PUT | 200 OK | 400/404 |
| DELETE | 204 No Content | 404 |
| POST (action/lifecycle) | 200 OK | 400/403/404 |

### Mock Conventions

```java
// WRONG — when().thenThrow() for void methods
when(service.someVoidMethod(any(), any())).thenThrow(...);

// CORRECT — doThrow() for void methods
doThrow(new SomeException(...))
    .when(service).someVoidMethod(any(), any());

// CORRECT — doNothing() for void methods
doNothing().when(service).someVoidMethod(any(), any());

// CORRECT — when().thenReturn() for non-void methods
when(service.someMethod(any())).thenReturn(response);
```

### DTO Type Verification

Tests must use the exact DTO type the controller returns. If a service returns `ProductVersionResponse`, the test mock must return `ProductVersionResponse`, not `ProductDetailResponse`.

---

## 3. Pre-Merge Checklist

Before merging from worktree to main, ALL must pass:

- [ ] `./gradlew clean build` passes (clean, not incremental)
- [ ] `npm run test:coverage` passes (frontend)
- [ ] `npm run lint` passes (frontend)
- [ ] No duplicate classes: grep for handler/exception classes returns expected count
- [ ] All error codes in tests reference `ExceptionClass.ERROR_CODE` constants
- [ ] All mocked return types match actual service method return types
- [ ] All void method mocks use `doThrow()/doNothing()`, not `when().thenThrow()`
- [ ] All HTTP status assertions match the controller's `ResponseEntity` status codes

---

## 4. CI Pipeline Requirements

The CI pipeline (GitHub Actions) must enforce:

1. **`./gradlew clean build`** — always clean, never incremental
2. **`npm run test:coverage`** — frontend tests with coverage threshold
3. **`npm run lint`** — frontend lint check
4. **Block merge if any step fails** — no exceptions

---

## 5. Test Data Isolation

- Each test class uses `@Transactional` to roll back after each test
- Test data created via `@BeforeEach` setup, not shared across test classes
- No dependency on seed data — tests create their own fixtures

---

## 6. Coverage Requirements

Per AGENTS.md:
- **Backend:** XML reports in `build/test-results/test/`, minimum thresholds in Gradle config
- **Frontend:** `npm run test:coverage` generates HTML report in `coverage/`, minimum thresholds in Vitest config
- Both must pass before merge

---

## 7. Usage in Design Specs

In your design spec's Testing Strategy section, reference this template:

```markdown
## Testing Strategy

### Standards

Follows [Test Strategy Template](../templates/test-strategy.md) for merge failure prevention, test conventions, pre-merge checklist, and CI requirements.

### Module-Specific Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `XxxServiceTest` | CRUD operations | S1.1, S1.2 |
| `XxxVersionServiceTest` | Lifecycle state machine | S2.1-S2.9 |
| ... | ... | ... |
```
