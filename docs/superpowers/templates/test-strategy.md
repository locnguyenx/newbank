# Test Strategy Template

**Purpose:** Standard testing strategy for all modules. Reference this template in design specs instead of duplicating content.

**Lessons Learned:**
- Account module worktree merge build failures (2026-03-20)
- Integration test infrastructure fixes: PostgreSQL testcontainers, mock conflicts, @WebMvcTest pitfalls (2026-03-24)

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

### Comprehensive Field Verification

Tests must verify ALL fields in a request/response, not just a subset. Incomplete tests give false confidence.

```java
// WRONG — only verifies one field passes
@Test
void shouldUpdateCustomer() {
    UpdateCustomerRequest request = new UpdateCustomerRequest();
    request.setName("Updated");
    // Only checks name is returned...
    assertEquals("Updated", response.getName());
    // PASSES but other fields broken!
}

// CORRECT — verifies all fields are persisted
@Test
void shouldUpdateCustomerWithAllFields() {
    UpdateCustomerRequest request = new UpdateCustomerRequest();
    request.setName("Updated");
    request.setIndustry("Technology");
    request.setWebsite("https://example.com");
    request.setEmployeeCount(500);
    // ... ALL fields in UpdateCustomerRequest

    verify(customerRepository).save(captor.capture());
    CorporateCustomer saved = captor.getValue();
    
    assertEquals("Updated", saved.getName());
    assertEquals("Technology", saved.getIndustry());
    assertEquals("https://example.com", saved.getWebsite());
    assertEquals(500, saved.getEmployeeCount());
}
```

**Rule:** If `UpdateCustomerRequest` has 15 fields, the test must verify all 15 are handled.

**Per-Customer-Type Tests:**

| Customer Type | Test Name | Fields to Verify |
|--------------|-----------|------------------|
| Corporate | `shouldUpdateCorporateCustomerWithAllFields` | registrationNumber, industry, website, employeeCount, annualRevenue |
| SME | `shouldUpdateSMECustomerWithAllFields` | businessType, yearsInOperation, annualTurnover |
| Individual | `shouldUpdateIndividualCustomerWithAllFields` | dateOfBirth, placeOfBirth, nationality, employmentStatus |

**Frontend Same Principle:**
```typescript
// WRONG — only verifies render
it('shows customer data', () => {
  expect(screen.getByText('John')).toBeInTheDocument();
});

// CORRECT — verifies ALL fields from API
it('displays all customer fields from API response', () => {
  expect(screen.getByText('John Doe')).toBeInTheDocument();
  expect(screen.getByText('john@example.com')).toBeInTheDocument();
  expect(screen.getByText('+1 555-0100')).toBeInTheDocument();
  // ... ALL fields
});
```

**Key Takeaway:** "Tests that only verify one field give false confidence. Tests that verify all fields find real bugs."

---

### Error Code Consistency

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

## 2.1 Frontend Test Guidelines

### API Response Shape Must Match Reality

Mock data in tests MUST match the actual API response shape, not assumed shapes.

```typescript
// WRONG — Assumed shape based on form fields
const mockCustomer = {
  customerType: 'INDIVIDUAL',
  firstName: 'John',
  lastName: 'Doe',
  email: 'john@example.com',
};

// CORRECT — Matches actual API response from backend
const mockCustomer = {
  id: 1,
  type: 'INDIVIDUAL',           // NOT 'customerType'
  name: 'John Doe',              // NOT 'firstName' + 'lastName'
  emails: ['john@example.com'],   // NOT 'email' (it's an array)
  phones: [{ countryCode: '+1', number: '555-0100', type: 'MOBILE' }],
};
```

**Before writing mocks, always verify by:**
1. Calling the actual API endpoint
2. Checking the API documentation/OpenAPI spec
3. Reading the TypeScript types generated from OpenAPI

### Test Both Create AND Edit Modes

Forms have two modes that behave differently:

| Mode | Route | Tests Required |
|------|-------|---------------|
| Create | `/customers/new` | Form renders, validates, submits correct data |
| Edit | `/customers/:id/edit` | Form populates from API, submits correct data |

```typescript
describe('CustomerForm', () => {
  describe('Create Mode', () => {
    it('renders Create button', () => { /* ... */ });
    it('submits with correct data structure', () => { /* ... */ });
  });

  describe('Edit Mode', () => {
    it('populates fields from API response', () => { /* ... */ });
    it('submits Update with correct data structure', () => { /* ... */ });
  });
});
```

### Verify Form → API Data Mapping

Tests must verify that form field values are correctly transformed before sending to API:

```typescript
it('submits individual name as combined first+last', async () => {
  fireEvent.change(screen.getByLabelText('First Name'), { target: { value: 'Jane' } });
  fireEvent.change(screen.getByLabelText('Last Name'), { target: { value: 'Smith' } });
  
  fireEvent.click(screen.getByText('Create'));
  
  await waitFor(() => {
    expect(customerService.create).toHaveBeenCalledWith(
      expect.objectContaining({ name: 'Jane Smith' })  // NOT firstName + lastName
    );
  });
});
```

### Mock Service Methods, Not Internal Implementation

Mock at the service boundary, not inside components:

```typescript
// WRONG — Mocks internal implementation details
vi.spyOn(service, 'updateCustomer').mockResolvedValue(...);

// CORRECT — Mocks the public service interface
vi.spyOn(customerService, 'update').mockResolvedValue(...);
```

### Required Test Scenarios for Forms

Every form must have these tests:

| # | Scenario | What to Verify |
|---|---------|----------------|
| 1 | Render in create mode | All fields visible, Create button shown |
| 2 | Render in edit mode | All fields visible, Update button shown |
| 3 | Form validation | Required fields trigger errors |
| 4 | Form population (edit) | Fields pre-filled from API response |
| 5 | Submit creates data | API called with correct structure |
| 6 | Submit updates data | API called with correct structure |
| 7 | API error handling | User-friendly error message shown |

### Test Error Display, Not Just Success

```typescript
it('displays API error message', async () => {
  vi.spyOn(customerService, 'create').mockRejectedValue({
    response: { data: { message: 'Customer already exists' } }
  });
  
  // Submit form...
  
  await waitFor(() => {
    expect(screen.getByText('Customer already exists')).toBeInTheDocument();
  });
});
```

---

## 3. Integration Test Infrastructure

### Shared PostgreSQL Container

All integration tests must use the shared `AbstractIntegrationTest` base class:

```java
@SpringBootTest(classes = BankingApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // Required to avoid mock conflicts
class MyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MyRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();  // NOT deleteAll()
    }

    @Test
    void shouldWork() {
        // test code
    }
}
```

**Key Points:**
- ✅ DO extend `AbstractIntegrationTest` for ANY test needing database
- ✅ DO use `@DirtiesContext(classMode = AFTER_CLASS)` if test defines `@MockBean`
- ✅ DO use `deleteAllInBatch()` in `@BeforeEach`
- ❌ DON'T use `@WebMvcTest` for controller tests (see section 4)
- ❌ DON'T use `deleteAll()` - causes `StaleObjectStateException`

### Preferred Test Approach: `@SpringBootTest` + `@AutoConfigureMockMvc`

For **controller tests**, always prefer `@SpringBootTest` over `@WebMvcTest`:

```java
@SpringBootTest(classes = BankingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;  // Only mock external dependencies

    @Test
    void shouldReturnCustomer() throws Exception {
        // test code
    }
}
```

**Advantages:**
- Loads full application context (suits modular monolith)
- Works with shared PostgreSQL container
- No duplicate mock conflicts
- Proper integration of all layers (controller → service → repository)

### Problems with `@WebMvcTest`

**Avoid `@WebMvcTest`** - it causes duplicate mock definition errors in a modular monolith:

```
@WebMvcTest(controllers = CustomerController.class)
@MockBean
private CustomerService customerService;  // ERROR: Duplicate mock definition
```

**Reasons:**
1. `@WebMvcTest` tries to load a "web slice" but often loads full context anyway
2. Combined with `@MockBean` → conflicts with beans auto-configured by `@SpringBootApplication`
3. Multiple test classes caching context compound the problem

**If you must use `@WebMvcTest`** (not recommended):
```java
@WebMvcTest(controllers = CustomerController.class, 
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {CustomerController.class, TestConfig.class})
@DirtiesContext(classMode = AFTER_CLASS)  // Prevent cache conflicts
```

### Schema Validation

- `application-test.yml` uses `ddl-auto: validate` (via `AbstractIntegrationTest`)
- Flyway migrations run automatically on test startup
- If schema validation fails: **add missing migration**, don't change `ddl-auto`

### TOTP Tests

**Disable TOTP tests** - they are inherently flaky due to 30-second time windows:
- Code generation and verification happen in different timestamps
- Even 1-second delay can invalidate the code
- This is a real constraint, not a test bug

```
@Test
@Disabled("TOTP timing sensitivity - codes expire every 30 seconds")
void verifyCode_withValidCode_returnsTrue() { ... }
```

**Better solution:** Refactor production code to inject a `TimeProvider` for deterministic testing.

**Reference:** See AGENTS.md "Test Infrastructure Guidelines" for complete details on PostgreSQL testcontainers, mock conflicts, and database cleanup patterns.

---

## 4. Pre-Merge Checklist

Before merging from worktree to main, ALL must pass:

- [ ] `./gradlew clean build` passes (clean, not incremental)
- [ ] `npm run test:coverage` passes (frontend)
- [ ] `npm run lint` passes (frontend)
- [ ] No duplicate classes: grep for handler/exception classes returns expected count
- [ ] All error codes in tests reference `ExceptionClass.ERROR_CODE` constants
- [ ] All mocked return types match actual service method return types
- [ ] All void method mocks use `doThrow()/doNothing()`, not `when().thenThrow()`
- [ ] All HTTP status assertions match the controller's `ResponseEntity` status codes
- [ ] **All request fields are tested** — update tests verify ALL fields in request are persisted

### Backend Test Checklist

- [ ] Create tests verify response contains all expected fields
- [ ] Update tests verify ALL fields in request are persisted (not just one field)
- [ ] Per-customer-type tests for Corporate, SME, Individual when applicable
- [ ] Mock data matches actual API response shape
- [ ] Error handling is tested

### Integration Test Checklist

- [ ] **All integration tests extend `AbstractIntegrationTest`** for shared PostgreSQL container
- [ ] Tests that define `@MockBean` include `@DirtiesContext(classMode = AFTER_CLASS)`
- [ ] All repository cleanup uses `deleteAllInBatch()` (NOT `deleteAll()`)
- [ ] Schema validation passes (`ddl-auto: validate` in `application-test.yml`)
- [ ] No tests use `@WebMvcTest` - prefer `@SpringBootTest` + `@AutoConfigureMockMvc`
- [ ] TOTP tests are disabled with clear reason (30-second timing window)

### Frontend Test Checklist

- [ ] Mock data matches actual API response (verify with API call or OpenAPI spec)
- [ ] Tests cover BOTH create and edit modes
- [ ] Form → API data mapping is verified (not just render)
- [ ] Error handling is tested (API errors display user-friendly messages)
- [ ] Field names match API: `type` not `customerType`, `name` not `firstName`/`lastName`, `emails[]` not `email`
- [ ] **ALL fields from API response are verified** — not just some fields

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

---

## 8. Key Lessons Learned (Recent Fixes)

### Issue: Customer Forms Not Saving Correctly

**Root Cause:** Mock data in tests didn't match actual API response shape.

**Prevention:** Always verify API response shape before writing tests:
```bash
# Test actual API response
curl -H "X-Username: system" http://localhost:8080/api/customers/1
```

**Key Differences Found:**
| What Tests Assumed | What API Actually Returns |
|-------------------|------------------------|
| `customerType: 'INDIVIDUAL'` | `type: 'INDIVIDUAL'` |
| `firstName`, `lastName` | `name: 'John Doe'` |
| `email: 'john@example.com'` | `emails: ['john@example.com']` |
| `phone: '555-0100'` | `phones: [{countryCode: '+1', number: '555-0100'}]` |

### Issue: Edit Mode Form Didn't Populate

**Root Cause:** Form components expected `customer.customerType` but API returns `customer.type`.

**Prevention:** 
- Always test edit mode, not just create mode
- Verify field names match between form, Redux state, and API response
