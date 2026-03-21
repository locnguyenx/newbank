# Corporate & SME Banking System Design

**Date:** 2026-03-19  
**Status:** Draft  
**Scope:** System Architecture + Customer Management Foundation Module

## Table of Contents

1. [Overview](#1-overview)
2. [Domain Model](#2-domain-model)
3. [Architecture Overview](#3-architecture-overview)
4. [Module Structure](#4-module-structure-customer-management---foundation-module)
5. [Key Workflows](#5-key-workflows)
6. [API Design](#6-api-design)
7. [Security Considerations](#7-security-considerations)
8. [Error Handling](#8-error-handling)
9. [Testing Strategy](#9-testing-strategy)
10. [Success Criteria](#10-success-criteria)
11. [Next Steps](#11-next-steps)

## 1. Overview

This document outlines the system architecture for a corporate and SME banking platform, with detailed specification of the Customer Management foundation module. The system uses a **Modular Monolith** architecture with clear separation between foundation modules and business modules.

### Key Decisions Made

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Architecture | Modular Monolith | Simpler deployment, easier data consistency, single deployable unit with clear module boundaries |
| Technology | Java 17 + Spring Boot | Industry standard for banking, excellent transaction management, strong security libraries |
| KYC/AML | Enhanced KYC | Supports due diligence for high-risk customers, periodic reviews, sanctions screening, PEP checks |
| Integration | Asynchronous Processing | Better reliability, allows retries, decouples KYC from onboarding flow |
| Authorization | RBAC with Attributes | Balances simplicity with flexibility for transaction limits and approval hierarchies |
| Customer Structure | Flexible | Supports single entities, hierarchies, and networks for complex corporate structures |

## 2. Domain Model

### Core Entities

```java
// Customer - Represents a business entity (corporate or SME)
@Entity
class Customer {
    String id;
    CustomerType type; // CORPORATE, SME, SOLE_PROPRIETOR
    String legalName;
    String tradingName;
    BusinessRegistration registration;
    RiskRating riskRating; // LOW, MEDIUM, HIGH, CRITICAL
    KycStatus kycStatus; // PENDING, IN_PROGRESS, COMPLETED, EXPIRED
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

// EntityRelationship - Links customers in complex structures
@Entity
class EntityRelationship {
    String id;
    Customer parent;
    Customer child;
    RelationshipType type; // SUBSIDIARY, JOINT_VENTURE, AFFILIATE, PARENT
    BigDecimal ownershipPercentage;
    LocalDate effectiveFrom;
    LocalDate effectiveTo;
}

// AuthorizedSignatory - Individuals authorized to act on behalf of customer
@Entity
class AuthorizedSignatory {
    String id;
    Customer customer;
    PersonDetails person;
    List<Role> roles;
    SigningAuthority authority; // SOLE, JOINT_2_OF_3, etc.
    List<Permission> permissions;
    LocalDate validFrom;
    LocalDate validTo;
}

// Role - Defines a set of permissions
@Entity
class Role {
    String id;
    String name; // ACCOUNT_OPERATOR, TREASURY_MANAGER, SIGNATORY, VIEWER
    String description;
    Set<Permission> permissions;
}

// Permission - Granular access control
@Entity
class Permission {
    String id;
    String resource; // ACCOUNT, TRANSACTION, PAYMENT, REPORT
    String action; // VIEW, CREATE, APPROVE, DELETE
    Map<String, Object> attributes; // Transaction limits, approval thresholds, etc.
}

// BusinessRegistration - Business registration details
@Embeddable
class BusinessRegistration {
    String registrationNumber;
    String jurisdiction;
    BusinessType type; // LLC, CORPORATION, PARTNERSHIP, SOLE_PROPRIETORSHIP
    LocalDate dateOfIncorporation;
    String taxId;
    String vatNumber; // Optional
}

// PersonDetails - Individual person details for authorized signatories
@Embeddable
class PersonDetails {
    String firstName;
    String lastName;
    String nationalId; // Government-issued ID
    LocalDate dateOfBirth;
    String nationality;
    String email;
    String phoneNumber;
    Address address;
}

// Address - Physical address
@Embeddable
class Address {
    String street;
    String city;
    String state;
    String postalCode;
    String country;
    String countryCode; // ISO 3166-1 alpha-2
}
```

### KYC/AML Entities

```java
// KycCheck - Tracks KYC verification status
@Entity
class KycCheck {
    String id;
    Customer customer;
    CheckType type; // IDENTITY, SANCTIONS, PEP, ADVERSE_MEDIA
    CheckStatus status; // PENDING, PASSED, FAILED, REQUIRES_REVIEW
    LocalDateTime initiatedAt;
    LocalDateTime completedAt;
    String externalReferenceId;
    Map<String, Object> result;
}

// Document - Customer documents for KYC
@Entity
class Document {
    String id;
    Customer customer;
    DocumentType type; // BUSINESS_LICENSE, TAX_ID, FINANCIAL_STATEMENT, PROOF_OF_ADDRESS
    String fileName;
    String storagePath;
    DocumentStatus status; // UPLOADED, VERIFIED, REJECTED
    LocalDateTime uploadedAt;
    LocalDateTime verifiedAt;
}

// RiskAssessment - Customer risk scoring
@Entity
class RiskAssessment {
    String id;
    Customer customer;
    RiskRating overallRating;
    Map<String, Integer> factorScores; // geography, industry, transaction_volume, etc.
    LocalDateTime assessedAt;
    LocalDateTime nextReviewDue;
}
```

## 3. Architecture Overview

### Modular Monolith with Foundation and Business Modules

The system uses a **Modular Monolith** architecture with two distinct module layers:

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Modular Monolith                              │
├─────────────────────────────────────────────────────────────────────┤
│  Business Modules (depend on foundation modules)                    │
│  ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐   │
│  │ Cash Management  │ │ Trade Finance    │ │ Payments         │   │
│  │ Module           │ │ Module           │ │ Module           │   │
│  └──────────────────┘ └──────────────────┘ └──────────────────┘   │
├─────────────────────────────────────────────────────────────────────┤
│  Foundation Modules (no dependencies on business modules)           │
│  ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐   │
│  │ Customer         │ │ Account          │ │ Product          │   │
│  │ Management       │ │ Management       │ │ Configuration    │   │
│  └──────────────────┘ └──────────────────┘ └──────────────────┘   │
│  ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐   │
│  │ Limits           │ │ Charges          │ │ Master Data      │   │
│  │ Management       │ │ Management       │ │ Management       │   │
│  └──────────────────┘ └──────────────────┘ └──────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### Module Dependency Rules

**Strict dependency direction:**
```
Foundation Modules → Business Modules (NEVER reverse)
```

**Why this matters:**
- Foundation modules are stable and rarely change
- Business modules can evolve independently
- No circular dependencies
- Clear ownership boundaries

### Module Communication

Modules communicate through **well-defined interfaces** (Java interfaces):

```java
// Foundation module exposes interface
public interface CustomerService {
    Customer getCustomer(String customerId);
    Customer createCustomer(CreateCustomerRequest request);
    Customer updateCustomer(String customerId, UpdateCustomerRequest request);
}

// Business module uses foundation interface
@Service
public class CashManagementService {
    private final CustomerService customerService;
    private final AccountService accountService;
    
    public Payroll createPayroll(String customerId, PayrollRequest request) {
        // Use foundation services
        Customer customer = customerService.getCustomer(customerId);
        Account account = accountService.getAccount(request.getAccountId());
        
        // Business logic
        return payrollRepository.save(payroll);
    }
}
```

**Benefits:**
- **ACID transactions** within single process
- **No network latency** between modules
- **Simple debugging** (all code in one process)
- **Easy cross-module queries** when needed
- **Shared database** for referential integrity

## 4. Module Structure (Customer Management - Foundation Module)

This spec focuses on the **Customer Management** foundation module. Other foundation modules (Account, Product, Limits, Charges, Master Data) will be specified separately.

```
src/main/java/com/bank/customer/
├── CustomerManagementModule.java          # Module entry point
├── api/                                   # External API interfaces
│   ├── CustomerApi.java
│   ├── KycApi.java
│   └── AuthorizationApi.java
├── application/                           # Application services (use cases)
│   ├── CreateCustomerService.java
│   ├── OnboardCustomerService.java
│   ├── KycVerificationService.java
│   └── AuthorizationService.java
├── domain/                                # Domain model and business logic
│   ├── model/
│   │   ├── Customer.java
│   │   ├── EntityRelationship.java
│   │   ├── AuthorizedSignatory.java
│   │   ├── Role.java
│   │   └── Permission.java
│   ├── kyc/
│   │   ├── KycCheck.java
│   │   ├── Document.java
│   │   ├── RiskAssessment.java
│   │   └── RiskScoringEngine.java
│   └── repository/                        # Repository interfaces
│       ├── CustomerRepository.java
│       └── KycRepository.java
├── infrastructure/                        # External integrations
│   ├── persistence/
│   │   ├── CustomerJpaRepository.java
│   │   └── KycJpaRepository.java
│   ├── messaging/
│   │   ├── KycEventPublisher.java
│   │   └── CustomerEventListener.java
│   └── external/
│       ├── SanctionsScreeningClient.java
│       └── PepCheckClient.java
├── config/                                # Module configuration
│   ├── CustomerModuleConfig.java
│   └── SecurityConfig.java
└── shared/                                # Shared interfaces for other modules
    ├── CustomerService.java               # Interface for business modules
    ├── CustomerDto.java                   # Data transfer object
    └── CustomerEvent.java                 # Events for other modules
```

### Other Foundation Modules (To Be Specified)

| Module | Purpose | Key Entities |
|--------|---------|--------------|
| **Account Management** | Business account lifecycle, balances | Account, SubAccount, Balance, Statement |
| **Product Configuration** | Banking products, pricing rules | Product, ProductCategory, PricingRule |
| **Limits Management** | Transaction limits, approval thresholds | Limit, LimitRule, ApprovalThreshold |
| **Charges Management** | Fees, commissions, interest | Charge, ChargeRule, InterestRate |
| **Master Data Management** | Reference data, currencies, countries | Currency, Country, Industry, etc. |

### Business Modules (To Be Specified)

| Module | Purpose | Dependencies |
|--------|---------|--------------|
| **Cash Management** | Payroll, receivables, liquidity | Customer, Account, Limits, Charges |
| **Trade Finance** | Letters of credit, guarantees | Customer, Account, Product, Limits |
| **Payments** | Domestic/international transfers | Customer, Account, Limits, Charges |

### Module Interface Example

```java
// Customer Management module exposes this interface
public interface CustomerService {
    
    // Core customer operations
    Customer getCustomer(String customerId);
    List<Customer> getCustomers(CustomerFilter filter);
    Customer createCustomer(CreateCustomerRequest request);
    Customer updateCustomer(String customerId, UpdateCustomerRequest request);
    
    // KYC operations
    KycStatus getKycStatus(String customerId);
    void initiateKycVerification(String customerId);
    
    // Authorization operations
    List<AuthorizedSignatory> getSignatories(String customerId);
    AuthorizedSignatory addSignatory(String customerId, AddSignatoryRequest request);
    
    // Relationship operations
    List<EntityRelationship> getRelationships(String customerId);
    void addRelationship(String customerId, EntityRelationshipRequest request);
}
```

### Data Ownership

Each module owns its data and exposes it through interfaces:

```
Customer Module owns:
├── customers table
├── entity_relationships table
├── authorized_signatories table
├── kyc_checks table
├── documents table
└── risk_assessments table

Other modules access customer data ONLY through:
└── CustomerService interface (NOT direct database access)
```

## 5. Key Workflows

### 5.1 Customer Onboarding

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐     ┌──────────────┐
│   Submit    │────▶│   Validate   │────▶│   Create    │────▶│  Initiate    │
│ Application │     │   Business   │     │   Customer  │     │   KYC Check  │
└─────────────┘     └──────────────┘     └─────────────┘     └──────────────┘
      │                    │                    │                    │
      ▼                    ▼                    ▼                    ▼
  Form Data         Registration Data     Customer Record      Async KYC Job
```

**Steps:**
1. Customer submits application with business details
2. System validates registration data (format, completeness)
3. Customer record created with PENDING status
4. KYC checks initiated asynchronously (sanctions, PEP, identity)
5. Customer notified of pending verification
6. KYC results processed (may take minutes to hours)
7. Customer status updated based on KYC outcome

### 5.2 KYC Verification

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   KYC Job    │────▶│   Sanctions  │────▶│    PEP       │────▶│   Risk       │
│   Queued     │     │   Screening  │     │    Check     │     │   Scoring    │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
      │                    │                    │                    │
      ▼                    ▼                    ▼                    ▼
  Message Queue       External API         External API        Scoring Engine
```

**Steps:**
1. KYC job placed in message queue
2. Sanctions screening against global watchlists
3. PEP (Politically Exposed Persons) check
4. Adverse media screening (optional)
5. Risk scoring based on collected data
6. Results stored and customer notified
7. If high-risk: manual review queue
8. If passed: customer activated

### 5.3 Authorization Management

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Add        │────▶│   Validate   │────▶│   Assign     │
│   Signatory  │     │   Authority  │     │   Permissions│
└──────────────┘     └──────────────┘     └──────────────┘
      │                    │                    │
      ▼                    ▼                    ▼
  Signatory Data      Signing Rules      Role Assignment
```

**Steps:**
1. Customer administrator adds authorized signatory
2. System validates signing authority rules
3. Roles and permissions assigned
4. Signing limits configured (per transaction, daily, monthly)
5. Signatory activated after verification
6. Audit log entry created

## 6. API Design

### Customer API

```yaml
openapi: 3.0.0
info:
  title: Customer Management API
  version: 1.0.0

paths:
  /api/v1/customers:
    post:
      summary: Create new customer
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateCustomerRequest'
      responses:
        '201':
          description: Customer created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Customer'
  
  /api/v1/customers/{customerId}:
    get:
      summary: Get customer by ID
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Customer details
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Customer'
    put:
      summary: Update customer
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UpdateCustomerRequest'
      responses:
        '200':
          description: Customer updated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Customer'
  
  /api/v1/customers/{customerId}/kyc:
    post:
      summary: Initiate KYC verification
      responses:
        '202':
          description: KYC check initiated
  
  /api/v1/customers/{customerId}/signatories:
    post:
      summary: Add authorized signatory
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AddSignatoryRequest'
      responses:
        '201':
          description: Signatory added

components:
  schemas:
    CreateCustomerRequest:
      type: object
      required:
        - legalName
        - type
        - registration
      properties:
        legalName:
          type: string
          description: Legal registered name of the business
        tradingName:
          type: string
          description: Trading or brand name (if different from legal name)
        type:
          type: string
          enum: [CORPORATE, SME, SOLE_PROPRIETOR]
        registration:
          $ref: '#/components/schemas/BusinessRegistration'
        contactEmail:
          type: string
          format: email
        contactPhone:
          type: string
    
    UpdateCustomerRequest:
      type: object
      properties:
        legalName:
          type: string
          description: Legal registered name of the business
        tradingName:
          type: string
          description: Trading or brand name (if different from legal name)
        contactEmail:
          type: string
          format: email
        contactPhone:
          type: string
        registration:
          $ref: '#/components/schemas/BusinessRegistration'
    
    BusinessRegistration:
      type: object
      required:
        - registrationNumber
        - jurisdiction
        - type
        - dateOfIncorporation
      properties:
        registrationNumber:
          type: string
          description: Official business registration number
        jurisdiction:
          type: string
          description: Country or state of registration
        type:
          type: string
          enum: [LLC, CORPORATION, PARTNERSHIP, SOLE_PROPRIETORSHIP]
        dateOfIncorporation:
          type: string
          format: date
        taxId:
          type: string
          description: Tax identification number
        vatNumber:
          type: string
          description: VAT registration number (optional)
    
    Customer:
      type: object
      properties:
        id:
          type: string
        legalName:
          type: string
        tradingName:
          type: string
        type:
          type: string
          enum: [CORPORATE, SME, SOLE_PROPRIETOR]
        registration:
          $ref: '#/components/schemas/BusinessRegistration'
        kycStatus:
          type: string
          enum: [PENDING, IN_PROGRESS, COMPLETED, EXPIRED]
        riskRating:
          type: string
          enum: [LOW, MEDIUM, HIGH, CRITICAL]
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time
    
    AddSignatoryRequest:
      type: object
      required:
        - person
        - roles
        - authority
      properties:
        person:
          $ref: '#/components/schemas/PersonDetails'
        roles:
          type: array
          items:
            type: string
          description: List of role IDs to assign
        authority:
          type: string
          enum: [SOLE, JOINT_2_OF_3, JOINT_3_OF_5, UNLIMITED]
          description: Signing authority level
        validFrom:
          type: string
          format: date
        validTo:
          type: string
          format: date
          description: Optional expiry date
    
    PersonDetails:
      type: object
      required:
        - firstName
        - lastName
        - nationalId
        - dateOfBirth
      properties:
        firstName:
          type: string
        lastName:
          type: string
        nationalId:
          type: string
          description: Government-issued identification number
        dateOfBirth:
          type: string
          format: date
        nationality:
          type: string
        email:
          type: string
          format: email
        phoneNumber:
          type: string
        address:
          $ref: '#/components/schemas/Address'
    
    Address:
      type: object
      required:
        - street
        - city
        - country
      properties:
        street:
          type: string
        city:
          type: string
        state:
          type: string
        postalCode:
          type: string
        country:
          type: string
        countryCode:
          type: string
          pattern: '^[A-Z]{2}$'
          description: ISO 3166-1 alpha-2 country code
```

## 7. Security Considerations

### Authentication & Authorization
- OAuth 2.0 + JWT for API authentication
- Multi-factor authentication for sensitive operations
- Session management with secure tokens
- API rate limiting per customer

### Data Protection
- Encryption at rest (AES-256) for customer PII
- Field-level encryption for sensitive data (tax IDs, account numbers)
- Audit logging for all customer data access
- Data masking in non-production environments

### Compliance
- KYC/AML workflow must be auditable
- Document retention per regulatory requirements
- Right to data export (GDPR)
- Consent management for data processing

## 8. Error Handling

### Business Errors
```java
// Domain-specific exceptions
class CustomerNotFoundException extends BusinessException {
    public CustomerNotFoundException(String customerId) {
        super("Customer not found: " + customerId, "CUSTOMER_NOT_FOUND", 404);
    }
}

class KycCheckFailedException extends BusinessException {
    public KycCheckFailedException(String checkId, String reason) {
        super("KYC check failed: " + reason, "KYC_CHECK_FAILED", 400);
    }
}

class InsufficientSigningAuthorityException extends BusinessException {
    public InsufficientSigningAuthorityException(String action) {
        super("Insufficient signing authority for: " + action, "INSUFFICIENT_AUTHORITY", 403);
    }
}
```

### Error Response Format
```json
{
  "error": {
    "code": "KYC_CHECK_FAILED",
    "message": "Sanctions screening returned positive match",
    "details": {
      "checkId": "chk_123",
      "matchType": "PARTIAL",
      "confidence": 0.85
    }
  }
}
```

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
| `CustomerNotFoundException` | CUST-001 |
| `DuplicateCustomerException` | CUST-002 |
| `InvalidKYCStateException` | CUST-003 |
| `KYCNotFoundException` | CUST-004 |
| `AuthorizationNotFoundException` | CUST-005 |

### Module-Specific Tests

#### Unit Tests
- Domain model validation
- Business rule enforcement
- Risk scoring logic
- Permission checking

#### Integration Tests
- Database persistence
- Message queue publishing
- External API mocking
- Transaction management

#### End-to-End Tests
- Customer onboarding workflow
- KYC verification process
- Signatory management
- Authorization flows

## 10. Success Criteria

- Customer onboarding completed in < 15 minutes
- KYC verification processed within 24 hours
- 99.9% uptime for customer-facing APIs
- Zero data breaches or compliance violations
- API response time < 200ms for 95% of requests

## 11. Next Steps

1. Implement core domain model
2. Set up database schema and migrations
3. Implement Customer API endpoints
4. Integrate KYC external services
5. Build authorization management
6. Comprehensive testing
7. Performance optimization
8. Security audit