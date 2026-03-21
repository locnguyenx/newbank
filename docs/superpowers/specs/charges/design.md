# Technical Design: Charges Management

**Version:** 1.0  
**Date:** 2026-03-21  
**Status:** Draft  
**Module:** Charges Management (`com.banking.charges`)  
**BRD Reference:** `docs/superpowers/brds/charges/brd.md`  
**BDD Reference:** `docs/superpowers/specs/charges/bdd.md`

---

## 1. Architecture Overview

### Module Placement

Charges Management is a **foundation module** consumed by business modules (Payments, Cash Management) for fee calculation and interest management.

```
┌─────────────────────────────────────────────────────────┐
│                  Modular Monolith                       │
│                                                         │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                │
│  │ Payments │ │ Cash     │ │ Trade    │                │
│  │ Module   │ │ Mgmt     │ │ Finance  │                │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘                │
│       │             │            │                       │
│       └─────────────┼────────────┘                       │
│                     ▼                                    │
│              ┌─────────────┐                            │
│              │  Charges    │  ◄── Foundation Module     │
│              │  Module     │                            │
│              └──────┬──────┘                            │
│                     │ references                        │
│       ┌─────────────┼─────────────┐                     │
│       ▼             ▼             ▼                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                │
│  │ Product  │ │ Account  │ │ Customer │                │
│  └──────────┘ └──────────┘ └──────────┘                │
└─────────────────────────────────────────────────────────┘
```

### Module Boundaries

- **Inbound:** Business modules call `ChargeCalculationService.calculate()` for fee calculation
- **Outbound:** References product codes, account numbers, customer IDs as plain strings (no JPA joins)

### Package Structure

```
com.banking.charges/
├── domain/
│   ├── entity/
│   │   ├── ChargeDefinition.java
│   │   ├── ChargeRule.java
│   │   ├── ChargeTier.java
│   │   ├── ProductCharge.java
│   │   ├── CustomerChargeOverride.java
│   │   ├── FeeWaiver.java
│   │   ├── InterestRate.java
│   │   ├── InterestTier.java
│   │   ├── InterestAccrual.java
│   │   └── ChargeAuditLog.java
│   └── enums/
│       ├── ChargeType.java
│       ├── CalculationMethod.java
│       ├── WaiverScope.java
│       ├── ChargeStatus.java
│       └── InterestSchedule.java
├── repository/
│   ├── ChargeDefinitionRepository.java
│   ├── ChargeRuleRepository.java
│   ├── ChargeTierRepository.java
│   ├── ProductChargeRepository.java
│   ├── CustomerChargeOverrideRepository.java
│   ├── FeeWaiverRepository.java
│   ├── InterestRateRepository.java
│   ├── InterestTierRepository.java
│   ├── InterestAccrualRepository.java
│   └── ChargeAuditLogRepository.java
├── service/
│   ├── ChargeDefinitionService.java
│   ├── ChargeRuleService.java
│   ├── ChargeAssignmentService.java
│   ├── FeeWaiverService.java
│   ├── InterestRateService.java
│   ├── ChargeCalculationService.java
│   ├── InterestAccrualService.java
│   └── ChargeAuditService.java
├── controller/
│   ├── ChargeDefinitionController.java
│   ├── ChargeRuleController.java
│   ├── ChargeAssignmentController.java
│   ├── FeeWaiverController.java
│   ├── InterestRateController.java
│   ├── ChargeCalculationController.java
│   └── ChargeExceptionHandler.java
├── dto/
│   ├── request/
│   │   ├── CreateChargeDefinitionRequest.java
│   │   ├── UpdateChargeDefinitionRequest.java
│   │   ├── CreateChargeRuleRequest.java
│   │   ├── AssignChargeRequest.java
│   │   ├── CreateFeeWaiverRequest.java
│   │   ├── CreateInterestRateRequest.java
│   │   └── ChargeCalculationRequest.java
│   └── response/
│       ├── ChargeDefinitionResponse.java
│       ├── ChargeRuleResponse.java
│       ├── ProductChargeResponse.java
│       ├── CustomerChargeOverrideResponse.java
│       ├── FeeWaiverResponse.java
│       ├── InterestRateResponse.java
│       ├── ChargeCalculationResponse.java
│       └── InterestAccrualResponse.java
├── mapper/
│   └── ChargeMapper.java
└── exception/
    ├── ChargeNotFoundException.java
    ├── ChargeAlreadyExistsException.java
    ├── InvalidChargeTypeException.java
    ├── WaiverNotFoundException.java
    ├── WaiverAlreadyExistsException.java
    ├── InterestRateNotFoundException.java
    └── InvalidCalculationMethodException.java
```

---

## 2. Entity Design (Data Model)

### Entity Relationship Diagram

```
charge_definitions (1) ──── (N) charge_rules
                        ──── (N) product_charges
                        ──── (N) customer_charge_overrides

charge_rules    (1) ──── (N) charge_tiers
interest_rates  (1) ──── (N) interest_tiers
interest_rates  (1) ──── (N) interest_accruals
fee_waivers     ─── references charge_definition
```

### Entity Definitions

#### ChargeDefinition

```java
@Entity
@Table(name = "charge_definitions")
public class ChargeDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_type", nullable = false)
    private ChargeType chargeType;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargeStatus status = ChargeStatus.ACTIVE;

    @Embedded
    private AuditFields audit;
}
```

#### ChargeRule (calculation configuration)

```java
@Entity
@Table(name = "charge_rules")
public class ChargeRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false)
    private CalculationMethod method;   // FLAT, PERCENTAGE, TIERED_VOLUME

    @Column(precision = 19, scale = 4)
    private BigDecimal flatAmount;

    @Column(precision = 10, scale = 6)
    private BigDecimal percentageRate;

    @Column(name = "min_amount", precision = 19, scale = 4)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 19, scale = 4)
    private BigDecimal maxAmount;

    @Embedded
    private AuditFields audit;
}
```

#### ChargeTier

```java
@Entity
@Table(name = "charge_tiers")
public class ChargeTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_rule_id", nullable = false)
    private ChargeRule chargeRule;

    @Column(name = "tier_from", nullable = false)
    private Long tierFrom;

    @Column(name = "tier_to")
    private Long tierTo;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;
}
```

#### ProductCharge

```java
@Entity
@Table(name = "product_charges", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"charge_definition_id", "product_code"})
})
public class ProductCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;
}
```

#### CustomerChargeOverride

```java
@Entity
@Table(name = "customer_charge_overrides", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"charge_definition_id", "customer_id"})
})
public class CustomerChargeOverride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;
}
```

#### FeeWaiver

```java
@Entity
@Table(name = "fee_waivers")
public class FeeWaiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaiverScope scope;          // CUSTOMER, ACCOUNT, PRODUCT

    @Column(name = "reference_id", nullable = false)
    private String referenceId;         // customerId, accountNumber, or productCode

    @Column(name = "waiver_percentage", nullable = false)
    private Integer waiverPercentage;   // 0-100

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;          // null = permanent

    @Embedded
    private AuditFields audit;
}
```

#### InterestRate

```java
@Entity
@Table(name = "interest_rates")
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "fixed_rate", precision = 10, scale = 6)
    private BigDecimal fixedRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "accrual_schedule", nullable = false)
    private InterestSchedule accrualSchedule;   // DAILY, MONTHLY

    @Enumerated(EnumType.STRING)
    @Column(name = "application_schedule", nullable = false)
    private InterestSchedule applicationSchedule;  // MONTHLY, QUARTERLY

    @Embedded
    private AuditFields audit;
}
```

#### InterestTier

```java
@Entity
@Table(name = "interest_tiers")
public class InterestTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRate interestRate;

    @Column(name = "balance_from", nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceFrom;

    @Column(name = "balance_to", precision = 19, scale = 4)
    private BigDecimal balanceTo;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal rate;
}
```

#### InterestAccrual

```java
@Entity
@Table(name = "interest_accruals", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"interest_rate_id", "account_number", "accrual_date"})
})
public class InterestAccrual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRate interestRate;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "accrual_date", nullable = false)
    private LocalDate accrualDate;

    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal amount;

    @Column(name = "applied", nullable = false)
    private Boolean applied = false;
}
```

### Enums

```java
public enum ChargeType {
    MONTHLY_MAINTENANCE, TRANSACTION_FEE, OVERDRAFT_FEE, INTEREST,
    WIRE_TRANSFER_FEE, STATEMENT_FEE, EARLY_CLOSURE_FEE
}

public enum CalculationMethod {
    FLAT, PERCENTAGE, TIERED_VOLUME
}

public enum WaiverScope {
    CUSTOMER, ACCOUNT, PRODUCT
}

public enum ChargeStatus {
    ACTIVE, INACTIVE
}

public enum InterestSchedule {
    DAILY, MONTHLY, QUARTERLY
}
```

---

## 3. Service Layer Design

### ChargeDefinitionService

```java
@Service
@Transactional
public class ChargeDefinitionService {

    ChargeDefinitionResponse createCharge(CreateChargeDefinitionRequest request);
    ChargeDefinitionResponse updateCharge(Long id, UpdateChargeDefinitionRequest request);
    void activateCharge(Long id);
    void deactivateCharge(Long id);
    ChargeDefinitionResponse getCharge(Long id);
    List<ChargeDefinitionResponse> getAllCharges(ChargeType type, ChargeStatus status);
}
```

### ChargeRuleService

```java
@Service
@Transactional
public class ChargeRuleService {

    ChargeRuleResponse addRule(Long chargeId, CreateChargeRuleRequest request);
    ChargeRuleResponse updateRule(Long ruleId, CreateChargeRuleRequest request);
    void removeRule(Long ruleId);
    List<ChargeRuleResponse> getRules(Long chargeId);
}
```

### ChargeAssignmentService

```java
@Service
@Transactional
public class ChargeAssignmentService {

    // Product level
    ProductChargeResponse assignToProduct(Long chargeId, String productCode, BigDecimal override);
    void unassignFromProduct(Long chargeId, String productCode);
    List<ProductChargeResponse> getProductCharges(String productCode);

    // Customer level
    CustomerChargeOverrideResponse assignToCustomer(Long chargeId, Long customerId, BigDecimal override);
    void unassignFromCustomer(Long chargeId, Long customerId);
    List<CustomerChargeOverrideResponse> getCustomerOverrides(Long customerId);
}
```

### FeeWaiverService

```java
@Service
@Transactional
public class FeeWaiverService {

    FeeWaiverResponse createWaiver(CreateFeeWaiverRequest request);
    void removeWaiver(Long waiverId);
    List<FeeWaiverResponse> getWaivers(WaiverScope scope, String referenceId);
    List<FeeWaiverResponse> getApplicableWaivers(Long chargeId, Long customerId, String accountNumber, LocalDate date);
}
```

### InterestRateService

```java
@Service
@Transactional
public class InterestRateService {

    InterestRateResponse createInterestRate(CreateInterestRateRequest request);
    InterestRateResponse updateInterestRate(Long id, CreateInterestRateRequest request);
    InterestRateResponse getInterestRate(Long id);
    List<InterestRateResponse> getInterestRatesByProduct(String productCode);
}
```

### ChargeCalculationService (primary entry point)

```java
@Service
@Transactional(readOnly = true)
public class ChargeCalculationService {

    /**
     * Calculate fee for a transaction or account operation.
     * Resolution: Customer override → Product charge → default rule.
     * Waivers applied before returning final amount.
     */
    ChargeCalculationResponse calculate(ChargeCalculationRequest request);
}
```

**ChargeCalculationRequest:**
```java
public class ChargeCalculationRequest {
    private String productCode;
    private Long customerId;
    private String accountNumber;
    private ChargeType chargeType;
    private BigDecimal transactionAmount;   // for percentage-based
    private Long transactionCount;          // for tiered volume
    private String currency;
}
```

**ChargeCalculationResponse:**
```java
public class ChargeCalculationResponse {
    private BigDecimal baseAmount;          // before waiver
    private BigDecimal waiverAmount;
    private BigDecimal finalAmount;         // after waiver
    private boolean waiverApplied;
    private String waiverId;
    private String ruleApplied;
}
```

### InterestAccrualService

```java
@Service
@Transactional
public class InterestAccrualService {

    BigDecimal calculateDailyAccrual(String accountNumber, BigDecimal balance, InterestRate rate);
    void accrueDaily(String accountNumber, LocalDate date);  // daily job
    void applyInterest(String accountNumber, LocalDate date);  // periodic job
}
```

**Scheduled jobs:**
```java
@Scheduled(cron = "0 0 1 * * ?")  // daily at 1 AM
public void dailyAccrualJob() {
    // For each account with interest: calculate daily accrual
}

@Scheduled(cron = "0 0 2 1 * ?")  // monthly on 1st at 2 AM
public void monthlyInterestApplicationJob() {
    // For each account: sum accruals, apply to balance, reset counter
}
```

---

## 4. Controller / API Design

### ChargeDefinitionController (`/api/charges/definitions`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/charges/definitions` | Create charge | S1.1, S1.2 |
| GET | `/api/charges/definitions` | List charges | - |
| GET | `/api/charges/definitions/{id}` | Get charge | - |
| PUT | `/api/charges/definitions/{id}` | Update charge | - |
| PUT | `/api/charges/definitions/{id}/activate` | Activate | S1.5 |
| PUT | `/api/charges/definitions/{id}/deactivate` | Deactivate | S1.4 |

### ChargeRuleController (`/api/charges/definitions/{chargeId}/rules`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/.../rules` | Add rule | S2.1, S2.3, S2.7 |
| PUT | `/api/.../rules/{ruleId}` | Update rule | - |
| DELETE | `/api/.../rules/{ruleId}` | Remove rule | - |
| GET | `/api/.../rules` | List rules | - |

### ChargeAssignmentController (`/api/charges/assignments`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/charges/assignments/product` | Assign to product | S3.1 |
| POST | `/api/charges/assignments/customer` | Assign to customer | S4.1 |
| GET | `/api/charges/assignments/product/{code}` | Get product charges | S3.2 |
| GET | `/api/charges/assignments/customer/{id}` | Get customer overrides | - |

### FeeWaiverController (`/api/charges/waivers`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/charges/waivers` | Create waiver | S5.1, S5.2, S5.3, S5.4 |
| DELETE | `/api/charges/waivers/{id}` | Remove waiver | - |
| GET | `/api/charges/waivers?scope=&ref=` | List waivers | - |

### InterestRateController (`/api/charges/interest`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/charges/interest` | Create interest rate | S5a.1 |
| PUT | `/api/charges/interest/{id}` | Update interest rate | - |
| GET | `/api/charges/interest/product/{code}` | Get rates for product | - |

### ChargeCalculationController (`/api/charges/calculate`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/charges/calculate` | Calculate fee | S6.1-S6.4 |

---

## 5. Frontend Design

### Sidebar Entry

"Charges Management" under "System" section.

### Pages

| Page | Route | Description |
|------|-------|-------------|
| ChargeDefinitionListPage | `/charges` | List charge definitions |
| ChargeDefinitionFormPage | `/charges/new` / `/charges/:id/edit` | Create/edit charge |
| ChargeAssignmentPage | `/charges/assignments` | Assign charges to products/customers |
| FeeWaiverPage | `/charges/waivers` | Manage fee waivers |
| InterestRatePage | `/charges/interest` | Configure interest rates |
| ChargeCalculatorPage | `/charges/calculate` | Fee calculator tool |

### Redux Slice

```
store/slices/chargeSlice.ts
```

### Services

```
services/chargeService.ts
```

### Types

```
types/charge.types.ts
```

---

## 6. Database Migration

### V1__create_charges_schema.sql

```sql
-- Charge definitions
CREATE TABLE charge_definitions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    charge_type VARCHAR(30) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_charge_definitions_type ON charge_definitions(charge_type);

-- Charge rules (calculation config)
CREATE TABLE charge_rules (
    id BIGSERIAL PRIMARY KEY,
    charge_definition_id BIGINT NOT NULL REFERENCES charge_definitions(id),
    calculation_method VARCHAR(20) NOT NULL,
    flat_amount NUMERIC(19,4),
    percentage_rate NUMERIC(10,6),
    min_amount NUMERIC(19,4),
    max_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_charge_rules_definition ON charge_rules(charge_definition_id);

-- Charge tiers
CREATE TABLE charge_tiers (
    id BIGSERIAL PRIMARY KEY,
    charge_rule_id BIGINT NOT NULL REFERENCES charge_rules(id) ON DELETE CASCADE,
    tier_from BIGINT NOT NULL,
    tier_to BIGINT,
    rate NUMERIC(19,6) NOT NULL
);

CREATE INDEX idx_charge_tiers_rule ON charge_tiers(charge_rule_id);

-- Product charges
CREATE TABLE product_charges (
    id BIGSERIAL PRIMARY KEY,
    charge_definition_id BIGINT NOT NULL REFERENCES charge_definitions(id),
    product_code VARCHAR(50) NOT NULL,
    override_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (charge_definition_id, product_code)
);

CREATE INDEX idx_product_charges_product ON product_charges(product_code);

-- Customer charge overrides
CREATE TABLE customer_charge_overrides (
    id BIGSERIAL PRIMARY KEY,
    charge_definition_id BIGINT NOT NULL REFERENCES charge_definitions(id),
    customer_id BIGINT NOT NULL,
    override_amount NUMERIC(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (charge_definition_id, customer_id)
);

CREATE INDEX idx_customer_overrides_customer ON customer_charge_overrides(customer_id);

-- Fee waivers
CREATE TABLE fee_waivers (
    id BIGSERIAL PRIMARY KEY,
    charge_definition_id BIGINT NOT NULL REFERENCES charge_definitions(id),
    scope VARCHAR(20) NOT NULL,
    reference_id VARCHAR(50) NOT NULL,
    waiver_percentage INTEGER NOT NULL DEFAULT 100,
    valid_from DATE NOT NULL,
    valid_to DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_fee_waivers_reference ON fee_waivers(scope, reference_id);
CREATE INDEX idx_fee_waivers_definition ON fee_waivers(charge_definition_id);

-- Interest rates
CREATE TABLE interest_rates (
    id BIGSERIAL PRIMARY KEY,
    charge_definition_id BIGINT NOT NULL REFERENCES charge_definitions(id),
    product_code VARCHAR(50) NOT NULL,
    fixed_rate NUMERIC(10,6),
    accrual_schedule VARCHAR(20) NOT NULL DEFAULT 'DAILY',
    application_schedule VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_interest_rates_product ON interest_rates(product_code);

-- Interest tiers (tiered by balance)
CREATE TABLE interest_tiers (
    id BIGSERIAL PRIMARY KEY,
    interest_rate_id BIGINT NOT NULL REFERENCES interest_rates(id) ON DELETE CASCADE,
    balance_from NUMERIC(19,4) NOT NULL,
    balance_to NUMERIC(19,4),
    rate NUMERIC(10,6) NOT NULL
);

CREATE INDEX idx_interest_tiers_rate ON interest_tiers(interest_rate_id);

-- Interest accruals
CREATE TABLE interest_accruals (
    id BIGSERIAL PRIMARY KEY,
    interest_rate_id BIGINT NOT NULL REFERENCES interest_rates(id),
    account_number VARCHAR(30) NOT NULL,
    accrual_date DATE NOT NULL,
    balance NUMERIC(19,4) NOT NULL,
    amount NUMERIC(19,6) NOT NULL,
    applied BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (interest_rate_id, account_number, accrual_date)
);

CREATE INDEX idx_interest_accruals_account ON interest_accruals(account_number);
CREATE INDEX idx_interest_accruals_date ON interest_accruals(accrual_date);
```

---

## 7. Integration with Downstream Modules

### Read Interface

Business modules call `ChargeCalculationService.calculate()` via in-process injection:

```java
// In PaymentService
ChargeCalculationResponse response = chargeCalculationService.calculate(
    new ChargeCalculationRequest()
        .setProductCode("CURRENT")
        .setCustomerId(customerId)
        .setChargeType(ChargeType.WIRE_TRANSFER_FEE)
        .setTransactionAmount(amount)
        .setCurrency("USD")
);
BigDecimal fee = response.getFinalAmount();
```

---

## 8. Error Handling

| Exception | Error Code | HTTP Status | Trigger |
|-----------|-----------|-------------|---------|
| `ChargeNotFoundException` | CHRG-001 | 404 | Charge ID not found |
| `ChargeAlreadyExistsException` | CHRG-002 | 409 | Duplicate charge name |
| `InvalidChargeTypeException` | CHRG-003 | 400 | Invalid charge type |
| `WaiverNotFoundException` | CHRG-004 | 404 | Waiver not found |
| `WaiverAlreadyExistsException` | CHRG-005 | 409 | Duplicate waiver |
| `InterestRateNotFoundException` | CHRG-006 | 404 | Interest rate not found |
| `InvalidCalculationMethodException` | CHRG-007 | 400 | Invalid calculation method |

### Global Exception Handler

```java
@RestControllerAdvice(basePackages = "com.banking.charges")
public class ChargeExceptionHandler {
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
| `ChargeNotFoundException` | CHRG-001 |
| `ChargeAlreadyExistsException` | CHRG-002 |
| `InvalidChargeTypeException` | CHRG-003 |
| `WaiverNotFoundException` | CHRG-004 |
| `WaiverAlreadyExistsException` | CHRG-005 |
| `InterestRateNotFoundException` | CHRG-006 |
| `InvalidCalculationMethodException` | CHRG-007 |

### Module-Specific Unit Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `ChargeDefinitionServiceTest` | CRUD, activate/deactivate | S1.1-S1.5 |
| `ChargeRuleServiceTest` | Flat, percentage, tiered rules | S2.1-S2.10 |
| `ChargeAssignmentServiceTest` | Product/customer assignment | S3.1-S3.2, S4.1-S4.2 |
| `FeeWaiverServiceTest` | Waiver creation, applicability | S5.1-S5.8 |
| `ChargeCalculationServiceTest` | Fee calculation with waivers | S6.1-S6.4 |
| `InterestAccrualServiceTest` | Accrual, application | S5a.5-S5a.10 |

### Module-Specific Integration Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `ChargeDefinitionControllerTest` | Charge CRUD API | S1.1-S1.5 |
| `ChargeCalculationControllerTest` | Calculation API | S6.1-S6.4 |
| `FeeWaiverControllerTest` | Waiver API | S5.1-S5.8 |
| `InterestRateControllerTest` | Interest rate API | S5a.1-S5a.3 |
