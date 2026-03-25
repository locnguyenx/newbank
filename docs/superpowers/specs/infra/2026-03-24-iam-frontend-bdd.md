# BDD Specification: IAM Frontend

**Version:** 1.0  
**Date:** 2026-03-24  
**Status:** Draft  
**Module:** Frontend - Identity & Access Management  
**BRD Reference:** `docs/superpowers/brds/2026-03-24-iam-frontend.md`

Each BDD scenario is tagged with exactly one `@US` (User Story) and one `@FR` (Functional Requirement) for atomic traceability.

---

## 1. Authentication

```gherkin
Feature: User Login

  @US-1 @FR-01
  Scenario: Login with valid credentials
    Given the user is on the login page
    When the user enters valid email "admin@bank.com" and password "admin123"
    And the user clicks the "Sign In" button
    Then the user is redirected to the dashboard
    And the access token is stored in localStorage

  @US-1 @FR-02
  Scenario: Login with invalid credentials
    Given the user is on the login page
    When the user enters email "admin@bank.com" and wrong password "wrongpass"
    And the user clicks the "Sign In" button
    Then an error message "Invalid email or password" is displayed
    And the user remains on the login page

  @US-1 @FR-02
  Scenario: Login with empty credentials
    Given the user is on the login page
    When the user leaves email and password empty
    And the user clicks the "Sign In" button
    Then validation errors are displayed for required fields

  @US-2 @FR-03
  Scenario: Automatic token refresh
    Given the user is logged in with valid access token
    When the access token expires
    Then the system automatically refreshes the token
    And the user session continues without interruption

  @US-3 @FR-06
  Scenario: MFA required after login
    Given the user's account has MFA enabled
    When the user logs in with valid credentials
    Then the user is redirected to MFA verification page
    And the user must enter MFA code to access the system

  @US-3b @FR-07
  Scenario: Enroll in MFA
    Given the user is logged in and on the MFA setup page
    When the user clicks "Enable MFA"
    Then a QR code is displayed for scanning
    When the user scans the QR code with authenticator app
    And enters the verification code
    Then MFA is enabled for the user account

  @US-4 @FR-04
  Scenario: Logout
    Given the user is logged in
    When the user clicks "Logout" in the user menu
    Then the user is redirected to login page
    And tokens are cleared from localStorage
```

---

## 2. Protected Routes

```gherkin
Feature: Route Protection

  @US-5 @FR-05
  Scenario: Access protected route without authentication
    Given the user is not logged in
    When the user tries to access "/iam/users"
    Then the user is redirected to "/login"
    And the original URL is stored for redirect after login

  @US-5 @FR-05
  Scenario: Access protected route with valid token
    Given the user is logged in with valid token
    When the user accesses "/dashboard"
    Then the page is displayed successfully

  @US-5 @FR-05
  Scenario: Access forbidden page with insufficient role
    Given the user is logged in with role "DEPARTMENT_VIEWER"
    When the user tries to access "/iam/users/new"
    Then access denied error is displayed
    Or the user is redirected to dashboard
```

---

## 3. User Profile

```gherkin
Feature: User Profile Management

  @US-8 @FR-08
  Scenario: View own profile
    Given the user is logged in
    When the user navigates to "/profile"
    Then the profile page displays:
      | Field     | Value |
      | Email     | current data |
      | Full Name     | current data |
      | Role      | current data |
      | MFA Status| current data |
    Then the user can edit `Full Name` and submit changes 

  @US-9 @FR-09
  Scenario: Change password
    Given the user is on the profile page
    When the user enters current password "oldpass"
    And enters new password "NewPass123!"
    And confirms new password "NewPass123!"
    And clicks "Change Password"
    Then the password is changed successfully
    And a success message is displayed

  @US-9 @FR-09
  Scenario: Password change with wrong current password
    Given the user is on the profile page
    When the user enters wrong current password "wrongpass"
    And enters new password "NewPass123!"
    And confirms new password "NewPass123!"
    And clicks "Change Password"
    Then an error message is displayed
    And the password remains unchanged

  @US-10 @FR-10
  Scenario: Enable MFA
    Given the user is on the profile page
    And MFA is currently disabled
    When the user clicks "Enable MFA"
    Then the MFA enrollment page is displayed
    And the user can complete QR code scanning

  @US-10 @FR-10
  Scenario: Disable MFA
    Given the user is on the profile page
    And MFA is currently enabled
    When the user clicks "Disable MFA"
    And confirms the action
    Then MFA is disabled for the user account

```

---

## 3. User Settings

```gherkin
Feature: User Settings Management

  @US-8 @FR-08
  Scenario: Settings Page
    Given the user is logged in
    When the user navigates to Settings under User profile icon
    Then the Settings page displays:
      - Notification preferences (email, SMS, push)
      - Alert types (transactions, security, weekly report)
      - Display settings (language, timezone, date format)
      - Placeholder save functionality (no backend API)
```

---

## 4. User Management - System Admin

```gherkin
Feature: User Management (System Admin)

  @US-7 @FR-12
  Scenario: View all internal users
    Given the user is logged in with role "SYSTEM_ADMIN"
    When the user navigates menu "IAM/User Management" (url: "/iam/users")
    Then all internal users are displayed in a table
    And each row shows: email, role, status, created date

  @US-7 @FR-20
  Scenario: Search users
    Given the user is on the user list page
    When the user enters "john" in the search box
    Then only users with "john" in their email are displayed

  @US-7 @FR-20
  Scenario: Filter users by role
    Given the user is on the user list page
    When the user selects role filter "SYSTEM_ADMIN"
    Then only users with role "SYSTEM_ADMIN" are displayed

  @US-7 @FR-13
  Scenario: Create new internal user
    Given the user is on the user list page
    When the user clicks "Add User"
    And fills the form:
      | Field        | Value                  |
      | Email        | newuser@bank.com       |
      | Password     | SecurePass123!         |
      | Role         | DEPARTMENT_MAKER       |
      | Branch       | HO                     |
      | Department   | Operations             |
    And clicks "Create"
    Then the new user is created
    And a success message is displayed

  @US-7 @FR-14
  Scenario: Edit user details
    Given the user is on the user list page
    When the user clicks "Edit" on a user row
    And modifies the department
    And clicks "Save"
    Then the user details are updated
    And a success message is displayed

  @US-8 @FR-15
  Scenario: Deactivate user
    Given a user "john@bank.com" exists with status "ACTIVE"
    And the user is on the user list page
    When the user clicks "Deactivate" on john's row
    And confirms the action
    Then john's status changes to "INACTIVE"
    And john can no longer log in

  @US-8 @FR-15
  Scenario: Reactivate user
    Given a user "john@bank.com" exists with status "INACTIVE"
    And the user is on the user list page
    When the user clicks "Activate" on john's row
    Then john's status changes to "ACTIVE"
    And john can log in again
```

---

## 5. User Management - Company Admin

```gherkin
Feature: User Management (Company Admin)

  @US-12 @FR-12
  Scenario: View company users only
    Given the user is logged in with role "COMPANY_ADMIN"
    And belongs to company "Acme Corp"
    When the user navigates to menu "Company User Management"  "/company/users"
    Then only users belonging to "Acme Corp" are displayed

  @US-12 @FR-13
  Scenario: Invite company user
    Given the user is on the company users page
    When the user clicks "Invite User"
    And fills:
      | Field   | Value              |
      | Email   | newuser@acme.com   |
      | Role    | COMPANY_MAKER      |
    And clicks "Send Invite"
    Then an invitation is sent to the user
    And the user appears in the list with status "PENDING"

  @US-13 @FR-16
  Scenario: Reset user MFA
    Given a company user has MFA enabled
    And the user is on the company users page
    When the Company Admin clicks "Reset MFA" on that user
    Then the user's MFA is disabled
    And the user can set up MFA again

  @US-13 @FR-15
  Scenario: Deactivate company user
    Given a company user exists with status "ACTIVE"
    And the user is on the company users page
    When the user clicks "Deactivate"
    And confirms
    Then the user's status changes to "INACTIVE"
```

---

## 6. Role Management (System Admin)

```gherkin
Feature: Role Management

  @US-4 @FR-21
  Scenario: View all roles
    Given the user is logged in with role "SYSTEM_ADMIN"
    When the user navigates to menu "IAM/Roles"  (url:"/iam/roles")
    Then all roles are displayed including system and custom roles
    And system roles show as non-deletable

  @US-4 @FR-22
  Scenario: Create custom role
    Given the user is on the role list page
    When the user clicks "Add Role"
    And fills:
      | Field       | Value              |
      | Name        | AUDIT_VIEWER       |
      | Description | Can view audit logs|
    And clicks "Create"
    Then the new custom role is created

  @US-5 @FR-23
  Scenario: Edit custom role
    Given a custom role "AUDIT_VIEWER" exists
    And the user is on the role list page
    When the user clicks "Edit" on the role
    And modifies the description
    And clicks "Save"
    Then the role is updated

  @US-6 @FR-24
  Scenario: Delete custom role
    Given a custom role "AUDIT_VIEWER" exists
    And no users are assigned to this role
    And the user is on the role list page
    When the user clicks "Delete" on the role
    And confirms
    Then the role is deleted

  @US-6 @FR-24
  Scenario: Cannot delete system role
    Given the system role "SYSTEM_ADMIN" exists
    And the user is on the role list page
    When the user clicks "Delete" on "SYSTEM_ADMIN"
    Then an error message "Cannot delete system role" is displayed

  @US-5 @FR-25
  Scenario: Assign permissions to role
    Given a custom role "AUDIT_VIEWER" exists
    And the user is on the role edit page
    When the user selects permissions:
      | Permission        |
      | AUDIT_LOG_VIEW    |
      | USER_VIEW         |
    And clicks "Save"
    Then the role has the selected permissions

  @US-5 @FR-26
  Scenario: View all permissions
    Given the user is on the role list page
    When the user clicks "View Permissions"
    Then a list of all available permissions is displayed
    And permissions are grouped by category
```

---

## 7. Activity Monitoring

```gherkin
Feature: Activity Monitoring

  @US-16 @FR-27
  Scenario: View login history
    Given the user is logged in with role "SYSTEM_ADMIN"
    When the user navigates to menu "IAM/Activity Monitoring"  (url:"/iam/activity/login-history")
    Then all login events are displayed in a table
    And each row shows: user, email, timestamp, IP address, status

  @US-16 @FR-29
  Scenario: Filter login history by date
    Given the user is on the login history page
    When the user selects date range "2026-03-01" to "2026-03-24"
    Then only login events within that range are displayed

  @US-16 @FR-29
  Scenario: Filter login history by user
    Given the user is on the login history page
    When the user enters user email "john@bank.com"
    Then only login events for that user are displayed

  @US-17 @FR-28
  Scenario: View failed login attempts
    Given the user is on the activity page
    When the user navigates to "Failed Logins"
    Then failed login attempts are displayed
    And each row shows: email, timestamp, IP address, failure reason

  @US-18 @FR-30
  Scenario: View permission change audit trail
    Given the user is on the activity page
    When the user navigates to "Permission Changes"
    Then permission change events are displayed
    And each row shows: user, changed by, permission, timestamp
```

---

## 8. Approval Thresholds

```gherkin
Feature: Approval Thresholds

  @US-11 @FR-31
  Scenario: View all thresholds
    Given the user is logged in with role "SYSTEM_ADMIN"
    When the user navigates to menu "IAM/Approval Thresholds" (url:"/iam/thresholds")
    Then all approval thresholds are displayed
    And each row shows: role, amount, currency, type

  @US-11 @FR-32
  Scenario: Create threshold
    Given the user is on the thresholds page
    When the user clicks "Add Threshold"
    And fills:
      | Field      | Value      |
      | Role       | CHECKER    |
      | Amount     | 50000      |
      | Currency   | USD        |
      | Type       | DAILY      |
    And clicks "Create"
    Then the new threshold is created

  @US-11 @FR-33
  Scenario: Edit threshold
    Given a threshold exists for role "CHECKER"
    And the user is on the thresholds page
    When the user clicks "Edit" on the threshold
    And modifies the amount to "75000"
    And clicks "Save"
    Then the threshold is updated

  @US-11 @FR-34
  Scenario: Delete threshold
    Given a threshold exists
    And the user is on the thresholds page
    When the user clicks "Delete" on the threshold
    And confirms
    Then the threshold is deleted

  @US-14 @FR-24
  Scenario: Company Admin sets company threshold
    Given the user is logged in with role "COMPANY_ADMIN"
    When the user navigates to "/company/thresholds"
    And creates a threshold for "COMPANY_CHECKER"
    Then the threshold applies to the user's company
```

---

## 9. Company Admin Scope Enforcement

```gherkin
Feature: Company Admin Scope Restrictions

  @US-15 @FR-22
  Scenario: Company Admin cannot view other company users
    Given the user is logged in with role "COMPANY_ADMIN"
    And belongs to company "Acme Corp"
    When the user tries to access "/iam/users"
    Then access is denied
    Or the user is redirected to "/company/users"

  @US-15 @FR-22
  Scenario: Company Admin cannot manage system thresholds
    Given the user is logged in with role "COMPANY_ADMIN"
    When the user tries to navigate to "/iam/thresholds"
    Then access is denied
    Or the user is redirected to dashboard

  @US-15 @FR-24
  Scenario: Company Admin can only set company-level thresholds
    Given the user is logged in with role "COMPANY_ADMIN"
    When the user navigates to "/company/thresholds"
    Then thresholds created apply only to their company
    And system-wide thresholds are not affected
```

---

## 10. Traceability Matrix

### User Story → BDD Scenario Coverage

| User Story | FR(s) | BDD Scenario(s) |
|------------|-------|-----------------|
| US-1 | FR-01, FR-02 | S1.1, S1.2, S1.3 |
| US-2 | FR-03 | S1.4 |
| US-3 | FR-06 | S1.5 |
| US-3b | FR-07 | S1.6 |
| US-4 | FR-04 | S1.7 |
| US-5 | FR-05 | S2.1, S2.2, S2.3 |
| US-6 | FR-05 | S2.3 |
| US-7 | FR-12, FR-13, FR-14, FR-20 | S4.1, S4.2, S4.3, S4.4, S4.5, S4.6 |
| US-8 | FR-15 | S4.7, S4.8 |
| US-9 | FR-09 | S3.3, S3.4, S3.5 |
| US-10 | FR-10 | S3.6, S3.7 |
| US-11 | FR-31, FR-32, FR-33, FR-34 | S8.1, S8.2, S8.3, S8.4, S8.5 |
| US-12 | FR-12, FR-13 | S5.1, S5.2 |
| US-13 | FR-15, FR-16 | S5.3, S5.4 |
| US-14 | FR-24 | S8.5 |
| US-15 | FR-22 | S9.1, S9.2, S9.3 |
| US-16 | FR-27, FR-29 | S7.1, S7.2, S7.3 |
| US-17 | FR-28 | S7.4 |
| US-18 | FR-30 | S7.5 |

### Functional Requirement Coverage Summary

| Requirement | Covered By Scenario(s) |
|-------------|----------------------|
| FR-01 | S1.1 |
| FR-02 | S1.2, S1.3 |
| FR-03 | S1.4 |
| FR-04 | S1.7 |
| FR-05 | S2.1, S2.2, S2.3 |
| FR-06 | S1.5 |
| FR-07 | S1.6 |
| FR-08 | S3.1 |
| FR-09 | S3.3, S3.4, S3.5 |
| FR-10 | S3.6, S3.7 |
| FR-12 | S4.1, S5.1 |
| FR-13 | S4.4, S5.2 |
| FR-14 | S4.5 |
| FR-15 | S4.6, S4.7, S5.4 |
| FR-16 | S5.3 |
| FR-17 | S4.4, S4.5 |
| FR-18 | S4.4, S4.5 |
| FR-19 | S8.1, S8.2, S8.3, S8.4 |
| FR-20 | S4.2, S4.3 |
| FR-21 | S6.1 |
| FR-22 | S5.2, S9.1 |
| FR-23 | S6.2 |
| FR-24 | S6.3, S6.4, S6.5, S8.5, S9.3 |
| FR-25 | S6.6 |
| FR-26 | S6.7 |
| FR-27 | S7.1 |
| FR-28 | S7.4 |
| FR-29 | S7.2, S7.3 |
| FR-30 | S7.5 |
| FR-31 | S8.1 |
| FR-32 | S8.2 |
| FR-33 | S8.3 |
| FR-34 | S8.4 |
| FR-35 | S8.5 |
| FR-36 | Navigation sidebar with role-based items |
| FR-37 | Header with user menu |
| FR-38 | Breadcrumb navigation |
| FR-39 | Responsive design |

---

## 11. Role-Based UI Coverage

| Page/Feature | SYSTEM_ADMIN | HO_ADMIN | BRANCH_ADMIN | COMPANY_ADMIN | COMPANY_MAKER | DEPARTMENT_VIEWER |
|--------------|-------------|----------|--------------|---------------|---------------|-------------------|
| Login | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Dashboard | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Profile | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Change Password | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| MFA Setup | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| /iam/users | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| /iam/roles | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| /iam/activity | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| /iam/thresholds | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| /company/users | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ |
| /company/thresholds | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ |
