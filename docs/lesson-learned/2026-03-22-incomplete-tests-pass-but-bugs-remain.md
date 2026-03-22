# Incomplete Tests Pass But Bugs Remain - 2026-03-22

## Summary

**Tests passed, but bugs existed.** Comprehensive update tests revealed missing field handling that simple tests missed.

## The Problem

### Original Test: Passed but Incomplete

```java
@Test
void shouldUpdateCustomer() {
    UpdateCustomerRequest request = new UpdateCustomerRequest();
    request.setName("Updated Name");
    // ... test only verifies name is updated
    
    assertEquals("Updated Name", response.getName());
    // PASSES - but other fields not verified!
}
```

**Result:** Test passes. Developer moves on. But `industry`, `website`, `employeeCount`, etc. were NOT being saved.

### New Test: Comprehensive Field Verification

```java
@Test
void shouldUpdateCustomerWithAllFields() {
    UpdateCustomerRequest request = new UpdateCustomerRequest();
    request.setName("New Corporate Name");
    request.setIndustry("Technology");
    request.setWebsite("https://newwebsite.com");
    request.setEmployeeCount(500);
    // ... sets ALL fields
    
    // Verifies ALL fields are persisted
    verify(customerRepository).save(customerCaptor.capture());
    CorporateCustomer saved = customerCaptor.getValue();
    assertEquals("Technology", saved.getIndustry());
    assertEquals("https://newwebsite.com", saved.getWebsite());
    assertEquals(500, saved.getEmployeeCount());
}
```

**Result:** FAILS. Reveals `placeOfBirth` and `employmentStatus` missing for Individual customers.

## Root Cause

Tests were written to verify "the happy path works" instead of "all specified fields work correctly."

| Test Approach | What It Catches | What It Misses |
|---------------|-----------------|----------------|
| Verify one field | NPE, type errors | Missing field handling |
| Verify ALL fields | Missing handlers, type mismatches | - |

## The Bug Found

After adding comprehensive test for Individual customer update:

```
expected: <New City> but was: <Old City>
```

`placeOfBirth` and `employmentStatus` were NOT being updated in `CustomerService.updateCustomer()`.

**Fixed by adding:**
```java
if (request.getPlaceOfBirth() != null && customer instanceof IndividualCustomer) {
    ((IndividualCustomer) customer).setPlaceOfBirth(request.getPlaceOfBirth());
}

if (request.getEmploymentStatus() != null && customer instanceof IndividualCustomer) {
    ((IndividualCustomer) customer).setEmploymentStatus(status);
}
```

## Prevention Rules

### 1. Backend Tests: Verify All Request Fields

For every update/create test, verify ALL fields in the request are processed:

```java
@Test
void shouldUpdateCustomerWithAllFields() {
    // Given - set ALL fields in request
    UpdateCustomerRequest request = new UpdateCustomerRequest();
    request.setName("Updated");
    request.setTaxId("NEW-TAX");
    request.setIndustry("Technology");
    request.setWebsite("https://example.com");
    request.setEmployeeCount(100);
    // ... ALL CorporateCustomer fields
    
    // When
    CustomerResponse response = customerService.updateCustomer(customerId, request);
    
    // Then - verify ALL fields persisted
    verify(customerRepository).save(captor.capture());
    CorporateCustomer saved = captor.getValue();
    
    assertEquals("Updated", saved.getName());
    assertEquals("NEW-TAX", saved.getTaxId());
    assertEquals("Technology", saved.getIndustry());
    assertEquals("https://example.com", saved.getWebsite());
    assertEquals(100, saved.getEmployeeCount());
}
```

### 2. Request Object = Contract

`UpdateCustomerRequest` defines what CAN be updated. Test must verify EVERY field in the request is handled.

| Request Field | Test Must Verify |
|--------------|------------------|
| `name` | Name updated in entity |
| `industry` | Industry updated in entity |
| `website` | Website updated in entity |
| `employeeCount` | EmployeeCount updated in entity |
| ... | ... |

### 3. One Test Per Customer Type

| Customer Type | Test Name | Fields to Verify |
|--------------|-----------|------------------|
| Corporate | `shouldUpdateCorporateCustomerWithAllFields` | registrationNumber, industry, website, employeeCount, annualRevenue |
| SME | `shouldUpdateSMECustomerWithAllFields` | businessType, yearsInOperation, annualTurnover |
| Individual | `shouldUpdateIndividualCustomerWithAllFields` | dateOfBirth, placeOfBirth, nationality, employmentStatus |

### 4. Frontend Tests: Same Principle

Verify ALL fields from API response are displayed/processed:

```typescript
it('displays all customer fields from API response', () => {
  const apiResponse = {
    id: 1,
    type: 'INDIVIDUAL',
    name: 'John Doe',
    taxId: '123-45-6789',
    emails: ['john@example.com'],
    phones: [{ countryCode: '+1', number: '555-0100' }],
    // ... ALL fields
  };
  
  // Verify component displays ALL fields
  expect(screen.getByText('John Doe')).toBeInTheDocument();
  expect(screen.getByText('john@example.com')).toBeInTheDocument();
  // ... ALL fields
});
```

## Enforcement in Test Strategy

Add to [Test Strategy Template](../superpowers/templates/test-strategy.md):

```markdown
### Comprehensive Field Verification

Tests must verify ALL fields in a request/response, not just a subset:

```java
// WRONG - only verifies one field
@Test
void shouldUpdateCustomer() {
    request.setName("Updated");
    // Test only checks name...
}

// CORRECT - verifies all fields
@Test
void shouldUpdateCustomerWithAllFields() {
    request.setName("Updated");
    request.setIndustry("Tech");
    request.setWebsite("https://example.com");
    // ... ALL fields
    
    verify(customerRepository).save(captor.capture());
    // Assert ALL fields
}
```

For update operations, test per customer type (Corporate, SME, Individual).
```

## Key Takeaway

> **"Tests that only verify one field give false confidence. Tests that verify all fields find real bugs."**

The original `shouldUpdateCustomer()` test passed, but the code was broken for most fields. Comprehensive tests caught this.

## Related Issues

- Frontend: Mock data in tests didn't match actual API response shape
- Backend: Tests only verified `name` field, not `industry`, `website`, etc.

Both share the same root cause: **incomplete verification**.
