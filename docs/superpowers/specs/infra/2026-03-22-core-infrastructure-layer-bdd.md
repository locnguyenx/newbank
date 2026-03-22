# BDD Scenarios: Core Infrastructure Layer

**Version:** 1.1
**Date:** 2026-03-22
**Status:** Draft
**Category:** Infrastructure
**BRD Reference:** `docs/superpowers/brds/infra/2026-03-22-core-infrastructure-layer.md`

Each BDD scenario is tagged with exactly one `@US` (User Story) and one `@FR` (Functional Requirement) for atomic traceability.

---

## 1. Authentication

```gherkin
Feature: JWT Authentication

  @US-1 @FR-01
  Scenario: User logs in with valid credentials
    Given a user "john@bank.com" exists with password "correct"
    When the user POSTs to "/api/auth/login" with email and password
    Then the response contains an access token (15min expiry) and refresh token (7 days)
    And the response status is 200

  @US-1 @FR-02
  Scenario: User logs in with invalid credentials
    Given a user "john@bank.com" exists
    When the user POSTs to "/api/auth/login" with wrong password
    Then the response status is 401
    And the response contains error message "Invalid credentials"

  @US-2 @FR-03
  Scenario: User refreshes access token
    Given a user has a valid refresh token
    When the user POSTs to "/api/auth/refresh" with the refresh token
    Then the response contains a new access token
    And the old refresh token is invalidated

  @US-1 @FR-01
  Scenario: User accesses protected endpoint with expired token
    Given a user has an expired access token
    When the user GETs "/api/customers" with the expired token
    Then the response status is 401
    And the response contains error message "Token expired"
```

## 2. Multi-Factor Authentication

```gherkin
Feature: Multi-Factor Authentication

  @US-3 @FR-04
  Scenario: User completes MFA with TOTP
    Given a user has TOTP configured
    When the user submits correct TOTP code after login
    Then the user receives JWT tokens
    And the response status is 200

  @US-3 @FR-04
  Scenario: User falls back to SMS OTP
    Given a user requests SMS OTP fallback
    When the user submits correct SMS OTP
    Then the user receives JWT tokens
    And the response status is 200

  @US-3 @FR-04
  Scenario: User submits invalid MFA code
    Given a user is at MFA step
    When the user submits an invalid TOTP code
    Then the response status is 401
    And the user remains at MFA step
```

## 3. Role-Based Access Control

```gherkin
Feature: Role-Based Access Control

  @US-11 @FR-06
  Scenario: Maker creates a transaction below threshold
    Given a Company Maker with approval threshold $50,000
    When the Maker submits a payment of $10,000
    Then the payment is created with status "PENDING_APPROVAL"
    And the audit log records the action

  @US-11 @FR-06
  Scenario: Checker approves transaction below their threshold
    Given a Company Checker with approval threshold $100,000
    When the Checker approves a payment of $10,000
    Then the payment status changes to "APPROVED"
    And the audit log records the approval

  @US-11 @FR-06
  Scenario: Checker cannot approve transaction above their threshold
    Given a Company Checker with approval threshold $100,000
    When the Checker attempts to approve a payment of $150,000
    Then the response status is 403
    And the error message indicates "Exceeds approval threshold"

  @US-11 @FR-05
  Scenario: Viewer cannot create transactions
    Given a Company Viewer
    When the Viewer attempts to create a payment
    Then the response status is 403

  @US-7 @FR-17
  Scenario: Internal user scoped to their branch
    Given a Department Maker at "Branch A"
    When the Maker queries customers
    Then only customers linked to "Branch A" are returned
```

## 4. Entity-Level Audit Trail

```gherkin
Feature: Entity-Level Audit Trail

  @US-19 @FR-08
  Scenario: Audit log created on entity creation
    Given a Maker creates a new customer record
    When the customer is saved
    Then an audit log entry is created with:
      | field         | value                    |
      | action        | CREATE                   |
      | entityType    | Customer                 |
      | entityId      | <customer-id>            |
      | before        | null                     |
      | after         | <customer JSON snapshot> |
      | actor         | <maker user ID>          |
      | ipAddress     | <request IP>             |
      | correlationId | <request correlation ID> |

  @US-19 @FR-08
  Scenario: Audit log created on entity update
    Given a customer "Acme Corp" exists with status "ACTIVE"
    When a Maker changes status to "SUSPENDED"
    Then an audit log entry is created with:
      | field      | value                    |
      | action     | UPDATE                   |
      | entityType | Customer                 |
      | before     | {"status": "ACTIVE"}     |
      | after      | {"status": "SUSPENDED"}  |

  @US-19 @FR-09
  Scenario: Audit log is immutable
    Given audit log entries exist
    When any user attempts to modify an audit log entry
    Then the response status is 405
```

## 5. Domain Event Publishing (Kafka)

```gherkin
Feature: Domain Event Publishing

  @US-20 @FR-11
  Scenario: CustomerCreated event published to Kafka
    Given Kafka is running
    When a new customer is created
    Then a "CustomerCreated" event is published to topic "customer-events"
    And the event payload contains customerId, customerType, actor, correlationId

  @US-20 @FR-11
  Scenario: KYCStatusChanged event published
    Given a customer with KYC status "PENDING"
    When KYC verification completes with status "VERIFIED"
    Then a "KYCStatusChanged" event is published to topic "kyc-events"
    And the event payload contains oldStatus, newStatus, customerId

  @US-20 @FR-12
  Scenario: Event consumer processes CustomerCreated
    Given a consumer listens on "customer-events"
    When a "CustomerCreated" event is received
    Then the consumer processes the event successfully
```

## 6. IAM: Role Management

```gherkin
Feature: Role Management (System Admin)

  @US-4 @FR-16
  Scenario: System Admin creates a new role
    Given a System Admin is logged in
    When the Admin creates role "TREASURY_MAKER" with permissions:
      | resource  | action |
      | payment   | create |
      | payment   | read   |
      | account   | read   |
    Then the role is created successfully
    And the role appears in the role list

  @US-5 @FR-16
  Scenario: System Admin assigns permissions to existing role
    Given role "BRANCH_CHECKER" exists
    When the Admin adds permission "payment:approve" to the role
    Then the permission is assigned
    And users with "BRANCH_CHECKER" can now approve payments

  @US-6 @FR-16
  Scenario: System Admin cannot delete system roles
    Given system role "ADMIN" exists
    When the Admin attempts to delete "ADMIN"
    Then the response status is 403
    And the error message indicates "Cannot delete system role"
```

## 7. IAM: User Management

```gherkin
Feature: User Management (System Admin)

  @US-7 @FR-17
  Scenario: System Admin creates internal user
    Given a System Admin is logged in
    When the Admin creates user "jane@bank.com" with:
      | field      | value           |
      | role       | DEPARTMENT_MAKER|
      | scopeType  | BRANCH          |
      | scopeId    | 5               |
    Then the user is created with status "ACTIVE"
    And a welcome email is sent to "jane@bank.com"

  @US-8 @FR-17
  Scenario: System Admin deactivates user
    Given user "jane@bank.com" is active
    When the Admin deactivates the user
    Then the user status changes to "INACTIVE"
    And the user cannot log in

  @US-9 @FR-18
  Scenario: System Admin views user permission summary
    Given user "jane@bank.com" has role DEPARTMENT_MAKER at Branch 5
    When the Admin views the user's permission summary
    Then the response shows:
      | resource | actions          |
      | payment  | create, read     |
      | account  | read             |
      | customer | read             |
    And the scope shows "Branch 5"

  @US-10 @FR-20
  Scenario: System Admin bulk imports users via CSV
    Given a CSV file with 50 users
    When the Admin uploads the CSV
    Then 48 users are created successfully
    And 2 users fail with "duplicate email" errors
    And a summary report is returned
```

## 8. IAM: Company Admin Self-Service

```gherkin
Feature: Company Admin Self-Service

  @US-12 @FR-22
  Scenario: Company Admin invites new user
    Given a Company Admin for "Acme Corp" is logged in
    When the Admin invites "bob@acme.com" with role COMPANY_MAKER
    Then an invitation email is sent to "bob@acme.com"
    And the user appears in company user list with status "PENDING"

  @US-13 @FR-23
  Scenario: Company Admin deactivates user
    Given user "bob@acme.com" is active in "Acme Corp"
    When the Company Admin deactivates the user
    Then the user status changes to "INACTIVE"
    And the user cannot log in

  @US-13 @FR-23
  Scenario: Company Admin resets user MFA
    Given user "bob@acme.com" has TOTP configured
    When the Company Admin resets MFA for the user
    Then the user's TOTP secret is cleared
    And the user must reconfigure MFA on next login

  @US-14 @FR-24
  Scenario: Company Admin sets approval threshold
    Given Company Admin for "Acme Corp" is logged in
    When the Admin sets approval threshold to $100,000 for COMPANY_CHECKER
    Then Company Checkers at "Acme Corp" can approve up to $100,000

  @US-15 @FR-22
  Scenario: Company Admin cannot manage users outside their company
    Given a Company Admin for "Acme Corp"
    When the Admin attempts to view users from "Beta Inc"
    Then the response status is 403
```

## 9. IAM: Activity Monitoring

```gherkin
Feature: Activity Monitoring (System Admin)

  @US-16 @FR-21
  Scenario: System Admin views login history
    Given users have logged in over the past 30 days
    When the Admin queries login history
    Then the response shows login records with:
      | field      | example                |
      | userId     | 42                     |
      | loginType  | PASSWORD               |
      | ipAddress  | 192.168.1.100          |
      | loginAt    | 2026-03-22T10:30:00Z   |
      | logoutAt   | 2026-03-22T11:45:00Z   |

  @US-17 @FR-21
  Scenario: System Admin views failed login attempts
    Given failed login attempts exist
    When the Admin queries failed logins for "jane@bank.com"
    Then the response shows attempts with:
      | field       | value                |
      | username    | jane@bank.com        |
      | ipAddress   | 10.0.0.50            |
      | reason      | INVALID_PASSWORD     |
      | attemptedAt | 2026-03-22T09:15:00Z |

  @US-18 @FR-21
  Scenario: System Admin views permission change audit
    Given a permission was changed for user "jane@bank.com"
    When the Admin queries permission changes
    Then the response shows the change with:
      | field         | value                          |
      | action        | UPDATE                         |
      | entityType    | UserScope                      |
      | before        | {"role": "VIEWER"}             |
      | after         | {"role": "MAKER"}              |
      | actor         | admin@bank.com                 |
```

---

## Requirement Coverage Summary

### User Story Coverage

| User Story | Covered By Scenario(s) |
|------------|----------------------|
| US-1 | S1.1, S1.2, S1.4 |
| US-2 | S1.3 |
| US-3 | S2.1, S2.2, S2.3 |
| US-4 | S6.1 |
| US-5 | S6.2 |
| US-6 | S6.3 |
| US-7 | S7.1, S3.5 |
| US-8 | S7.2 |
| US-9 | S7.3 |
| US-10 | S7.4 |
| US-11 | S3.1, S3.2, S3.3, S3.4 |
| US-12 | S8.1 |
| US-13 | S8.2, S8.3 |
| US-14 | S8.4 |
| US-15 | S8.5 |
| US-16 | S9.1 |
| US-17 | S9.2 |
| US-18 | S9.3 |
| US-19 | S4.1, S4.2, S4.3 |
| US-20 | S5.1, S5.2, S5.3 |

### Functional Requirement Coverage

| Requirement | Covered By Scenario(s) |
|-------------|----------------------|
| FR-01 | S1.1, S1.4 |
| FR-02 | S1.2 |
| FR-03 | S1.3 |
| FR-04 | S2.1, S2.2, S2.3 |
| FR-05 | S3.4 |
| FR-06 | S3.1, S3.2, S3.3 |
| FR-08 | S4.1, S4.2 |
| FR-09 | S4.3 |
| FR-11 | S5.1, S5.2 |
| FR-12 | S5.3 |
| FR-16 | S6.1, S6.2, S6.3 |
| FR-17 | S7.1, S7.2, S3.5 |
| FR-18 | S7.3 |
| FR-20 | S7.4 |
| FR-21 | S9.1, S9.2, S9.3 |
| FR-22 | S8.1, S8.5 |
| FR-23 | S8.2, S8.3 |
| FR-24 | S8.4 |
