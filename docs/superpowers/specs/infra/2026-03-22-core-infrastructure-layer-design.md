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
│   │   ├── Role.java                     # Enum: ADMIN, MAKER, CHECKER, VIEWER
│   │   ├── ScopeType.java                # Enum: GLOBAL, BRANCH, DEPARTMENT, COMPANY
│   │   ├── UserScope.java                # Maps user to scope (branch/company)
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

## 3. Data Model

### New Tables

```sql
-- Refresh tokens
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

-- MFA secrets
CREATE TABLE mfa_secrets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
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
| **0** | Fix foundation module `api` packages (5 modules) | None |
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

## 8. Deferred Infrastructure (Future Implementation)

| Item | Current State | When to Implement | Impact if Deferred |
|------|--------------|-------------------|-------------------|
| **CI/CD Pipeline** | None | Phase 2 | No code changes needed, pure DevOps |
| **Monitoring (Actuator + Prometheus/Grafana)** | None | Phase 2 | Add Spring Boot Actuator dependency + metrics config. Minimal refactoring. |
| **API Gateway (Kong/Spring Cloud Gateway)** | Conventions defined | Phase 2-3 | Routes proxy to existing `/api/...` paths. No controller changes needed. |
| **PostgreSQL (production)** | H2 for dev/tests | Phase 2 | Flyway migrations already target PostgreSQL. Switch datasource config only. |
| **ELK Stack (Centralized Logging)** | Basic Spring logging | Phase 3 | Add Logback JSON encoder + Filebeat. No code changes. |
| **Vault (Secrets Management)** | None | Phase 3 | Replace `application.yml` secrets with Vault paths. Config change only. |
| **Distributed Tracing (Jaeger/Zipkin)** | None | Phase 3 | Add Micrometer Tracing + Brave. Minimal code changes. |
| **Kafka Schema Registry** | None | Phase 3 | Add Avro/JSON Schema for event contracts. No existing code changes. |
| **SSO Integration (Azure AD)** | None | When required | Add OAuth2 social login flow alongside existing username/password. Auth controller handles both paths. |
| **Company Admin: Request new accounts** | None | Phase 2 | Extends self-service portal. Depends on Account module API. |
| **Company Admin: Manage signatories** | None | Phase 2 | Extends self-service portal. Depends on Customer module authorization. |
