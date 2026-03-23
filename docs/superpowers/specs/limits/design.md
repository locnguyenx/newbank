# Technical Design: Limits Management

**Version:** 1.0  
**Date:** 2026-03-21  
**Status:** Draft  
**Module:** Limits Management (`com.banking.limits`)  
**BRD Reference:** `docs/superpowers/brds/limits/brd.md`  
**BDD Reference:** `docs/superpowers/specs/limits/bdd.md`

---

## 1. Architecture Overview

### Module Placement

Limits Management is a **foundation module** consumed by business modules (Payments, Trade Finance) for real-time transaction validation.

```
┌─────────────────────────────────────────────────────────┐
│                  Modular Monolith                       │
│                                                         │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                │
│  │ Payments │ │ Trade    │ │ Cash     │                │
│  │ Module   │ │ Finance  │ │ Mgmt     │                │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘                │
│       │             │            │                       │
│       └─────────────┼────────────┘                       │
│                     ▼                                    │
│              ┌─────────────┐                            │
│              │   Limits    │  ◄── Foundation Module     │
│              │   Module    │                            │
│              └──────┬──────┘                            │
│                     │ references                        │
│       ┌─────────────┼─────────────┐                     │
│       ▼             ▼             ▼                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                │
│  │ Customer │ │ Account  │ │ Product  │                │
│  └──────────┘ └──────────┘ └──────────┘                │
└─────────────────────────────────────────────────────────┘
```

### Module Boundaries

- **Inbound:** Business modules call `LimitCheckService.checkLimit()` for real-time enforcement
- **Outbound:** References customer IDs, account numbers, product codes as plain strings (no JPA joins)
- **Critical path:** Limit check must be < 10ms — design for speed over flexibility

### Package Structure

```
com.banking.limits/
├── domain/
│   ├── entity/
│   │   ├── LimitDefinition.java
│   │   ├── ProductLimit.java
│   │   ├── CustomerLimit.java
│   │   ├── AccountLimit.java
│   │   ├── ApprovalThreshold.java
│   │   ├── LimitUsage.java
│   │   └── ApprovalRequest.java
│   └── enums/
│       ├── LimitType.java
│       ├── LimitStatus.java
│       ├── LimitCheckResult.java
│       └── ApprovalStatus.java
├── repository/
│   ├── LimitDefinitionRepository.java
│   ├── ProductLimitRepository.java
│   ├── CustomerLimitRepository.java
│   ├── AccountLimitRepository.java
│   ├── ApprovalThresholdRepository.java
│   ├── LimitUsageRepository.java
│   └── ApprovalRequestRepository.java
├── service/
│   ├── LimitDefinitionService.java
│   ├── LimitAssignmentService.java
│   ├── LimitCheckService.java
│   ├── LimitUsageService.java
│   ├── ApprovalService.java
│   └── LimitQueryService.java
├── controller/
│   ├── LimitDefinitionController.java
│   ├── LimitAssignmentController.java
│   ├── LimitCheckController.java
│   └── ApprovalController.java
├── dto/
│   ├── request/
│   │   ├── CreateLimitDefinitionRequest.java
│   │   ├── UpdateLimitDefinitionRequest.java
│   │   ├── AssignLimitRequest.java
│   │   ├── LimitCheckRequest.java
│   │   └── ApprovalActionRequest.java
│   └── response/
│       ├── LimitDefinitionResponse.java
│       ├── ProductLimitResponse.java
│       ├── CustomerLimitResponse.java
│       ├── AccountLimitResponse.java
│       ├── LimitCheckResponse.java
│       ├── ApprovalRequestResponse.java
│       └── EffectiveLimitResponse.java
├── mapper/
│   └── LimitMapper.java
└── exception/
    ├── LimitNotFoundException.java
    ├── LimitAlreadyExistsException.java
    ├── InvalidLimitTypeException.java
    ├── LimitExceededException.java
    ├── ApprovalRequestNotFoundException.java
    └── InvalidApprovalActionException.java
```

---

## 2. Entity Design (Data Model)

### Entity Relationship Diagram

```
limit_definitions (1) ──── (N) product_limits
                    ──── (N) customer_limits
                    ──── (N) account_limits
                    ──── (N) approval_thresholds
                    ──── (N) limit_usage

approval_requests ─── references limit_definition
```

### Entity Definitions

#### LimitDefinition

```java
@Entity
@Table(name = "limit_definitions")
public class LimitDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "limit_type", nullable = false)
    private LimitType limitType;           // DAILY, WEEKLY, MONTHLY, PER_TRANSACTION

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LimitStatus status = LimitStatus.ACTIVE;

    @Embedded
    private AuditFields audit;
}
```

#### ProductLimit

```java
@Entity
@Table(name = "product_limits", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"limit_definition_id", "product_code"})
})
public class ProductLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "product_code", nullable = false)
    private String productCode;            // no JPA FK — reference only

    @Column(precision = 19, scale = 4)
    private BigDecimal overrideAmount;     // null = use definition amount

    @Embedded
    private AuditFields audit;
}
```

#### CustomerLimit

```java
@Entity
@Table(name = "customer_limits", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"limit_definition_id", "customer_id"})
})
public class CustomerLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;
}
```

#### AccountLimit

```java
@Entity
@Table(name = "account_limits", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"limit_definition_id", "account_number"})
})
public class AccountLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;
}
```

#### ApprovalThreshold

```java
@Entity
@Table(name = "approval_thresholds", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"limit_definition_id"})
})
public class ApprovalThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "absolute_amount", precision = 19, scale = 4)
    private BigDecimal absoluteAmount;

    @Column(name = "percentage_of_limit")
    private Integer percentageOfLimit;     // 0-100, null = use absolute

    @Embedded
    private AuditFields audit;
}
```

#### LimitUsage

```java
@Entity
@Table(name = "limit_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"limit_definition_id", "account_number", "period_start"})
})
public class LimitUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "cumulative_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal cumulativeAmount = BigDecimal.ZERO;
}
```

#### ApprovalRequest

```java
@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "transaction_reference", nullable = false)
    private String transactionReference;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "decision_at")
    private LocalDateTime decisionAt;

    @Embedded
    private AuditFields audit;
}
```

### Enums

```java
public enum LimitType {
    DAILY, WEEKLY, MONTHLY, PER_TRANSACTION
}

public enum LimitStatus {
    ACTIVE, INACTIVE
}

public enum LimitCheckResult {
    ALLOWED, REJECTED, REQUIRES_APPROVAL
}

public enum ApprovalStatus {
    PENDING, APPROVED, REJECTED
}
```

---

## 3. Service Layer Design

### LimitDefinitionService

```java
@Service
@Transactional
public class LimitDefinitionService {

    LimitDefinitionResponse createLimit(CreateLimitDefinitionRequest request);
    LimitDefinitionResponse updateLimit(Long limitId, UpdateLimitDefinitionRequest request);
    void activateLimit(Long limitId);
    void deactivateLimit(Long limitId);
    LimitDefinitionResponse getLimit(Long limitId);
    List<LimitDefinitionResponse> getAllLimits(LimitStatus status);
}
```

### LimitAssignmentService

```java
@Service
@Transactional
public class LimitAssignmentService {

    // Product level
    ProductLimitResponse assignToProduct(Long limitId, String productCode, BigDecimal overrideAmount);
    void unassignFromProduct(Long limitId, String productCode);
    List<ProductLimitResponse> getProductLimits(String productCode);

    // Customer level
    CustomerLimitResponse assignToCustomer(Long limitId, Long customerId, BigDecimal overrideAmount);
    void unassignFromCustomer(Long limitId, Long customerId);
    List<CustomerLimitResponse> getCustomerLimits(Long customerId);

    // Account level
    AccountLimitResponse assignToAccount(Long limitId, String accountNumber, BigDecimal overrideAmount);
    void unassignFromAccount(Long limitId, String accountNumber);
    List<AccountLimitResponse> getAccountLimits(String accountNumber);
}
```

### LimitCheckService (critical path)

```java
@Service
@Transactional(readOnly = true)
public class LimitCheckService {

    /**
     * Primary entry point for downstream modules.
     * Resolution order: Account → Customer → Product (most specific wins).
     * Returns result with approvalRequired flag.
     */
    LimitCheckResponse checkLimit(LimitCheckRequest request);
}
```

**LimitCheckRequest:**
```java
public class LimitCheckRequest {
    private String accountNumber;
    private Long customerId;
    private String productCode;
    private BigDecimal transactionAmount;
    private String currency;
    private String transactionReference;
}
```

**LimitCheckResponse:**
```java
public class LimitCheckResponse {
    private LimitCheckResult result;        // ALLOWED, REJECTED, REQUIRES_APPROVAL
    private BigDecimal effectiveLimit;       // resolved limit amount
    private BigDecimal currentUsage;         // cumulative usage this period
    private BigDecimal remainingAmount;      // effectiveLimit - currentUsage
    private boolean approvalRequired;        // true if above threshold
    private String rejectionReason;          // if REJECTED
}
```

**Resolution logic:**
1. Look up AccountLimit for accountNumber + limitType
2. If not found, look up CustomerLimit for customerId + limitType
3. If not found, look up ProductLimit for productCode + limitType
4. If not found, no limit enforced → ALLOWED

### LimitUsageService

```java
@Service
@Transactional
public class LimitUsageService {

    void recordUsage(Long limitId, String accountNumber, BigDecimal amount, LocalDate date);
    BigDecimal getCumulativeUsage(Long limitId, String accountNumber, LocalDate periodStart);
    void resetExpiredPeriods();  // scheduled job
}
```

**Scheduled job — daily at midnight:**
```java
@Scheduled(cron = "0 0 0 * * ?")
public void resetExpiredPeriods() {
    // Reset DAILY usages where period_end < today
    // Reset WEEKLY usages where period_end < today and week boundary crossed
    // Reset MONTHLY usages where period_end < today and month boundary crossed
}
```

### ApprovalService

```java
@Service
@Transactional
public class ApprovalService {

    ApprovalRequestResponse createApprovalRequest(String transactionRef, Long limitId, BigDecimal amount, String currency, String accountNumber);
    List<ApprovalRequestResponse> getPendingApprovals(Pageable pageable);
    void approve(Long requestId, String approverUsername);
    void reject(Long requestId, String approverUsername, String reason);
}
```

### LimitQueryService (for downstream modules)

```java
@Service
@Transactional(readOnly = true)
public class LimitQueryService {

    EffectiveLimitResponse getEffectiveLimit(String accountNumber, Long customerId, String productCode, String currency, LimitType type);
    List<EffectiveLimitResponse> getAllEffectiveLimits(String accountNumber, Long customerId, String productCode, String currency);
}
```

---

## 4. Controller / API Design

### LimitDefinitionController (`/api/limits/definitions`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/limits/definitions` | Create limit | S1.1, S1.2 |
| GET | `/api/limits/definitions` | List limits | - |
| GET | `/api/limits/definitions/{id}` | Get limit | - |
| PUT | `/api/limits/definitions/{id}` | Update limit | - |
| PUT | `/api/limits/definitions/{id}/activate` | Activate | S1.5 |
| PUT | `/api/limits/definitions/{id}/deactivate` | Deactivate | S1.4 |

### LimitAssignmentController (`/api/limits/assignments`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/limits/assignments/product` | Assign to product | S2.1 |
| POST | `/api/limits/assignments/customer` | Assign to customer | S3.1 |
| POST | `/api/limits/assignments/account` | Assign to account | S4.1 |
| GET | `/api/limits/assignments/product/{code}` | Get product limits | S2.2 |
| GET | `/api/limits/assignments/customer/{id}` | Get customer limits | - |
| GET | `/api/limits/assignments/account/{num}` | Get account limits | - |
| DELETE | `/api/limits/assignments/{type}/{id}` | Unassign | - |

### LimitCheckController (`/api/limits/check`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/limits/check` | Check limit | S5.1, S5.2, S5.3 |
| GET | `/api/limits/effective?accountNumber=&customerId=&productCode=` | Get effective limits | S3.2, S4.2 |

### ApprovalController (`/api/limits/approvals`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| GET | `/api/limits/approvals/pending` | List pending | S7.1 |
| POST | `/api/limits/approvals/{id}/approve` | Approve | S7.2 |
| POST | `/api/limits/approvals/{id}/reject` | Reject | S7.3 |

---

## 5. Frontend Design

### Sidebar Entry

"Limit Management" under "System" section.

### Pages

| Page | Route | Description |
|------|-------|-------------|
| LimitDefinitionListPage | `/limits` | List limit definitions |
| LimitDefinitionFormPage | `/limits/new` / `/limits/:id/edit` | Create/edit limit |
| LimitAssignmentPage | `/limits/assignments` | Assign limits to products/customers/accounts |
| ApprovalDashboardPage | `/limits/approvals` | Pending approval queue |

### Redux Slice

```
store/slices/limitSlice.ts
```

### Services

```
services/limitService.ts
```

### Types

```
types/limit.types.ts
```

---

## 6. Database Migration

### V1__create_limits_schema.sql

```sql
-- Limit definitions
CREATE TABLE limit_definitions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    limit_type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_limit_definitions_type ON limit_definitions(limit_type);
CREATE INDEX idx_limit_definitions_status ON limit_definitions(status);

-- Product-level limits
CREATE TABLE product_limits (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id),
    product_code VARCHAR(50) NOT NULL,
    override_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (limit_definition_id, product_code)
);

CREATE INDEX idx_product_limits_product ON product_limits(product_code);

-- Customer-level limits
CREATE TABLE customer_limits (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id),
    customer_id BIGINT NOT NULL,
    override_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (limit_definition_id, customer_id)
);

CREATE INDEX idx_customer_limits_customer ON customer_limits(customer_id);

-- Account-level limits
CREATE TABLE account_limits (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id),
    account_number VARCHAR(30) NOT NULL,
    override_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (limit_definition_id, account_number)
);

CREATE INDEX idx_account_limits_account ON account_limits(account_number);

-- Approval thresholds
CREATE TABLE approval_thresholds (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id) UNIQUE,
    absolute_amount NUMERIC(19,4),
    percentage_of_limit INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Limit usage tracking
CREATE TABLE limit_usage (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id),
    account_number VARCHAR(30) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    cumulative_amount NUMERIC(19,4) NOT NULL DEFAULT 0,
    UNIQUE (limit_definition_id, account_number, period_start)
);

CREATE INDEX idx_limit_usage_account ON limit_usage(account_number);

-- Approval requests
CREATE TABLE approval_requests (
    id BIGSERIAL PRIMARY KEY,
    limit_definition_id BIGINT NOT NULL REFERENCES limit_definitions(id),
    transaction_reference VARCHAR(50) NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    account_number VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rejection_reason TEXT,
    approved_by VARCHAR(100),
    decision_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_approval_requests_status ON approval_requests(status);
CREATE INDEX idx_approval_requests_account ON approval_requests(account_number);
```

---

## 7. Integration with Downstream Modules

### Read Interface

Business modules call `LimitCheckService.checkLimit()` via in-process injection:

```java
// In PaymentService
LimitCheckResponse response = limitCheckService.checkLimit(
    new LimitCheckRequest()
        .setAccountNumber(accountNumber)
        .setCustomerId(customerId)
        .setProductCode(productCode)
        .setTransactionAmount(amount)
        .setCurrency("USD")
        .setTransactionReference(txnRef)
);

if (response.getResult() == REJECTED) {
    throw new LimitExceededException(response.getRejectionReason());
}
if (response.isApprovalRequired()) {
    approvalService.createApprovalRequest(txnRef, ...);
}
```

### Event-Driven Integration with Account Module

The Limits module listens for `AccountOpenedEvent` published by the Account module to automatically assign product limits when a new account is created.

**Why Event-Driven?**
- **Loose Coupling:** Account module doesn't directly call `LimitAssignmentService`; it just publishes an event
- **Resilience:** Limit assignment failures don't block account creation (fire-and-forget)
- **Change Isolation:** Account module can change without affecting limits; limits logic can change without account changes
- **Architectural Compliance:** Eliminates direct dependency on `LimitAssignmentService` (was violating module boundaries)

**Event Listener Implementation:**

```java
@Component
public class AccountOpenedEventListener {

    private final LimitAssignmentService limitAssignmentService;

    @EventListener
    public void handleAccountOpened(AccountOpenedEvent event) {
        log.info("Processing AccountOpenedEvent for account: {}, product: {}", 
                 event.getAccountNumber(), event.getProductCode());

        List<ProductLimitResponse> productLimits = 
            limitAssignmentService.getProductLimits(event.getProductCode());
        
        for (ProductLimitResponse productLimit : productLimits) {
            try {
                limitAssignmentService.assignToAccount(
                    productLimit.getLimitDefinitionId(),
                    event.getAccountNumber(),
                    productLimit.getOverrideAmount()
                );
                log.info("Assigned limit {} to account {}", 
                         productLimit.getLimitDefinitionId(), 
                         event.getAccountNumber());
            } catch (Exception e) {
                log.warn("Failed to assign limit {} to account {}: {}", 
                         productLimit.getLimitDefinitionId(), 
                         event.getAccountNumber(), 
                         e.getMessage());
                // Continue with other limits - don't fail the entire event
            }
        }
    }
}
```

**Event Data Contract:**

The `AccountOpenedEvent` provides all necessary data:
- `accountNumber`: The newly created account number
- `productCode`: The product code (e.g., "CURRENT", "SAVINGS")
- `customerId`: The primary customer ID
- `currency`, `initialBalance`: For limit calculations if needed

**Error Handling:**
- Individual limit assignment failures are logged as WARN but don't stop processing
- Global exception handler catches unexpected errors and logs ERROR
- No retry logic (fire-and-forget); manual intervention may be needed if assignment fails

**Future Enhancement:**
- Bridge events to Kafka for distributed processing (if limits module scales to separate service)
- Add dead-letter queue handling for persistent failures
- Implement compensation actions for failed assignments

---

## 8. Error Handling

| Exception | Error Code | HTTP Status | Trigger |
|-----------|-----------|-------------|---------|
| `LimitNotFoundException` | LIM-001 | 404 | Limit ID not found |
| `LimitAlreadyExistsException` | LIM-002 | 409 | Duplicate limit assignment |
| `InvalidLimitTypeException` | LIM-003 | 400 | Invalid limit type value |
| `LimitExceededException` | LIM-004 | 400 | Transaction exceeds limit |
| `ApprovalRequestNotFoundException` | LIM-005 | 404 | Approval request not found |
| `InvalidApprovalActionException` | LIM-006 | 400 | Invalid approval action |

### Global Exception Handler

```java
@RestControllerAdvice(basePackages = "com.banking.limits")
public class LimitExceptionHandler {
    // Follows existing pattern from GlobalExceptionHandler in customer module
}
```

---

## 9. Testing Strategy

### Standards

Follows [Test Strategy Template](../templates/test-strategy.md) for:
- Merge failure prevention (stale cache, duplicate classes, Gradle syntax, mock conventions)
- Test conventions (error code constants, HTTP status verification, mock patterns, DTO types)
- Pre-merge checklist
- CI pipeline requirements
- Test data isolation
- Coverage requirements

### Module-Specific Exception Constants

Each exception class must define `ERROR_CODE` as a public constant:

| Exception | Error Code |
|-----------|-----------|
| `LimitNotFoundException` | LIM-001 |
| `LimitAlreadyExistsException` | LIM-002 |
| `InvalidLimitTypeException` | LIM-003 |
| `LimitExceededException` | LIM-004 |
| `ApprovalRequestNotFoundException` | LIM-005 |
| `InvalidApprovalActionException` | LIM-006 |

### Module-Specific Unit Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `LimitDefinitionServiceTest` | CRUD, activate/deactivate | S1.1-S1.5 |
| `LimitAssignmentServiceTest` | Product/customer/account assignment | S2.1-S2.2, S3.1-S3.2, S4.1-S4.2 |
| `LimitCheckServiceTest` | Resolution logic, enforcement | S3.2, S4.2, S5.1-S5.5 |
| `ApprovalServiceTest` | Approval workflow | S7.1-S7.3 |

### Module-Specific Integration Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `LimitDefinitionControllerTest` | Limit CRUD API | S1.1-S1.5 |
| `LimitCheckControllerTest` | Check API | S5.1-S5.5 |
| `ApprovalControllerTest` | Approval API | S7.1-S7.3 |
