# Customer Management Module Specification

**Module:** Customer Management (Foundation)  
**Version:** 1.1  
**Date:** 2026-03-19  
**Status:** Draft  
**Updated:** Added frontend, Core Banking integration, third-party integration

---

## Table of Contents

1. [Module Overview](#1-module-overview)
2. [Domain Model](#2-domain-model)
3. [Module Structure](#3-module-structure)
4. [Key Workflows](#4-key-workflows)
5. [API Design](#5-api-design)
6. [Frontend Components](#6-frontend-components)
7. [Integration Points](#7-integration-points)
8. [Security](#8-security)
9. [Error Handling](#9-error-handling)
10. [Testing Strategy](#10-testing-strategy)
11. [Success Criteria](#11-success-criteria)

---

## 1. Module Overview

### Purpose

Manage corporate, SME, and individual customer lifecycle, KYC/AML verification, and authorization. This module is a **foundation module** that other business modules depend on.

**Customer Types:**
- **Corporate customers** - Large businesses with complex structures
- **SME customers** - Small and medium enterprises
- **Individual customers** - Staff of corporate/SME customers (for payroll services)

### Key Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Customer Structure | Flexible | Supports single entities, hierarchies, and networks for complex corporate structures |
| Individual Customer Scope | Payroll recipients only | Staff receive payroll payments, no other banking services |
| Individual Customer Creation | Bulk upload by company | Company administrator uploads staff list |
| Individual KYC | Standard KYC | Identity verification, no sanctions screening |
| Individual Data | Employment details | Name, national ID, bank account, employer, address, phone, email, tax ID, employment status |
| Employer Relationship | Employment relationship | Separate entity with start date, status, department |
| KYC/AML (Corporate/SME) | Enhanced KYC | Supports due diligence for high-risk customers, periodic reviews, sanctions screening, PEP checks |
| Integration | Asynchronous Processing | Better reliability, allows retries, decouples KYC from onboarding flow |
| Authorization | RBAC with Attributes | Balances simplicity with flexibility for transaction limits and approval hierarchies |

### Module Dependencies

```
Customer Management Module
    ├── Depends On: Master Data (currencies, countries)
    └── Used By: Account, Cash Management, Trade Finance, Payments
```

### Data Owned

| Table | Purpose |
|-------|---------|
| customers | Core customer records (Corporate, SME, Individual) |
| entity_relationships | Customer hierarchy/relationships (Corporate/SME) |
| employment_relationships | Individual customer to employer links |
| authorized_signatories | Individuals authorized to act on behalf of corporate/SME |
| roles | Permission sets |
| permissions | Granular access control |
| kyc_checks | KYC verification records |
| documents | Customer documents |
| risk_assessments | Customer risk scoring |

---

## 2. Domain Model

### Core Entities

```java
// Customer - Represents a business entity (corporate/SME) or individual (staff)
@Entity
class Customer {
    String id;
    CustomerType type; // CORPORATE, SME, INDIVIDUAL
    String legalName; // For corporate/SME: legal name; For individual: full name
    String tradingName; // For corporate/SME only
    BusinessRegistration registration; // For corporate/SME only
    PersonDetails personDetails; // For individual customers only
    RiskRating riskRating; // LOW, MEDIUM, HIGH, CRITICAL
    KycStatus kycStatus; // PENDING, IN_PROGRESS, COMPLETED, EXPIRED
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

// EmploymentRelationship - Links individual customers to their employer
@Entity
class EmploymentRelationship {
    String id;
    Customer individual; // Individual customer (staff)
    Customer employer; // Corporate/SME customer
    EmploymentStatus status; // ACTIVE, INACTIVE, TERMINATED
    String department;
    String employeeId; // Employer's internal employee ID
    LocalDate startDate;
    LocalDate endDate; // Null if currently employed
    String taxId; // Individual's tax identification number
    String bankAccountNumber; // For payroll deposits
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

// EntityRelationship - Links corporate/SME customers in complex structures
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

// PersonDetails - Individual person details
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

// EmploymentStatus - Employment status for individual customers
enum EmploymentStatus {
    ACTIVE,
    INACTIVE,
    TERMINATED,
    ON_LEAVE
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

### KYC/AML Requirements by Customer Type

| Customer Type | KYC Level | Checks Required | Frequency |
|---------------|-----------|-----------------|-----------|
| **Corporate** | Enhanced KYC | Identity, Sanctions, PEP, Adverse Media | Onboarding + Annual review |
| **SME** | Enhanced KYC | Identity, Sanctions, PEP, Adverse Media | Onboarding + Annual review |
| **Individual (Staff)** | Standard KYC | Identity verification | Onboarding only |

**Individual Customer KYC:**
- Identity verification based on employer's KYC status
- No sanctions screening required
- Simplified due diligence process
- Bulk verification during staff upload

---

## 3. Module Structure

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

---

## 4. Key Workflows

### 4.1 Corporate/SME Customer Onboarding

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

### 4.2 Individual Customer Bulk Upload

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Upload     │────▶│   Validate   │────▶│   Create     │────▶│   Verify     │
│   Staff List │     │   Data       │     │   Records    │     │   Identity   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
      │                    │                    │                    │
      ▼                    ▼                    ▼                    ▼
  CSV/Excel File     Validation Rules    Individual Records    Standard KYC
```

**Steps:**
1. Company administrator uploads staff list (CSV/Excel)
2. System validates data format and completeness
3. Individual customer records created with PENDING status
4. Employment relationships created linking staff to employer
5. Standard KYC verification initiated (identity only)
6. Verification results processed
7. Customer status updated based on verification outcome

**Bulk Upload Data Fields:**
- Employee ID (employer's internal ID)
- Full name
- National ID
- Date of birth
- Address
- Phone number
- Email
- Tax ID
- Bank account number
- Department
- Start date

### 4.3 KYC Verification

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

### 4.4 Authorization Management

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

---

## 5. API Design

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
  
  /api/v1/customers/{customerId}/employees:
    post:
      summary: Bulk upload employees for payroll
      requestBody:
        content:
          multipart/form-data:
            schema:
              type: object
              properties:
                file:
                  type: string
                  format: binary
                  description: CSV or Excel file with employee data
      responses:
        '202':
          description: Bulk upload accepted, processing in background
          content:
            application/json:
              schema:
                type: object
                properties:
                  uploadId:
                    type: string
                  status:
                    type: string
                    enum: [PROCESSING, COMPLETED, FAILED]
                  totalRecords:
                    type: integer
                  processedRecords:
                    type: integer
                  failedRecords:
                    type: integer
    get:
      summary: List employees for payroll
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
        - name: status
          in: query
          schema:
            type: string
            enum: [ACTIVE, INACTIVE, TERMINATED]
        - name: department
          in: query
          schema:
            type: string
        - name: page
          in: query
          schema:
            type: integer
            default: 0
        - name: size
          in: query
          schema:
            type: integer
            default: 20
      responses:
        '200':
          description: List of employees
          content:
            application/json:
              schema:
                type: object
                properties:
                  content:
                    type: array
                    items:
                      $ref: '#/components/schemas/Employee'
                  totalElements:
                    type: integer
                  totalPages:
                    type: integer
  
  /api/v1/customers/{customerId}/employees/{employeeId}:
    get:
      summary: Get employee details
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
        - name: employeeId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Employee details
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Employee'
    put:
      summary: Update employee details
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
        - name: employeeId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UpdateEmployeeRequest'
      responses:
        '200':
          description: Employee updated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Employee'
    delete:
      summary: Terminate employee
      parameters:
        - name: customerId
          in: path
          required: true
          schema:
            type: string
        - name: employeeId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Employee terminated

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
    
    Employee:
      type: object
      properties:
        id:
          type: string
        customerId:
          type: string
          description: Employer customer ID
        employeeId:
          type: string
          description: Employer's internal employee ID
        name:
          type: string
        nationalId:
          type: string
        dateOfBirth:
          type: string
          format: date
        email:
          type: string
          format: email
        phoneNumber:
          type: string
        address:
          $ref: '#/components/schemas/Address'
        taxId:
          type: string
        bankAccountNumber:
          type: string
        department:
          type: string
        status:
          type: string
          enum: [ACTIVE, INACTIVE, TERMINATED, ON_LEAVE]
        startDate:
          type: string
          format: date
        endDate:
          type: string
          format: date
        kycStatus:
          type: string
          enum: [PENDING, IN_PROGRESS, COMPLETED, EXPIRED]
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time
    
    UpdateEmployeeRequest:
      type: object
      properties:
        name:
          type: string
        email:
          type: string
          format: email
        phoneNumber:
          type: string
        address:
          $ref: '#/components/schemas/Address'
        bankAccountNumber:
          type: string
        department:
          type: string
        status:
          type: string
          enum: [ACTIVE, INACTIVE, TERMINATED, ON_LEAVE]
        endDate:
          type: string
          format: date
    
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

---

## 6. Frontend Components

### Customer Management UI

The Customer Management module includes the following frontend components built with React 18, TypeScript, and Ant Design:

#### Pages

| Page | Components | Description |
|------|-----------|-------------|
| **Customer List** | Table, Filters, Pagination | List all customers with search and filtering |
| **Customer Detail** | Tabs, Cards, Forms | View and edit customer details |
| **Customer Onboarding** | Stepper, Forms, Upload | Multi-step onboarding wizard |
| **KYC Management** | Status Cards, Documents, Timeline | KYC status and document management |
| **Employee Management** | Table, Upload, Bulk Actions | Manage individual customers (staff) |
| **Signatory Management** | Table, Forms, Authority Matrix | Manage authorized signatories |

#### Key Components

```typescript
// Customer List Component
const CustomerList: React.FC = () => {
  const { data, loading, pagination } = useCustomers();
  
  return (
    <Table
      dataSource={data}
      loading={loading}
      pagination={pagination}
      columns={[
        { title: 'Legal Name', dataIndex: 'legalName' },
        { title: 'Type', dataIndex: 'type' },
        { title: 'KYC Status', dataIndex: 'kycStatus', render: (status) => <StatusBadge status={status} /> },
        { title: 'Risk Rating', dataIndex: 'riskRating', render: (rating) => <RiskBadge rating={rating} /> },
      ]}
    />
  );
};

// Employee Bulk Upload Component
const EmployeeBulkUpload: React.FC<{ customerId: string }> = ({ customerId }) => {
  const { upload, loading, progress } = useEmployeeUpload(customerId);
  
  return (
    <Upload
      accept=".csv,.xlsx"
      beforeUpload={(file) => {
        upload(file);
        return false;
      }}
    >
      <Button icon={<UploadOutlined />} loading={loading}>
        Upload Employee List
      </Button>
    </Upload>
  );
};
```

#### State Management

Customer management uses Redux Toolkit for state management:

```typescript
// Customer Slice
const customerSlice = createSlice({
  name: 'customer',
  initialState: {
    customers: [],
    selectedCustomer: null,
    loading: false,
    error: null,
  },
  reducers: {
    setCustomers: (state, action) => {
      state.customers = action.payload;
    },
    setSelectedCustomer: (state, action) => {
      state.selectedCustomer = action.payload;
    },
  },
});

// Employee Slice
const employeeSlice = createSlice({
  name: 'employee',
  initialState: {
    employees: [],
    uploadProgress: null,
    loading: false,
  },
  reducers: {
    setEmployees: (state, action) => {
      state.employees = action.payload;
    },
    setUploadProgress: (state, action) => {
      state.uploadProgress = action.payload;
    },
  },
});
```

---

## 7. Integration Points

### Core Banking System Integration

The Customer Management module integrates with the bank's Core Banking System (CBS):

| Integration Point | Direction | Sync Type | Description |
|-------------------|-----------|-----------|-------------|
| **Customer Master** | Digital → CBS | Push | Customer records synced to CBS |
| **Account Linkage** | CBS → Digital | Pull | Accounts linked to customers |
| **KYC Status** | Digital → CBS | Push | KYC status updates sent to CBS |
| **Transaction History** | CBS → Digital | Pull | Customer transaction history |

**Data Flow:**
```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ Digital Banking  │────▶│ Integration      │────▶│ Core Banking     │
│ Platform         │     │ Gateway          │     │ System           │
│ (Customer Mgmt)  │     │ (API/SFTP)       │     │ (Customer Master)│
└──────────────────┘     └──────────────────┘     └──────────────────┘
```

**Sync Rules:**
- Customer created → Push to CBS (async, via message queue)
- Customer updated → Push to CBS (async, via message queue)
- Account created in CBS → Pull to Digital (real-time API)
- KYC completed → Push to CBS (async, via message queue)

### Third-Party Service Integration

| Service | Purpose | Provider | Protocol | Integration |
|---------|---------|----------|----------|-------------|
| **Sanctions Screening** | AML compliance | Dow Jones, Refinitiv | REST API | Async via Kafka |
| **PEP Screening** | AML compliance | Dow Jones, Refinitiv | REST API | Async via Kafka |
| **Identity Verification** | KYC | Jumio, Onfido, Trulioo | REST API | Async via Kafka |
| **Credit Scoring** | Risk assessment | Dun & Bradstreet | REST API | Async via Kafka |
| **Document Verification** | KYC | Onfido, Veriff | REST API | Async via Kafka |
| **Address Validation** | Data quality | Loqate | REST API | Synchronous |

**Integration Pattern:**
```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ Customer         │────▶│ Message Queue    │────▶│ Third-Party      │
│ Management       │     │ (Kafka)          │     │ Service          │
│ Service          │     │                  │     │ (KYC Provider)   │
└──────────────────┘     └──────────────────┘     └──────────────────┘
        │                        │                        │
        │                        ▼                        │
        │                 ┌──────────────────┐           │
        └────────────────▶│ Result Handler   │◀──────────┘
                          │ (Process Results)│
                          └──────────────────┘
```

**Resilience Patterns:**
- Circuit breaker for each third-party service
- Retry with exponential backoff (max 3 retries)
- Timeout configuration (connect: 5s, read: 30s)
- Fallback responses for degraded functionality
- Dead letter queue for failed messages

### Event Publishing

The Customer Management module publishes events for other modules:

| Event | Trigger | Consumers |
|-------|---------|-----------|
| **CustomerCreated** | New customer created | Account, Payments, Cash Management |
| **CustomerUpdated** | Customer details updated | Account, Payments |
| **KycCompleted** | KYC verification completed | Account, Payments |
| **SignatoryAdded** | New signatory added | Account, Payments |
| **EmployeeAdded** | New employee added | Cash Management (Payroll) |
| **EmployeeUpdated** | Employee details updated | Cash Management (Payroll) |

**Event Schema:**
```json
{
  "eventId": "uuid",
  "eventType": "CustomerCreated",
  "timestamp": "2026-03-19T10:30:00Z",
  "source": "customer-management",
  "data": {
    "customerId": "uuid",
    "customerType": "CORPORATE",
    "kycStatus": "PENDING"
  }
}
```

---

## 8. Security

### Data Protection

| Data Type | Protection | Storage |
|-----------|-----------|---------|
| **Tax IDs** | Field-level encryption (AES-256-GCM) | Encrypted at rest |
| **National IDs** | Field-level encryption | Encrypted at rest |
| **Personal Names** | Standard encryption | Encrypted at rest |
| **Documents** | S3 server-side encryption | Encrypted at rest |

### Access Control

- **RBAC with Attributes**: Predefined roles (ACCOUNT_OPERATOR, TREASURY_MANAGER, SIGNATORY, VIEWER)
- **Signing Authority**: SOLE, JOINT_2_OF_3, JOINT_3_OF_5, UNLIMITED
- **Multi-factor Authentication**: Required for sensitive operations

### Audit Trail

All customer data changes generate immutable audit records:

```json
{
  "auditId": "uuid",
  "timestamp": "2026-03-19T10:30:00Z",
  "actor": "user-id",
  "action": "CREATE",
  "resource": "customer",
  "resourceId": "customer-uuid",
  "before": null,
  "after": { "legalName": "Acme Corp" },
  "ipAddress": "192.168.1.1"
}
```

---

## 9. Error Handling

### Business Errors

```java
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

---

## 10. Testing Strategy

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

---

## 11. Success Criteria

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Customer Onboarding Time** | < 15 minutes | Transaction logs |
| **KYC Verification Time** | < 24 hours | System logs |
| **API Response Time (p95)** | < 200ms | Monitoring |
| **Uptime** | 99.9% | Monitoring |
| **Test Coverage** | > 80% | Code coverage |

---

## Related Documents

- [System Design](docs/superpowers/architecture/system-design.md))
- [Master Implementation Plan](../2026-03-19-master-implementation-plan.md)
- [Account Management Spec](./account-management-spec.md)
- [Cash Management Spec](./cash-management-spec.md)
- [Trade Finance Spec](./trade-finance-spec.md)
- [Payments Spec](./payments-spec.md)
- [Product Configuration Spec](./product-configuration-spec.md)
- [Limits Management Spec](./limits-management-spec.md)
- [Charges Management Spec](./charges-management-spec.md)
- [Master Data Spec](./master-data-spec.md)
