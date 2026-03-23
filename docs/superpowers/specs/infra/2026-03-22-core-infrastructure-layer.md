# Business Requirements Document: Core Infrastructure Layer

**Version:** 1.0
**Date:** 2026-03-22
**Status:** Draft
**Category:** Infrastructure

---

## 1. Business Goals

Establish the foundational infrastructure that all banking modules depend on — authentication, authorization, audit trail, and event bus — so that business module implementation can proceed without future refactoring.

## 2. User Roles & Stories

### Roles

#### Internal (Bank Staff)

| Role | Responsibilities |
|------|-----------------|
| **System Admin** | Manages system config, roles, users, approval thresholds, monitors activity |
| **HO Admin** | Manages branch hierarchy, HO-level users |
| **Branch Admin** | Manages branch users and settings |
| **Department Maker** | Creates/edits records within department |
| **Department Checker** | Reviews/approves within department (amount thresholds) |
| **Department Viewer** | Read-only within department |

#### External (Corporate Customers)

| Role | Responsibilities |
|------|-----------------|
| **Company Admin** | Manages company users, sets approval thresholds |
| **Company Maker** | Initiates transactions, submits requests |
| **Company Checker** | Approves transactions (amount thresholds) |
| **Company Viewer** | Read-only within company |

### User Stories

#### Authentication

- **US-1** As a bank user, I can log in with email and password to receive JWT access and refresh tokens
- **US-2** As a bank user, I can refresh my access token without re-entering credentials
- **US-3** As a bank user, I must complete MFA (TOTP or SMS) after password login
- **US-3b** As a bank user, I can enroll in MFA by scanning a QR code to set up TOTP

#### IAM: Role Management (System Admin)

- **US-4** As a System Admin, I can create custom roles with specific permissions (resource + action)
- **US-5** As a System Admin, I can edit permissions assigned to existing roles
- **US-6** As a System Admin, I cannot delete system-built-in roles (ADMIN, MAKER, CHECKER, VIEWER)

#### IAM: User Management (System Admin)

- **US-7** As a System Admin, I can create internal users and assign roles with branch/department scope
- **US-8** As a System Admin, I can deactivate or reactivate internal users
- **US-9** As a System Admin, I can view a summary of all permissions a specific user has
- **US-10** As a System Admin, I can bulk import users via CSV file

#### IAM: Approval Thresholds (System Admin)

- **US-11** As a System Admin, I can configure approval thresholds per role (max amount a Checker can approve)

#### IAM: Company Admin Self-Service

- **US-12** As a Company Admin, I can invite users by email and assign Maker/Checker/Viewer roles within my company
- **US-13** As a Company Admin, I can deactivate users and reset MFA within my company
- **US-14** As a Company Admin, I can set company-level approval thresholds
- **US-15** As a Company Admin, I cannot manage users outside my company

#### IAM: Activity Monitoring (System Admin)

- **US-16** As a System Admin, I can view login history (who logged in, when, from where)
- **US-17** As a System Admin, I can view failed login attempts for security monitoring
- **US-18** As a System Admin, I can view permission change audit trail (who changed what)

#### Audit Trail

- **US-19** As a compliance user, I can view entity-level audit logs (who changed what, before/after snapshots)

#### Event Bus (Kafka)

- **US-20** As the system, domain events (CustomerCreated, KYCStatusChanged) are published to Kafka with actor and correlation metadata

### User Story to Functional Requirement Mapping

| User Story | Functional Requirements |
|------------|------------------------|
| US-1 | FR-01, FR-02 |
| US-2 | FR-03 |
| US-3 | FR-04 |
| US-3b | FR-04 |
| US-4 | FR-16 |
| US-5 | FR-16 |
| US-6 | FR-16 |
| US-7 | FR-17 |
| US-8 | FR-17 |
| US-9 | FR-18 |
| US-10 | FR-20 |
| US-11 | FR-19 |
| US-12 | FR-22 |
| US-13 | FR-23 |
| US-14 | FR-24 |
| US-15 | FR-22 |
| US-16 | FR-21 |
| US-17 | FR-21 |
| US-18 | FR-21 |
| US-19 | FR-08, FR-09, FR-10 |
| US-20 | FR-11, FR-12 |

## 3. Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-01 | JWT authentication with access token (15min) + refresh token (7 days) | Must |
| FR-02 | Login endpoint with username/password, returns JWT pair | Must |
| FR-03 | Refresh token endpoint | Must |
| FR-04 | MFA via TOTP (primary) and SMS OTP (fallback) | Must |
| FR-05 | RBAC: roles (ADMIN, MAKER, CHECKER, VIEWER) with permission mapping | Must |
| FR-06 | Amount-based approval thresholds per role | Must |
| FR-07 | Security annotations on controllers (`@PreAuthorize`) | Must |
| FR-08 | Entity-level audit trail: who, what action, entity type, entity ID, before/after JSON | Must |
| FR-09 | Audit log stored in dedicated `audit_log` table | Must |
| FR-10 | Audit captures IP address and correlation ID | Must |
| FR-11 | Kafka event bus for domain events (CustomerCreated, KYCStatusChanged, etc.) | Must |
| FR-12 | Kafka producer sends events with audit metadata (actor, correlationId) | Must |
| FR-13 | Docker Compose with PostgreSQL, Kafka, Zookeeper for local dev | Must |
| FR-14 | API Gateway conventions defined (route prefixes, health check endpoint) | Should |
| FR-15 | Security config is per-module reusable (shared `common/security` package) | Must |
| FR-16 | System Admin: CRUD operations for roles and permissions | Must |
| FR-17 | System Admin: create/deactivate/activate internal users, assign roles + scopes (branch/department) | Must |
| FR-18 | System Admin: view user permissions summary (what a user can access) | Must |
| FR-19 | System Admin: manage approval thresholds per role | Must |
| FR-20 | System Admin: bulk import users via CSV | Must |
| FR-21 | System Admin: activity monitoring dashboard (login history, permission changes, failed logins) | Must |
| FR-22 | Company Admin: invite users by email, assign Maker/Checker/Viewer roles within company | Must |
| FR-23 | Company Admin: deactivate users, reset MFA within company | Must |
| FR-24 | Company Admin: set company-level approval thresholds | Must |

## 4. Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR-01 | Auth endpoint response < 200ms (p95) |
| NFR-02 | JWT validation adds < 10ms overhead per request |
| NFR-03 | Audit log writes are async (non-blocking) |
| NFR-04 | Kafka producer is non-blocking (async send) |
| NFR-05 | All existing module tests continue to pass after integration |

## 5. Deferred Infrastructure (Future Implementation)

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
