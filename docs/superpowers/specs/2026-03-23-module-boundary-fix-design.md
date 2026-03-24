# Technical Design Specification: Fix Module Boundary Violations

**Date:** 2026-03-23  
**Status:** Draft  
**Module:** Cross-cutting concern (all modules)  

## 1. Executive Summary

This technical design specifies the changes needed to fix module boundary violations in the banking system. The primary issue is that modules are incorrectly depending on internal implementation details of other modules (domain entities, repositories) instead of using their published API interfaces. This violates the modular monolith architecture principles and creates tight coupling.

## 2. Architecture Overview

The system follows a Modular Monolith architecture with clear separation between foundation modules and business modules. Key architectural principles include:

1. **Dependency Direction:** Foundation Modules → Business Modules (never reverse)
2. **Module Boundaries:** Modules can only depend on the API and DTO packages of other modules
3. **Encapsulation:** Internal implementation details (domain entities, repositories, internal services) are not exposed to other modules
4. **Communication:** Modules communicate through well-defined interfaces (Java interfaces in API packages)

## 3. Current Issues Identified

Through code analysis, the following architectural violations were found:

### 3.1 AccountInquiryService.java
- **File:** `src/main/java/com/banking/account/service/AccountInquiryService.java`
- **Violation:** Direct import and use of `com.banking.customer.repository.CustomerRepository`
- **Should use:** `com.banking.customer.api.CustomerQueryService`

### 3.2 DemoDataLoader.java
- **File:** `src/main/java/com/banking/account/DemoDataLoader.java`
- **Violations:**
  - Direct import of `com.banking.product.domain.entity.Product`
  - Direct import of `com.banking.product.domain.entity.ProductVersion`
  - Direct import of `com.banking.product.repository.ProductRepository`
  - Direct import of `com.banking.product.repository.ProductVersionRepository`
- **Should use:** `com.banking.product.api.ProductQueryService` for product lookups

### 3.3 LoanAccount.java
- **File:** `src/main/java/com/banking/account/domain/entity/LoanAccount.java`
- **Violation:** Direct import of `com.banking.customer.domain.entity.Customer`
- **Should use:** Customer ID (Long) reference only, with service lookup when customer details are needed

## 4. Proposed Solution

### 4.1 Fix AccountInquiryService.java
Replace direct CustomerRepository usage with CustomerQueryService:

```java
// Before
private final CustomerRepository customerRepository;
public AccountInquiryService(AccountRepository accountRepository,
                             AccountHolderRepository accountHolderRepository,
                             CustomerRepository customerRepository,
                             TransactionService transactionService) {
    this.customerRepository = customerRepository;
}

// After
private final CustomerQueryService customerQueryService;
public AccountInquiryService(AccountRepository accountRepository,
                             AccountHolderRepository accountHolderRepository,
                             CustomerQueryService customerQueryService,
                             TransactionService transactionService) {
    this.customerQueryService = customerQueryService;
}

// Usage change
// Before: Customer customer = customerRepository.findById(customerId);
// After: CustomerDTO customerDTO = customerQueryService.findById(customerId);
```

### 4.2 Fix DemoDataLoader.java
Replace direct repository and entity usage with API services:

```java
// Before
private final ProductRepository productRepository;
private final ProductVersionRepository productVersionRepository;
// ...
Product current = new Product(...);
productRepository.save(current);

// After
private final ProductQueryService productQueryService;
// ...
// For demo data loading, we may need to keep some direct repository access
// but should wrap it in a service or use the API where possible
// For lookups, definitely use the API:
ProductVersionDTO productVersion = productQueryService.findActiveVersionByCode("CURRENT");
```

### 4.3 Fix LoanAccount.java
Remove direct Customer entity dependency and use ID reference only:

```java
// Before
import com.banking.customer.domain.entity.Customer;
// ...
public class LoanAccount extends Account {
    private Customer customer; // Direct entity reference
    // ...
}

// After
// Remove the import
public class LoanAccount extends Account {
    private Long customerId; // ID reference only
    // ...
    
    // When customer details are needed, use CustomerQueryService in service layer
    // Not in the entity itself
}
```

However, since LoanAccount is an entity that logically belongs to a customer, we need to consider:
1. Keep the customerId foreign key (which is already there based on the Account hierarchy)
2. Remove any direct Customer object references in the entity
3. Handle customer-related logic in service layers using the API

Looking at the existing Account entity, it already has a customerId field, so the LoanAccount shouldn't need a direct Customer reference.

## 5. Module Communication Patterns

### 5.1 Correct Patterns (to maintain)
- **Direct Interface Call:** Synchronous operations within process (Customer lookup during payment)
- **Event Publishing:** Asynchronous notifications (Customer created, account opened)
- **Domain Events:** Cross-module state changes (KYC status changed, limit exceeded)
- **Command Query Separation:** Read vs Write operations

### 5.2 Incorrect Patterns (to fix)
- Direct repository access across modules
- Direct entity references across modules
- Bypassing API interfaces for internal implementation access

## 6. Data Flow Changes

### 6.1 Account Inquiry Service
**Before:**
```
AccountInquiryService → CustomerRepository (direct DB access)
```

**After:**
```
AccountInquiryService → CustomerQueryService → CustomerRepository
```

### 6.2 Demo Data Loader
**Before:**
```
DemoDataLoader → ProductRepository/ProductVersionRepository (direct)
DemoDataLoader → Product/ProductVersion entities (direct)
```

**After:**
```
DemoDataLoader → ProductQueryService → ProductRepository/ProductVersionRepository
DemoDataLoader → ProductDTO/ProductVersionDTO (via API)
```

## 7. Interface Contracts

The following API interfaces are already defined and should be used:

### 7.1 CustomerQueryService
```java
package com.banking.customer.api;

public interface CustomerQueryService {
    CustomerDTO findById(Long id);
    boolean existsById(Long id);
    String getCustomerName(Long id);
}
```

### 7.2 ProductQueryService
```java
package com.banking.product.api;

public interface ProductQueryService {
    ProductDTO findById(Long id);
    ProductVersionDTO findActiveVersionByCode(String productCode);
    // ... other methods
}
```

## 8. Implementation Details

### 8.1 Package Structure Compliance
After fixes, modules should only have these cross-module dependencies:
- `com.banking.<module>.api` (service interfaces)
- `com.banking.<module>.dto` (data transfer objects)

No dependencies on:
- `com.banking.<module>.domain.entity`
- `com.banking.<module>.repository`
- `com.banking.<module>.service` (internal implementations)
- `com.banking.<module>.config`

### 8.2 Dependency Management
No new dependencies are needed - we're replacing existing incorrect dependencies with correct ones.

## 9. Testing Strategy

### 9.1 Unit Tests
- Update AccountInquiryServiceTest to mock CustomerQueryService instead of CustomerRepository
- Update DemoDataLoaderTest to use ProductQueryService mocks
- Verify that LoanAccount entity tests don't require Customer entity mocks

### 9.2 Integration Tests
- Verify that account inquiry functionality still works correctly
- Verify demo data loading still works
- Verify that account-customer relationships are maintained

### 9.3 Contract Tests
Ensure that API contracts between modules are maintained:
- Account module -> Customer module API
- Account module -> Product module API

## 10. Error Handling

No changes to error handling are required as we're maintaining the same functionality through different interfaces.

## 11. Security Considerations

No security implications - we're maintaining the same access patterns through proper interfaces.

## 12. Performance Impact

Minimal performance impact expected:
- Service layer delegation adds negligible overhead
- No additional network calls (all in-process)
- Potential benefit: better encapsulation may allow for caching optimizations in service layers

## 13. Cross-Cutting Concerns

### 12.1 Transaction Management
No changes needed - service layer methods already have appropriate transactional annotations.

### 12.2 Logging
No changes needed - logging can continue as before.

### 12.3 Monitoring
No changes needed - existing monitoring continues to work.

## 14. Implementation Plan

1. Fix AccountInquiryService.java (high priority - active violation)
2. Fix DemoDataLoader.java (medium priority - demo/data loading concern)
3. Fix LoanAccount.java (medium priority - entity design concern)
4. Run verification tests
5. Check for any additional similar violations in other modules

## 15. Open Issues

None identified at this time.

## 16. References

- Architecture Document: `docs/superpowers/architecture/system-design.md`
- Module Boundary Rules: Section 2.3 in system-design.md
- API Contract Enforcement: Section 5.5 in system-design.md