# Technical Design: Core Infrastructure Layer

**Version:** 1.0
**Date:** 2026-03-22
**Status:** Draft
**Category:** Infrastructure

---

## 1. Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                  Business Modules                    │
│         (Cash Mgmt, Trade Finance, Payments)         │
└──────────────────────┬──────────────────────────────┘
                       │ depends on
┌──────────────────────▼──────────────────────────────┐
│                Foundation Modules                     │
│  Customer | Account | Product | Limits | Charges |  │
│  MasterData                                          │
│  [add @AuditEntityListener + Kafka event publishers] │
└──────────────────────┬──────────────────────────────┘
                       │ uses
┌──────────────────────▼──────────────────────────────┐
│              Common Infrastructure                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐            │
│  │ Security │ │  Audit   │ │  Kafka   │            │
│  │ (JWT,    │ │ (Entity  │ │ (Event   │            │
│  │  RBAC,   │ │  change  │ │  bus for │            │
│  │  MFA)    │ │  log)    │ │  domain) │            │
│  └──────────┘ └──────────┘ └──────────┘            │
│  ┌──────────────────────────────────────┐           │
│  │  Common: BaseException, MessageCatalog│           │
│  │  GlobalExceptionHandler, AuditFields  │           │
│  └──────────────────────────────────────┘           │
└─────────────────────────────────────────────────────┘
```

## 2. Package Structure

New packages under `com.banking.common`:

```
com/banking/common/
├── security/
│   ├── config/
│   │   ├── SecurityConfig.java           # Spring Security filter chain
│   │   └── JwtConfig.java                # JWT properties (secret, expiry)
│   ├── jwt/
│   │   ├── JwtTokenProvider.java         # Generate/validate tokens
│   │   ├── JwtAuthenticationFilter.java  # Extract JWT from request
│   │   └── JwtTokenRepository.java       # Store refresh tokens
│   ├── auth/
│   │   ├── AuthController.java           # POST /api/auth/login, /refresh, /mfa
│   │   ├── AuthService.java              # Login logic, MFA flow
│   │   ├── UserPrincipal.java            # Implements UserDetails
│   │   └── dto/                          # LoginRequest, TokenResponse, MfaRequest
│   ├── rbac/
│   │   ├── Role.java                     # Enum: SYSTEM_ADMIN, HO_ADMIN, BRANCH_ADMIN, DEPARTMENT_MAKER, DEPARTMENT_CHECKER, DEPARTMENT_VIEWER, COMPANY_ADMIN, COMPANY_MAKER, COMPANY_CHECKER, COMPANY_VIEWER
│   │   ├── ScopeType.java                # Enum: GLOBAL, BRANCH, DEPARTMENT, COMPANY
│   │   ├── UserScope.java                # Maps user to scope (branch/company)
│   │   ├── UserScopeRepository.java      # Query scopes by user, by scope
│   │   ├── PermissionEvaluator.java      # Custom PermissionEvaluator for @PreAuthorize
│   │   └── AmountThreshold.java          # Role + max approval amount
│   ├── iam/
│   │   ├── controller/
│   │   │   ├── RoleManagementController.java        # CRUD roles + permissions
│   │   │   ├── UserManagementController.java        # CRUD users, assign roles/scopes
│   │   │   ├── UserPermissionController.java        # View user permission summary
│   │   │   ├── ThresholdManagementController.java   # Manage approval thresholds
│   │   │   ├── BulkImportController.java            # CSV user import
│   │   │   └── ActivityMonitoringController.java    # Login history, permission changes
│   │   ├── service/
│   │   │   ├── RoleManagementService.java
│   │   │   ├── UserManagementService.java
│   │   │   ├── UserPermissionService.java
│   │   │   ├── ThresholdManagementService.java
│   │   │   ├── BulkImportService.java
│   │   │   └── ActivityMonitoringService.java
│   │   ├── entity/
│   │   │   ├── RoleDefinition.java                  # Custom roles with permissions
│   │   │   ├── RolePermission.java                  # Maps role to permissions
│   │   │   ├── LoginHistory.java                    # Login/logout tracking
│   │   │   └── FailedLoginAttempt.java              # Failed login tracking
│   │   ├── repository/
│   │   │   ├── RoleDefinitionRepository.java
│   │   │   ├── RolePermissionRepository.java
│   │   │   ├── LoginHistoryRepository.java
│   │   │   └── FailedLoginAttemptRepository.java
│   │   └── dto/
│   │       ├── RoleRequest.java / RoleResponse.java
│   │       ├── UserCreateRequest.java / UserResponse.java
│   │       ├── UserPermissionSummary.java
│   │       ├── ThresholdRequest.java / ThresholdResponse.java
│   │       ├── BulkImportRequest.java / BulkImportResult.java
│   │       └── ActivityLogResponse.java
│   └── mfa/
│       ├── MfaSecret.java                # JPA entity: mfa_secrets table
│       ├── MfaSecretRepository.java      # Persist/retrieve MFA secrets
│       ├── TotpService.java              # Generate/verify TOTP codes
│       ├── SmsOtpService.java            # Send/verify SMS OTP
│       └── MfaConfig.java                # MFA provider configuration
├── audit/
│   ├── AuditLog.java                     # JPA entity: audit_log table
│   ├── AuditEntityListener.java          # @PrePersist/@PreUpdate listener
│   ├── AuditContext.java                 # Holds current user, IP, correlationId (ThreadLocal)
│   ├── AuditLogRepository.java           # Spring Data repository
│   └── AuditLogService.java              # Async write to audit_log
└── kafka/
    ├── KafkaConfig.java                  # Producer/consumer configuration
    ├── BaseDomainEvent.java              # Event envelope: eventId, type, timestamp, source, correlationId
    ├── DomainEventPublisher.java         # Publish events to Kafka
    └── DomainEventListener.java          # Base listener with error handling
```

### Kafka Topics

| Topic | Partitions | Consumer Group | Events |
|-------|-----------|----------------|--------|
| `customer-events` | 3 | `customer-consumer-group` | CustomerCreated, CustomerUpdated, CustomerDeactivated |
| `kyc-events` | 3 | `kyc-consumer-group` | KYCStatusChanged, SanctionsScreeningCompleted |
| `account-events` | 3 | `account-consumer-group` | AccountOpened, AccountClosed |
| `audit-events` | 3 | `audit-consumer-group` | (future: cross-module audit streaming) |

## 3. Data Model

### New Tables

```sql
-- Users (core identity table)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(20) NOT NULL,     -- INTERNAL, EXTERNAL
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, INACTIVE, LOCKED
    mfa_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Refresh tokens
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- MFA secrets
CREATE TABLE mfa_secrets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    totp_secret VARCHAR(64),
    mfa_type VARCHAR(20) NOT NULL,  -- TOTP, SMS
    enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

-- User scopes (maps user to branch/company)
CREATE TABLE user_scopes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scope_type VARCHAR(20) NOT NULL,  -- GLOBAL, BRANCH, DEPARTMENT, COMPANY
    scope_id BIGINT,                  -- branch_id or company_id
    role VARCHAR(20) NOT NULL,        -- ADMIN, MAKER, CHECKER, VIEWER
    UNIQUE(user_id, scope_type, scope_id, role)
);

-- Approval thresholds per role
CREATE TABLE approval_thresholds (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(20) NOT NULL,
    scope_type VARCHAR(20) NOT NULL,
    max_amount DECIMAL(19,4),
    currency VARCHAR(3) DEFAULT 'USD'
);

-- Audit log (immutable)
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(20) NOT NULL,      -- CREATE, UPDATE, DELETE
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    before_json JSONB,
    after_json JSONB,
    actor_user_id BIGINT,
    actor_type VARCHAR(20),           -- INTERNAL, EXTERNAL, SYSTEM
    ip_address VARCHAR(45),
    correlation_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_audit_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_actor ON audit_log(actor_user_id);
CREATE INDEX idx_audit_created ON audit_log(created_at);
```

### IAM Tables

```sql
-- Custom role definitions (beyond built-in ADMIN/MAKER/CHECKER/VIEWER)
CREATE TABLE role_definitions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    scope_type VARCHAR(20) NOT NULL,    -- GLOBAL, BRANCH, DEPARTMENT, COMPANY
    is_system BOOLEAN DEFAULT FALSE,    -- TRUE for built-in roles
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Permissions mapped to roles
CREATE TABLE role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_definition_id BIGINT NOT NULL REFERENCES role_definitions(id),
    resource VARCHAR(100) NOT NULL,     -- e.g., "customer", "account", "payment"
    action VARCHAR(50) NOT NULL,        -- e.g., "create", "read", "update", "delete", "approve"
    UNIQUE(role_definition_id, resource, action)
);

-- Login history (all successful logins)
CREATE TABLE login_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_type VARCHAR(20) NOT NULL,    -- PASSWORD, SSO
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    session_id VARCHAR(36),
    login_at TIMESTAMP NOT NULL,
    logout_at TIMESTAMP
);

-- Failed login attempts (security monitoring)
CREATE TABLE failed_login_attempts (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    ip_address VARCHAR(45),
    reason VARCHAR(100),               -- INVALID_PASSWORD, ACCOUNT_LOCKED, MFA_FAILED
    attempted_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_login_history_user ON login_history(user_id, login_at);
CREATE INDEX idx_failed_login_username ON failed_login_attempts(username, attempted_at);
CREATE INDEX idx_failed_login_ip ON failed_login_attempts(ip_address, attempted_at);
```

## 4. Impact on Existing Modules

Minimal changes — each module adds 2 things:

### 4.1 Entity Listener (one line per entity)

```java
@EntityListeners(AuditEntityListener.class)  // ADD THIS
public class Customer { ... }
```

### 4.2 Event Publishing (replace in-process publisher)

```java
// BEFORE
applicationEventPublisher.publishEvent(new CustomerCreatedEvent(customer));

// AFTER
domainEventPublisher.publish(new CustomerCreatedEvent(customer));
```

### 4.3 No Controller Changes Needed

`@PreAuthorize` annotations are added incrementally, not all at once.

## 5. API Gateway Readiness

Conventions defined now, gateway implemented later:

| Convention | Value |
|-----------|-------|
| Route prefix | `/api/v1/{module}/...` |
| Health check | `/api/health` (Spring Actuator) |
| Auth header | `Authorization: Bearer {token}` |
| Correlation ID | `X-Correlation-Id` header |
| Rate limit headers | `X-RateLimit-Limit`, `X-RateLimit-Remaining` |

Controllers are already at `/api/...`. When gateway is added, it proxies to the same paths.

## 6. Implementation Order (Layered)

| Layer | What | Depends on |
|-------|------|-----------|
| **0** | Fix foundation module `api` packages (5 modules) — add `<module>.api` package with service interfaces, move cross-module imports to use api/dto only (see AGENTS.md Architecture Enforcement) | None |
| **1a** | Spring Security + JWT + Auth endpoints | Layer 0 |
| **1b** | RBAC + user scopes + approval thresholds | Layer 1a |
| **1c** | MFA (TOTP + SMS) | Layer 1a |
| **2** | Audit trail (entity listener + audit_log) | Layer 1a (needs user context) |
| **3** | Kafka (config + event publisher/listener) | None (parallel with 1-2) |
| **4** | Docker Compose (PostgreSQL, Kafka, Zookeeper) | None (parallel) |

## 7. Test Strategy

### Standards

Follows [Test Strategy Template](../templates/test-strategy.md) for merge failure prevention, test conventions, pre-merge checklist, and CI requirements.

### Module-Specific Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `JwtTokenProviderTest` | Token generation, validation, expiry | Auth-1, Auth-4 |
| `JwtAuthenticationFilterTest` | Extract token from request, reject invalid | Auth-4 |
| `AuthControllerTest` | Login, refresh, MFA endpoints | Auth-1, Auth-2, Auth-3 |
| `PermissionEvaluatorTest` | Role-based permission checks | RBAC-4 |
| `ApprovalThresholdTest` | Amount threshold logic per role | RBAC-1, RBAC-2, RBAC-3 |
| `UserScopeServiceTest` | Branch/company scoping for queries | RBAC-5 |
| `TotpServiceTest` | TOTP generation, verification | MFA-1, MFA-3 |
| `SmsOtpServiceTest` | SMS OTP generation, verification | MFA-2 |
| `AuditEntityListenerTest` | Listener captures create/update with before/after | Audit-1, Audit-2 |
| `AuditLogServiceTest` | Async write to audit_log table | Audit-1, Audit-2 |
| `DomainEventPublisherTest` | Events published to Kafka with metadata | Kafka-1, Kafka-2 |
| `DomainEventListenerTest` | Consumer processes events | Kafka-3 |
| `RoleManagementServiceTest` | CRUD roles, assign permissions | IAM-1, IAM-2 |
| `UserManagementServiceTest` | CRUD users, assign roles/scopes, activate/deactivate | IAM-3, IAM-4, IAM-5 |
| `UserPermissionServiceTest` | View user permission summary | IAM-6 |
| `ThresholdManagementServiceTest` | CRUD approval thresholds per role | IAM-7 |
| `BulkImportServiceTest` | CSV user import, error handling | IAM-8 |
| `ActivityMonitoringServiceTest` | Login history, failed logins, permission changes | IAM-9, IAM-10 |
| `RoleManagementControllerTest` | Role CRUD API endpoints | IAM-1, IAM-2 |
| `UserManagementControllerTest` | User management API endpoints | IAM-3, IAM-4 |
| `BulkImportControllerTest` | Bulk import API endpoint | IAM-8 |

### RBAC Test Matrix

Tests must verify permissions for every role × action combination:

| Role | Create | Approve (≤ threshold) | Approve (> threshold) | View | Admin actions |
|------|--------|----------------------|----------------------|------|---------------|
| System Admin | ✅ | ✅ | ✅ | ✅ | ✅ |
| HO Admin | ✅ | ✅ | ✅ | ✅ | ✅ (HO scope) |
| Branch Admin | ✅ | ✅ | ✅ | ✅ | ✅ (branch scope) |
| Department Maker | ✅ | ❌ | ❌ | ✅ | ❌ |
| Department Checker | ❌ | ✅ | ❌ | ✅ | ❌ |
| Department Viewer | ❌ | ❌ | ❌ | ✅ | ❌ |
| Company Admin | ✅ | ✅ | ✅ | ✅ | ✅ (company scope) |
| Company Maker | ✅ | ❌ | ❌ | ✅ | ❌ |
| Company Checker | ❌ | ✅ | ❌ | ✅ | ❌ |
| Company Viewer | ❌ | ❌ | ❌ | ✅ | ❌ |

Each cell = one test case asserting 200 (allowed) or 403 (denied).

### Scope Isolation Tests

Verify users can only access data within their scope:

| Scenario | Test |
|----------|------|
| Branch admin sees only branch data | Query customers → only branch-linked customers returned |
| Company maker sees only company data | Query accounts → only company accounts returned |
| HO admin sees all data | Query customers → all customers returned |
| Cross-scope access denied | Branch A user tries to access Branch B data → 403 |

### Audit Trail Verification Tests

For each entity that gets `@AuditEntityListener`, verify:

| Action | Audit Log Verification |
|--------|----------------------|
| CREATE | `before_json` = null, `after_json` = full entity snapshot, `action` = CREATE |
| UPDATE | `before_json` = old values (changed fields only), `after_json` = new values, `action` = UPDATE |
| DELETE | `before_json` = entity snapshot, `after_json` = null, `action` = DELETE |
| Actor context | `actor_user_id` = authenticated user, `ip_address` = request IP, `correlation_id` = request header |

### Coverage Requirements

- **Backend:** Minimum 80% line coverage for `com.banking.common.security` and `com.banking.common.audit`
- **RBAC tests:** 100% of role × action matrix must be tested
- **Audit tests:** Every `@AuditEntityListener` entity must have CREATE + UPDATE test

## 8. Implementation Guardrails

**For Future Developers:**
The Infrastructure layer (`com.banking.common.*`) provides cross-cutting services used by ALL business and foundation modules. It is **dependency-free** (does not depend on any business module). This is critical: infrastructure must serve all modules without favoring any.

### Dependency Rule (Infra-Specific)

```
Business Modules (Account, Customer, Product, etc.)
    ↓ depend on
Infrastructure Layer (common.security, common.audit, common.kafka)
    ↓ depends on NOTHING (except maybe external libs)
```

**Infra modules MUST NOT:**
- Import any `com.banking.[module].domain.entity` 
- Import any `com.banking.[module].service` (internal implementations)
- Depend on foundation modules (customer, account, product, limits, charges, masterdata)
- Contain business logic (only generic, reusable infrastructure)

**Infra modules CAN:**
- Define generic interfaces that business modules implement (e.g., `TokenProvider` interface, implemented in security module)
- Publish infrastructure events (`AuditEvent`, `SecurityEvent`) that business modules can listen to
- Define base classes/utilities used by all modules (e.g., `BaseException`, `AuditFields`)

### Module-Specific Guardrails

#### Common Security (`com.banking.common.security`)
- Provides JWT authentication, RBAC, MFA
- Must not contain business-specific permissions (e.g., "ACCOUNT_OPERATOR" is business logic and belongs in customer module)
- Role definitions should be generic (ADMIN, USER, APPROVER) or configurable via database
- APIs: `JwtTokenProvider`, `UserDetailsService` implementations can depend on customer module (for user lookup) via **API only**

#### Common Audit (`com.banking.common.audit`)
- Provides `@AuditEntityListener` JPA entity listener
- Must not depend on any specific entity; uses reflection to capture changes generically
- Stores audit in generic tables (`audit_log`) with `entityName`, `entityId`, `operation`, `changes`

#### Common Kafka (`com.banking.common.kafka`)
- Provides `DomainEventPublisher` for Spring `ApplicationEvent` → Kafka bridge
- Must not depend on specific event types; publishes any `BaseDomainEvent`
- Event types are defined in each business module (account, customer, etc.)
- Ideally, kafka config lives here but event schemas live in business modules

### Code Review Checklist for Infra Changes:
- [ ] No imports from `com.banking.[any-business-module]`
- [ ] No business logic in infrastructure code
- [ ] New security roles are truly generic or configurable (not business-specific)
- [ ] Audit annotations apply to any entity without modification
- [ ] Kafka publisher can handle any event type without code changes

### For All Modules Using Infrastructure:

When using infrastructure services (security, audit, events):
- ✅ Inject `JwtTokenProvider` (interface) — implementation details hidden
- ✅ Use `@Auditable` annotation on entities — infrastructure handles persistence
- ✅ Publish events via `ApplicationEventPublisher` (or `DomainEventPublisher`) — infrastructure routes to Kafka
- ❌ Don't bypass infrastructure (e.g., manual audit log entries, custom JWT logic)

See `AGENTS.md` for complete architecture enforcement rules (note: infra modules are exempt from Rule 2's `.api` requirement since they provide implementation, not API contract to business modules — but they must still provide clear extension points via interfaces).

---

## 9. Kafka Integration Strategy

The `DomainEventPublisher` in `com.banking.common.kafka` bridges Spring `ApplicationEvent` to Kafka:

```java
@Component
public class DomainEventPublisher {
    public DomainEventPublisher(KafkaTemplate<String, BaseDomainEvent> kafkaTemplate) { ... }
    // Publishes any ApplicationEvent that extends BaseDomainEvent
}
```

**Why this design?**
- Business modules publish Spring events (simple, no Kafka dependencies in their code)
- `DomainEventPublisher` listens for all `ApplicationEvent`s and forwards to Kafka topic based on `eventType`
- Allows business modules to remain framework-agnostic (could switch to RabbitMQ, etc.)

**Event Naming Convention:**
- `com.banking.account.event.AccountOpenedEvent` → Kafka topic: `account.events`
- `com.banking.customer.event.KYCApprovedEvent` → Kafka topic: `customer.events`

**When to enable Kafka:**
- Currently Kafka is available but not necessarily enabled in dev
- Business modules can publish events regardless; they'll be in-process only if Kafka is down
- Can add `@Profile("kafka-enabled")` to `DomainEventPublisher` when ready

---

## 10. Deferred Infrastructure (Future Implementation)
