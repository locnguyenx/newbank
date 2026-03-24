# Cash Management Services Module Design

**Created:** 2026-03-24  
**Status:** Draft  
**Module:** Cash Management Services  

**Traceability:**
- Charges Management Design: `docs/superpowers/specs/charges/design.md`
- Account Management Design: `docs/superpowers/specs/account/design.md`
- Customer Management Design: `docs/superpowers/specs/customer/design.md`

## Overview

The Cash Management Services module provides comprehensive cash flow management tools for corporate treasury functions, enabling business customers to efficiently manage their liquidity, payments, and receivables. This module implements the business requirements defined in the BRD and BDD specifications, following the modular monolith architecture principles outlined in AGENTS.md and the System Design document (`docs/superpowers/architecture/system-design.md`). 

This design has been validated for compliance with:
- Modular Monolith architecture with proper module boundaries
- Dependency flow direction (Business → Foundation modules only)
- API contract enforcement using OpenAPI 3.0 as single source of truth
- Module interface restrictions (only depending on .api and .dto packages)

## Architecture

### Module Boundaries
- **Domain:** Core cash management entities and business logic (payroll, receivables, liquidity, batch processing, auto-collection)
- **Application Services:** Cash management operations, payment processing, collection services, liquidity optimization
- **Infrastructure:** Data persistence (JPA repositories), external integrations (payment networks, file processing)
- **Interface:** RESTful API for cash management operations (exposed via `.api` package)

**Compliance with Modular Monolith Principles:**
- Only depends on `.api` and `.dto` packages of other modules (Customer, Account, Limits, Charges, Master Data)
- Does not access internal repositories or services directly (`*.repository`, `*.service` internal packages)
- Follows dependency direction: Business module (Cash Management) depends on foundation modules, not vice versa
- Implements API-first design with OpenAPI contract enforcement as specified in AGENTS.md

### Key Components

#### 1. Payroll Processing Subsystem
- Payroll batch processing engine
- File format parsers (CSV, XML, ISO 20022)
- Payment file generators (ACH, SEPA, local clearing)
- Payroll validation and exception handling
- Employee banking details management

#### 2. Receivables Management Subsystem
- Invoice presentment and presentment engines
- Payment matching and reconciliation algorithms
- Dunning and collection workflow management
- Aging and forecasting analytics
- Partial payment and write-off handling

#### 3. Liquidity Optimization Subsystem
- Real-time cash position aggregation engine
- Cash pooling and sweeping algorithms
- Notional pool interest optimization
- Liquidity forecasting models
- Automated transfer rule engine
- Multi-currency cash management with FX integration

#### 4. Batch Payment Processing Subsystem
- High-volume batch processing engine (10,000+ transactions)
- Payment format handlers (ISO 20022, NACHA, local formats)
- Batch validation and error handling
- Payment file generation and transmission
- Batch approval workflow integration
- Payment cancellation and modification handling

#### 5. Auto-Collection Subsystem
- Configurable collection rule engine
- Automated collection scheduling and execution
- Pre-notification and customer communication
- Collection retry and failure handling
- Collection result reporting and accounting

#### 6. Reporting and Analytics Subsystem
- Real-time dashboard data aggregation
- Standard treasury report generation
- Custom report builder and scheduler
- Audit trail reporting for compliance
- Data export in multiple formats (PDF, Excel, CSV)

## Data Model

### Core Entities

```
PayrollBatch (entity)
├── id (PK, BIGINT auto-increment)
├── batchReference (UNIQUE, VARCHAR)
├── customerId (FK to Customer)
├── status (PayrollBatchStatus enum)
├── fileFormat (PayrollFileFormat enum)
├── recordCount (INTEGER)
├── processedCount (INTEGER)
├── errorCount (INTEGER)
├── totalAmount (BigDecimal)
├── currency (Currency enum)
├── paymentDate (Date)
├── createdAt (Timestamp)
├── createdBy (VARCHAR)
├── updatedAt (Timestamp)
└── updatedBy (VARCHAR)

PayrollRecord (entity)
├── id (PK, BIGINT auto-increment)
├── payrollBatchId (FK to PayrollBatch)
├── employeeIdentifier (VARCHAR)
├── employeeName (VARCHAR)
├── bankAccountNumber (VARCHAR)
├── bankCode (VARCHAR)
├── amount (BigDecimal)
├── currency (Currency enum)
├── status (PayrollRecordStatus enum)
├── errorCode (VARCHAR, nullable)
├── errorDescription (VARCHAR, nullable)
└── auditFields

ReceivableInvoice (entity)
├── id (PK, BIGINT auto-increment)
├── invoiceNumber (UNIQUE, VARCHAR)
├── customerId (FK to Customer)
├── billToCustomerId (FK to Customer, nullable)
├── amount (BigDecimal)
├── currency (Currency enum)
├── issueDate (Date)
├── dueDate (Date)
├── status (InvoiceStatus enum)
├── balanceDue (BigDecimal)
├── currency (Currency enum)
├── referenceNumber (VARCHAR, nullable)
├── description (VARCHAR, nullable)
└── auditFields

ReceivablePayment (entity)
├── id (PK, BIGINT auto-increment)
├── invoiceId (FK to ReceivableInvoice)
├── paymentReference (UNIQUE, VARCHAR)
├── amount (BigDecimal)
├── currency (Currency enum)
├── paymentDate (Date)
├── paymentMethod (PaymentMethod enum)
├── bankDetails (VARCHAR, nullable)
├── status (PaymentStatus enum)
└── auditFields

LiquidityPosition (entity)
├── id (PK, BIGINT auto-increment)
├── customerId (FK to Customer)
├── calculationDateTime (Timestamp)
├── totalPosition (BigDecimal)
├── currency (Currency enum)
├── accountBreakdown (JSON)  // Detailed breakdown by account
├── availableLiquidity (BigDecimal)
├── projectedLiquidity (JSON)  // Forecast data
└── auditFields

CashPoolingTransaction (entity)
├── id (PK, BIGINT auto-increment)
├── customerId (FK to Customer)
├── poolReference (UNIQUE, VARCHAR)
├── transactionDate (Timestamp)
├── fromAccountId (FK to Account)
├── toAccountId (FK to Account)
├── amount (BigDecimal)
├── currency (Currency enum)
├── poolingType (CashPoolingType enum)
├── status (CashPoolingStatus enum)
└── auditFields

BatchPayment (entity)
├── id (PK, BIGINT auto-increment)
├── batchReference (UNIQUE, VARCHAR)
├── customerId (FK to Customer)
├── status (BatchPaymentStatus enum)
├── fileFormat (BatchFileFormat enum)
├── instructionCount (INTEGER)
├── processedCount (INTEGER)
├── errorCount (INTEGER)
├── totalAmount (BigDecimal)
├── currency (Currency enum)
├── paymentDate (Date)
├── createdAt (Timestamp)
├── createdBy (VARCHAR)
├── updatedAt (Timestamp)
└── updatedBy (VARCHAR)

BatchPaymentInstruction (entity)
├── id (PK, BIGINT auto-increment)
├── batchPaymentId (FK to BatchPayment)
├── instructionReference (VARCHAR)
├── beneficiaryName (VARCHAR)
├── beneficiaryAccount (VARCHAR)
├── beneficiaryBankCode (VARCHAR)
├── amount (BigDecimal)
├── currency (Currency enum)
├── paymentType (PaymentType enum)
├── status (PaymentInstructionStatus enum)
├── errorCode (VARCHAR, nullable)
├── errorDescription (VARCHAR, nullable)
├── settlementDate (Date, nullable)
└── auditFields

AutoCollectionRule (entity)
├── id (PK, BIGINT auto-increment)
├── customerId (FK to Customer)
├── ruleName (VARCHAR)
├── description (VARCHAR, nullable)
├── triggerCondition (AutoCollectionTrigger enum)
├── collectionAmountType (CollectionAmountType enum)
├── collectionAmountValue (BigDecimal, nullable)
├── currency (Currency enum)
├── fundingAccountId (FK to Account)
├── isActive (BOOLEAN)
├── preNotificationDays (INTEGER)
├── retryAttempts (INTEGER)
├── retryIntervalHours (INTEGER)
├── createdAt (Timestamp)
├── createdBy (VARCHAR)
├── updatedAt (Timestamp)
└── updatedBy (VARCHAR)

AutoCollectionAttempt (entity)
├── id (PK, BIGINT auto-increment)
├── autoCollectionRuleId (FK to AutoCollectionRule)
├── invoiceId (FK to ReceivableInvoice)
├── attemptDateTime (Timestamp)
├── collectionAmount (BigDecimal)
├── currency (Currency enum)
├── status (AutoCollectionStatus enum)
├── errorCode (VARCHAR, nullable)
├── errorDescription (VARCHAR, nullable)
└── auditFields

CashManagementAudit (entity)
├── id (PK, BIGINT auto-increment)
├── eventTimestamp (Timestamp)
├── eventType (CashManagementEventType enum)
├── customerId (FK to Customer)
├── userId (VARCHAR, nullable)
├── userType (UserType enum)
├── entityType (VARCHAR)
├── entityId (VARCHAR)
├── actionPerformed (VARCHAR)
├── details (JSON)
├── ipAddress (VARCHAR, nullable)
├── userAgent (VARCHAR, nullable)
└── auditFields
```

### Enumerations

```
PayrollBatchStatus: PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
PayrollFileFormat: CSV, XML_ISO20022, XML_PAIN, NACHA
PayrollRecordStatus: PENDING, VALID, INVALID, PROCESSED, FAILED, HELD
InvoiceStatus: DRAFT, ISSUED, VIEWED, PARTIALLY_PAID, PAID, OVERDUE, WRITTEN_OFF, DISPUTED
PaymentStatus: PENDING, PROCESSING, SETTLED, FAILED, REVERSED, CANCELLED
PaymentMethod: BANK_TRANSFER, CHECK, CASH, CREDIT_CARD, DIRECT_DEBIT
LiquidityPositionStatus: CALCULATING, COMPLETE, FAILED
CashPoolingType: PHYSICAL_POOLING, NOTIONAL_POOLING, OVERDRAFT_OFFSET
CashPoolingStatus: PENDING, EXECUTED, FAILED, CANCELLED
BatchPaymentStatus: PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
BatchFileFormat: ISO20022, NACHA, EBICS, LOCAL_CLEARING
PaymentInstructionStatus: PENDING, VALID, INVALID, PROCESSING, SETTLED, FAILED, HELD_FOR_APPROVAL, CANCELLED
PaymentType: CREDIT_TRANSFER, DIRECT_DEBIT, CARD_PAYMENT
AutoCollectionTrigger: INVOICE_DUE_DATE, DAYS_BEFORE_DUE, INTERVAL_BASED
CollectionAmountType: FULL_AMOUNT, PERCENTAGE, FIXED_AMOUNT, MINIMUM_BALANCE
AutoCollectionStatus: PENDING, SUCCESS, FAILED_INSUFFICIENT_FUNDS, FAILED_INVALID_ACCOUNT, RETRY_SCHEDULED, CANCELLED
UserType: INTERNAL_USER, EXTERNAL_CUSTOMER, SYSTEM_PROCESS
CashManagementEventType: PAYROLL_UPLOAD, PAYROLL_PROCESSING, PAYMENT_INITIATION, COLLECTION_ATTEMPT, LIQUIDITY_CALCULATION, CASH_POOLING, BATCH_PROCESSING, RULE_CREATION, RULE_EXECUTION
```

### Relationships
- Customer 1:N PayrollBatch (via customerId)
- PayrollBatch 1:N PayrollRecord
- Customer 1:N ReceivableInvoice (via customerId, billToCustomerId)
- ReceivableInvoice 1:N ReceivablePayment
- Customer 1:N LiquidityPosition
- Customer 1:N CashPoolingTransaction
- Account 1:N CashPoolingTransaction (as fromAccountId, toAccountId)
- Customer 1:N BatchPayment
- BatchPayment 1:N BatchPaymentInstruction
- Customer 1:N AutoCollectionRule
- AutoCollectionRule 1:N AutoCollectionAttempt
- ReceivableInvoice 1:N AutoCollectionAttempt
- Customer 1:N CashManagementAudit

## Key Features

### Payroll Processing
- **Batch Upload:** Secure file upload with virus scanning and format validation
- **Validation Engine:** Multi-layer validation (format, data integrity, banking details)
- **Exception Handling:** Detailed error reporting with correction capabilities
- **Payment File Generation:** Support for multiple clearing formats (ACH, SEPA, local)
- **Status Tracking:** Real-time batch and record-level status monitoring
- **Funding Validation:** Pre-processing account balance verification
- **Limit Integration:** Automatic limit checking via Limits Management API
- **Charge Application:** Automatic fee application via Charges Management API

### Receivables Management
- **Invoice Presentment:** Electronic invoice delivery with customer notifications
- **Payment Matching:** Intelligent matching algorithms (exact amount, reference, date windows)
- **Reconciliation:** Automated reconciliation with manual exception handling
- **Dunning Management:** Configurable reminder sequences and escalation
- **Aging Reports:** Standard and customizable aging analysis
- **Forecasting:** Cash inflow prediction based on payment patterns
- **Write-off Handling:** Approved bad debt write-off with accounting entries
- **Dispute Management:** Invoice dispute tracking and resolution workflow

### Liquidity Optimization
- **Real-time Positioning:** Consolidated view across all accounts and entities
- **Multi-currency Support:** FX-rate converted consolidated reporting
- **Cash Pooling:** Physical and notional pooling with interest optimization
- **Sweeping:** Automated balance maintenance between accounts
- **Forecasting:** Statistical and rule-based liquidity forecasting
- **Alerting:** Threshold-based notifications for liquidity conditions
- **What-if Analysis:** Scenario modeling for liquidity planning
- **Integration:** Real-time balance feeds from Account Management API

### Batch Payment Processing
- **High-volume Handling:** Optimized for 10,000+ transaction batches
- **Format Support:** Multiple international and local payment formats
- **Validation:** Comprehensive pre-processing validation
- **Error Isolation:** Individual transaction error handling without batch failure
- **Approval Workflows:** Limit-based and manual approval integration
- **Transmission:** Secure payment file generation and bank transmission
- **Cancellation:** Time-window based payment cancellation
- **Tracking:** End-to-end payment tracking with status updates
- **Reversal:** Payment reversal and return handling

### Auto-Collection
- **Rule Engine:** Flexible rule configuration for various collection scenarios
- **Scheduling:** Time-based and event-triggered collection attempts
- **Pre-notification:** Customer advance notification before collection
- **Retry Logic:** Configurable retry attempts with exponential backoff
- **Funding Validation:** Pre-collection account balance verification
- **Partial Collection:** Support for partial amount collections
- **Result Reporting:** Detailed collection success/failure reporting
- **Accounting:** Automatic generation of collection accounting entries
- **Customer Communication:** Automated collection result notifications

### Reporting and Analytics
- **Real-time Dashboard:** Customizable treasury dashboard with key metrics
- **Standard Reports:** Pre-built treasury reports (cash forecast, liquidity, DSO)
- **Custom Report Builder:** Drag-and-drop report creation with filtering
- **Scheduled Reports:** Automated report generation and distribution
- **Export Formats:** Multiple output formats (PDF, Excel, CSV, XML)
- **Audit Trail Reporting:** Comprehensive compliance reporting
- **Regulatory Reporting:** Support for local regulatory reporting requirements
- **Analytics:** Trend analysis and predictive modeling capabilities

### Security and Compliance
- **Role-Based Access Control:** Fine-grained permissions for all functions
- **Dual Control:** Configurable approval workflows for high-value transactions
- **Audit Trail:** Immutable logging of all cash management operations
- **Data Encryption:** Encryption at rest and in transit for sensitive data
- **Fraud Detection:** Anomaly detection and suspicious activity monitoring
- **Segregation of Duties:** Prevention of conflicting role assignments
- **Data Privacy:** GDPR/CCPA compliance for personal data handling
- **Regulatory Adherence:** Support for local and international regulations

## API Design

### Cash Management Endpoints (Exposed via `.api` package)

#### Payroll Management
```
POST   /api/cash-management/payroll/batches          # Upload payroll batch
GET    /api/cash-management/payroll/batches          # List payroll batches
GET    /api/cash-management/payroll/batches/{batchId} # Get payroll batch details
POST   /api/cash-management/payroll/batches/{batchId}/process  # Process payroll batch
POST   /api/cash-management/payroll/batches/{batchId}/cancel   # Cancel payroll batch
GET    /api/cash-management/payroll/batches/{batchId}/records  # Get payroll records
GET    /api/cash-management/payroll/statistics       # Get payroll processing statistics
```

#### Receivables Management
```
POST   /api/cash-management/receivables/invoices     # Create receivable invoice
GET    /api/cash-management/receivables/invoices     # List receivable invoices
GET    /api/cash-management/receivables/invoices/{invoiceId}  # Get invoice details
PUT    /api/cash-management/receivables/invoices/{invoiceId}  # Update invoice
POST   /api/cash-management/receivables/invoices/{invoiceId}/payments  # Apply payment to invoice
GET    /api/cash-management/receivables/payments     # List receivable payments
GET    /api/cash-management/receivables/statistics   # Get receivables processing statistics
```

#### Liquidity Management
```
GET    /api/cash-management/liquidity/position       # Get current liquidity position
GET    /api/cash-management/liquidity/forecast       # Get liquidity forecast
POST   /api/cash-management/liquidity/cash-pooling   # Execute cash pooling transaction
GET    /api/cash-management/liquidity/cash-pooling/history  # Get cash pooling history
GET    /api/cash-management/liquidity/statistics     # Get liquidity management statistics
```

#### Batch Payment Processing
```
POST   /api/cash-management/batch-payments           # Upload batch payment file
GET    /api/cash-management/batch-payments           # List batch payments
GET    /api/cash-management/batch-payments/{batchId} # Get batch payment details
POST   /api/cash-management/batch-payments/{batchId}/process  # Process batch payment
POST   /api/cash-management/batch-payments/{batchId}/cancel   # Cancel batch payment
GET    /api/cash-management/batch-payments/{batchId}/instructions  # Get batch payment instructions
GET    /api/cash-management/batch-payments/statistics  # Get batch payment statistics
```

#### Auto-Collection
```
POST   /api/cash-management/auto-collection/rules    # Create auto-collection rule
GET    /api/cash-management/auto-collection/rules    # List auto-collection rules
GET    /api/cash-management/auto-collection/rules/{ruleId}  # Get rule details
PUT    /api/cash-management/auto-collection/rules/{ruleId}  # Update rule
DELETE /api/cash-management/auto-collection/rules/{ruleId}  # Delete rule
POST   /api/cash-management/auto-collection/rules/{ruleId}/execute  # Execute rule manually
GET    /api/cash-management/auto-collection/attempts  # List collection attempts
GET    /api/cash-management/auto-collection/statistics  # Get auto-collection statistics
```

#### Reporting and Analytics
```
GET    /api/cash-management/reports/dashboard        # Get dashboard data
GET    /api/cash-management/reports/standard/{reportType}  # Get standard report
POST   /api/cash-management/reports/custom           # Create custom report
GET    /api/cash-management/reports/custom/{reportId}  # Get custom report details
POST   /api/cash-management/reports/custom/{reportId}/execute  # Execute custom report
GET    /api/cash-management/reports/audit-trail       # Get audit trail report
GET    /api/cash-management/reports/statistics        # Get reporting statistics
```

### Response Formats
All responses follow standard structure:
```json
{
  "success": true,
  "data": { /* resource data */ },
  "errorCode": null,
  "message": null,
  "timestamp": "ISO 8601 datetime"
}
```

Error responses include appropriate HTTP status codes and error codes (CAS-XXX prefix).

## Integration Points

### Customer Module (via Customer API)
- Reads customer information for validation and reference
- Validates customer eligibility for cash management services
- Links cash management transactions to customer profiles
- Provides customer-cash management summary views
- Validates authorized users for cash management operations

### Account Module (via Account API)
- Validates account status and availability for transactions
- Retrieves account balances for funding verification
- Posts transactions to accounts (credits/debits)
- Retrieves account details for payment processing
- Validates account ownership and authorization
- Provides account statement and transaction history

### Limits Module (via Limits API)
- Checks payment amounts against customer/account limits
- Determines approval requirements based on limits
- Retrieves limit utilization and availability
- Validates limit configurations for cash management products
- Integrates limit checking into payment processing workflows

### Charges Module (via Charges API)
- Calculates applicable fees for cash management transactions
- Applies charges to customer accounts
- Retrieves charge details for transaction reconciliation
- Supports various charge types (transaction, monthly, volume-based)
- Provides charge reporting and reconciliation capabilities

### Master Data Module (via Master Data API)
- Retrieves currency information for multi-currency support
- Validates country codes for international payments
- Accesses industry codes for customer classification
- Gets holiday calendars for payment date calculations
- Retrieves exchange rates for FX conversions

### Event/Kafka Integration
- Publishes domain events for significant cash management operations:
  - PayrollBatchCreated, PayrollBatchProcessed
  - ReceivableInvoiceCreated, ReceivablePaymentApplied
  - LiquidityPositionCalculated, CashPoolingTransactionExecuted
  - BatchPaymentCreated, BatchPaymentProcessed
  - AutoCollectionRuleCreated, AutoCollectionAttempted
- Subscribes to relevant events from other modules:
  - AccountBalanceUpdated (for liquidity calculations)
  - CustomerStatusChanged (for service eligibility)
  - LimitUpdated (for limit checking adjustments)

### External Integrations
- **Payment Networks:** SWIFT, SEPA, ACH, local clearing systems
- **File Transfer:** SFTP, FTPS for batch file exchanges
- **Validation Services:** Account validation, IBAN verification
- **FX Services:** Real-time foreign exchange rates for multi-currency operations
- **Notification Services:** Email, SMS for customer communications
- **Archival:** Document storage for invoices, payment files, audit trails

## Security & Authorization

### Access Controls
- Customers can only access their own cash management data
- Treasury roles have elevated privileges for payment initiation
- Accounting roles have access to receivables and reporting
- Audit roles have access to audit trails and compliance reports
- System processes have limited access for automated operations
- Role definitions managed through Customer Management RBAC

### Validation & Sanitization
- Input validation for all API requests using Jakarta Validation
- SQL injection prevention via JPA/Criteria API and query parameters
- XSS prevention in response rendering (JSON API primarily)
- File upload validation (type, size, virus scanning)
- Payment detail validation (account formats, bank codes)
- Rate limiting on sensitive operations (payment initiation, file uploads)

### Audit Trail
- All cash management operations logged with user context
- File uploads/downloads tracked with metadata
- Payment initiations, modifications, cancellations logged
- Limit exceptions and approvals recorded
- Charge applications and reversals tracked
- Regulatory reporting data preserved for minimum 7 years
- Immutable audit storage with tamper-evident mechanisms

### Data Protection
- Field-level encryption for sensitive data (account numbers, tax IDs)
- Transport encryption (TLS 1.3) for all API communications
- Key management through existing infrastructure (Vault/HSM)
- Data classification and handling procedures
- Backup and disaster recovery procedures
- Data retention and archival policies

## Technology Implementation

### Backend (Java/Spring Boot)
- Java 17, Spring Boot 3.2
- Spring Data JPA for data access
- Hibernate as JPA provider
- Jakarta Validation for request validation
- Spring Transaction management
- Spring Events for domain events
- Spring Kafka for event streaming
- SLF4J with Logback for logging
- Apache Commons FileUpload for file processing
- Jackson for JSON processing
- Apache POI for Excel report generation
- iText/Flying Saucer for PDF report generation

### Database (PostgreSQL)
- Proper indexing for query performance (composite indexes for common queries)
- Foreign key constraints for referential integrity
- Check constraints for enum validation
- Partitioning strategy for large tables (PayrollRecord, BatchPaymentInstruction, AutoCollectionAttempt)
- Connection pooling with HikariCP
- Read-only replicas for reporting workloads
- Backup and point-in-time recovery capabilities

### Testing Strategy
Follows the Test Strategy Template from `docs/superpowers/templates/test-strategy.md`:

#### Standards
- Merge failure prevention (stale cache, duplicate classes, Gradle syntax, mock conventions)
- Test conventions (error code constants, HTTP status verification, mock patterns, DTO types)
- Pre-merge checklist
- CI pipeline requirements
- Test data isolation
- Coverage requirements

#### Module-Specific Exception Constants
Each exception class must define `ERROR_CODE` as a public constant:

| Exception | Error Code |
|-----------|-----------|
| `PayrollBatchNotFoundException` | CAS-001 |
| `InvalidPayrollFileException` | CAS-002 |
| `InsufficientFundsException` | CAS-003 |
| `LimitExceededException` | CAS-004 |
| `InvalidReceivableStateException` | CAS-005 |
| `AutoCollectionRuleNotFoundException` | CAS-006 |
| `LiquidityCalculationException` | CAS-007 |
| `BatchPaymentProcessingException` | CAS-008 |
| `CashManagementException` | CAS-009 |

#### Module-Specific Tests
- Unit tests for service layer (80%+ coverage)
- Integration tests for repositories and API controllers
- Contract tests for REST APIs (using Springdoc OpenAPI validation)
- Test data builders for consistent test setup
- Performance tests for critical operations (batch processing, liquidity calculations)
- Security tests for authorization and data protection
- File processing tests for various formats and edge cases
- Event publishing/subscription tests

## Non-Functional Requirements

### Performance
- API response time: < 200ms for 95% of requests under normal load
- Payroll batch processing: < 2 seconds per 100 records
- Batch payment processing: < 5 seconds per 1000 instructions
- Liquidity position calculation: < 1 second for standard portfolio
- Real-time dashboard updates: < 5 seconds
- File upload processing: < 10 seconds for 10MB file
- Concurrent users: 1000+ active sessions

### Scalability
- Horizontal scaling via stateless service instances
- Database read replicas for inquiry-heavy workloads (reporting, liquidity)
- Caching layer for frequently accessed reference data (currencies, holiday calendars)
- Asynchronous processing for non-critical operations (notifications, archival)
- Message queue (Kafka) buffering for peak load handling
- Database connection pooling for efficient resource utilization
- Microservice-like deployment within modular monolith for independent scaling

### Reliability
- Automated failover for database connections
- Circuit breaker pattern for external dependencies (payment networks, FX services)
- Graceful degradation for non-core features (advanced analytics when dependencies down)
- Comprehensive health checks and monitoring (liveness, readiness, startup)
- Retry mechanisms with exponential backoff for transient failures
- Dead letter queue processing for failed messages
- Backup and recovery procedures compliant with RTO/RPO targets
- Disaster recovery testing procedures

### Compliance
- GDPR/CCPA data privacy controls for personal data handling
- SOX financial reporting controls for audit trail integrity
- PCI DSS compliance for payment data handling (where applicable)
- AML/KYC integration points for transaction monitoring
- Basel III/IV liquidity coverage ratio reporting support
- Audit trail retention (7+ years) with secure archival
- Local regulatory compliance (country-specific where applicable)
- Accessibility compliance (WCAG 2.1 AA) for user interfaces

## Implementation Notes

### Assumptions
- Foundation modules (Customer, Account, Limits, Charges, Master Data) are implemented with proper APIs
- Existing authentication and authorization infrastructure (OAuth2/JWT, RBAC) is available
- Core Banking System integration layer exists for settlement of payments
- Standard payment formats (ACH, SEPA, wire, local clearing) are supported by infrastructure
- Sufficient database performance for real-time balance inquiries and transaction processing
- File storage system (S3-compatible) available for document storage
- Notification infrastructure (email/SMS) available for customer communications
- FX rate service available for multi-currency operations

### Future Enhancements
- AI-powered cash flow forecasting with machine learning
- Advanced payment fraud detection using behavioral analytics
- Real-time payment tracking and confirmation
- Dynamic discounting and supply chain finance integration
- Cross-border payment optimization with FX hedging
- Virtual account management for complex corporate structures
- API-based treasury workstation for third-party integrations
- Enhanced analytics with predictive modeling and scenario planning
- Mobile workforce approval capabilities
- Regulatory reporting automation for specific jurisdictions
- Integration with accounting ERP systems (SAP, Oracle, Microsoft Dynamics)

### Dependencies
- Customer module (for customer data, validation, authorization)
- Account module (for account validation, balance inquiry, transaction posting)
- Limits module (for limit checking and approval workflows)
- Charges module (for fee calculation and application)
- Master Data module (for reference data: currencies, countries, industries)
- Shared kernel (for common enums, exceptions, utilities, audit fields)
- Infrastructure modules (security, messaging, monitoring)

### Implementation Phases
1. **Phase 1:** Core data models and repository layer
2. **Phase 2:** Payroll processing subsystem
3. **Phase 3:** Receivables management subsystem
4. **Phase 4:** Liquidity optimization subsystem
5. **Phase 5:** Batch payment processing subsystem
6. **Phase 6:** Auto-collection subsystem
7. **Phase 7:** Reporting and analytics subsystem
8. **Phase 8:** Security, audit trail, and compliance features
9. **Phase 9:** Integration testing and performance optimization
10. **Phase 10:** User acceptance testing and documentation

---
*This design follows the modular monolith architecture principles and API contract enforcement policies specified in AGENTS.md. All implementation must respect module boundaries by only depending on the `.api` and `.dto` packages of other modules, never accessing internal repositories or services directly.*