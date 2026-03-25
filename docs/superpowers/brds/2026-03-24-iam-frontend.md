# Business Requirements Document: IAM Frontend Implementation

**Version:** 1.0  
**Date:** 2026-03-24  
**Status:** Draft  
**Category:** Frontend - Identity & Access Management

---

## 1. Business Goals

Implement a complete frontend for the Identity & Access Management (IAM) module that enables:
- Bank staff (Internal users) to authenticate and manage the system
- Corporate customers (External users) to manage their company users and thresholds

## 2. User Roles & Stories

### Internal Roles (Bank Staff)

| Role | Access Level |
|------|-------------|
| **SYSTEM_ADMIN** | Full system configuration, user management, role management, activity monitoring, thresholds |
| **HO_ADMIN** | Head Office-level administration |
| **BRANCH_ADMIN** | Branch-level user management |
| **DEPARTMENT_MAKER** | Create records within department |
| **DEPARTMENT_CHECKER** | Approve records within department |
| **DEPARTMENT_VIEWER** | Read-only within department |

### External Roles (Corporate Customers)

| Role | Access Level |
|------|-------------|
| **COMPANY_ADMIN** | Manage company users, set approval thresholds |
| **COMPANY_MAKER** | Initiate transactions |
| **COMPANY_CHECKER** | Approve transactions |
| **COMPANY_VIEWER** | Read-only access |

---

## 3. Functional Requirements

### 3.1 Authentication (All Users)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-01 | Login page with email/password fields | Must |
| FR-02 | JWT token storage and management | Must |
| FR-03 | Automatic token refresh | Must |
| FR-04 | Logout functionality with token clearing | Must |
| FR-05 | Protected routes (redirect to login if not authenticated) | Must |
| FR-06 | MFA verification page after login | Must |
| FR-07 | MFA enrollment page (QR code + verification) | Must |

### 3.2 User Profile (All Authenticated Users)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-08 | View profile information (name, email, role) | Must |
| FR-09 | Change password | Must |
| FR-10 | Enable/disable MFA | Must |
| FR-11 | View own login history | Should |

### 3.3 User Management (System Admin / Company Admin)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-12 | List all users (filtered by role/company) | Must |
| FR-13 | Create new user (internal for System Admin, invite for Company Admin) | Must |
| FR-14 | Edit user details | Must |
| FR-15 | Deactivate/activate user | Must |
| FR-16 | Reset user MFA | Must (Company Admin) |
| FR-17 | Assign roles to user | Must |
| FR-18 | Assign branch/department scope | Must (Internal) |
| FR-19 | Bulk import users via CSV upload | Should |
| FR-20 | Search and filter users | Must |

### 3.4 Role Management (System Admin Only)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-21 | List all roles | Must |
| FR-22 | Create custom role | Must |
| FR-23 | Edit role (name, description) | Must |
| FR-24 | Delete custom role (not system roles) | Must |
| FR-25 | Assign permissions to role | Must |
| FR-26 | View all permissions | Must |

### 3.5 Activity Monitoring (System Admin / Company Admin)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-27 | View login history (all users for System Admin, company users for Company Admin) | Must |
| FR-28 | View failed login attempts | Must |
| FR-29 | Filter by date range, user | Should |
| FR-30 | View permission change audit trail | Should |

### 3.6 Approval Thresholds (System Admin / Company Admin)

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-31 | List all thresholds | Must |
| FR-32 | Create threshold (role, amount, currency) | Must |
| FR-33 | Edit threshold | Must |
| FR-34 | Delete threshold | Must |
| FR-35 | View threshold by user | Should |

### 3.7 Navigation & Layout

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-36 | Sidebar navigation with role-based menu items | Must |
| FR-37 | Header with user menu (profile, logout) | Must |
| FR-38 | Breadcrumb navigation | Should |
| FR-39 | Responsive design (mobile support) | Should |

---

## 4. Page Specifications

### 4.1 Public Pages

| Page | Route | Description |
|------|-------|-------------|
| Login | `/login` | Email/password login form |
| MFA Verify | `/mfa-verify` | MFA code entry after login |
| MFA Enroll | `/profile/mfa-setup` | QR code scan and verification |

### 4.2 Protected Pages - Common

| Page | Route | Access | Description |
|------|-------|--------|-------------|
| Dashboard | `/dashboard` | All | Welcome page with quick stats |
| Profile | `/profile` | All | View/edit profile, change password |
| Change Password | `/profile/change-password` | All | Password change form |

### 4.3 Protected Pages - Internal Admin

| Page | Route | Access | Description |
|------|-------|--------|-------------|
| User List | `/iam/users` | SYSTEM_ADMIN | List, search, filter users |
| Create User | `/iam/users/new` | SYSTEM_ADMIN | Create internal user form |
| Edit User | `/iam/users/:id` | SYSTEM_ADMIN | Edit user details |
| Role List | `/iam/roles` | SYSTEM_ADMIN | List all roles |
| Create Role | `/iam/roles/new` | SYSTEM_ADMIN | Create custom role |
| Edit Role | `/iam/roles/:id` | SYSTEM_ADMIN | Edit role and permissions |
| Activity Login History | `/iam/activity/login-history` | SYSTEM_ADMIN | Login history table |
| Activity Failed Logins | `/iam/activity/failed-logins` | SYSTEM_ADMIN | Failed attempts table |
| Threshold List | `/iam/thresholds` | SYSTEM_ADMIN | Approval thresholds |
| Create Threshold | `/iam/thresholds/new` | SYSTEM_ADMIN | Create threshold form |

### 4.4 Protected Pages - Company Admin

| Page | Route | Access | Description |
|------|-------|--------|-------------|
| Company Users | `/company/users` | COMPANY_ADMIN | List company users |
| Invite User | `/company/users/invite` | COMPANY_ADMIN | Invite external user |
| Edit User | `/company/users/:id` | COMPANY_ADMIN | Edit company user |
| Company Thresholds | `/company/thresholds` | COMPANY_ADMIN | Company approval thresholds |

---

## 5. UI/UX Requirements

### 5.1 Design System
- Use existing Ant Design components
- Follow current color scheme and typography
- Consistent spacing and layout

### 5.2 Tables
- Sortable columns
- Pagination (default 10/20/50 per page)
- Row actions (view, edit, delete)
- Bulk selection for actions

### 5.3 Forms
- Form validation with error messages
- Required field indicators
- Loading states during submission
- Success/error notifications

### 5.4 Navigation
- Collapsible sidebar
- Active route highlighting
- Role-based menu visibility
- User avatar and name in header

---

## 6. API Integration

### 6.1 Authentication
```
POST /api/auth/login          → TokenResponse
POST /api/auth/refresh       → TokenResponse
POST /api/auth/logout        → void
POST /api/auth/mfa/enroll    → MfaEnrollResponse
POST /api/auth/mfa/verify    → TokenResponse
POST /api/auth/mfa/enable    → void
POST /api/auth/mfa/disable   → void
GET  /api/auth/mfa/status    → { enabled: boolean }
```

### 6.2 User Management
```
GET    /api/iam/users              → UserResponse[]
POST   /api/iam/users              → UserResponse
GET    /api/iam/users/:id          → UserResponse
PUT    /api/iam/users/:id          → UserResponse
POST   /api/iam/users/:id/activate → UserResponse
POST   /api/iam/users/:id/deactivate → UserResponse
GET    /api/iam/users/active       → UserResponse[]
POST   /api/iam/users/import       → BulkImportResult
```

### 6.3 Role Management
```
GET    /api/iam/roles              → RoleResponse[]
POST   /api/iam/roles              → RoleResponse
GET    /api/iam/roles/:id          → RoleResponse
PUT    /api/iam/roles/:id          → RoleResponse
DELETE /api/iam/roles/:id          → void
PUT    /api/iam/roles/:id/permissions → RoleResponse
GET    /api/iam/permissions        → Permission[]
```

### 6.4 Activity Monitoring
```
GET /api/iam/activity/login-history         → Page<LoginHistoryResponse>
GET /api/iam/activity/login-history/user/:id → LoginHistoryResponse[]
GET /api/iam/activity/failed-logins          → FailedLoginAttempt[]
```

### 6.5 Thresholds
```
GET    /api/iam/thresholds              → AmountThreshold[]
POST   /api/iam/thresholds              → AmountThreshold
GET    /api/iam/thresholds/:id          → AmountThreshold
PUT    /api/iam/thresholds/:id          → AmountThreshold
DELETE /api/iam/thresholds/:id         → void
```

---

## 7. State Management

### 7.1 Redux Slices
- `authSlice` - Authentication state, tokens, user info
- `usersSlice` - User list, selected user, loading states
- `rolesSlice` - Role list, permissions
- `activitySlice` - Login history, failed logins
- `thresholdsSlice` - Threshold list

### 7.2 Local Storage
- `accessToken` - JWT access token
- `refreshToken` - JWT refresh token

---

## 8. Acceptance Criteria

### Login
- [ ] User can enter email and password
- [ ] Invalid credentials show error message
- [ ] Successful login redirects to dashboard
- [ ] MFA required triggers MFA verification page

### User Management
- [ ] System Admin can view all internal users
- [ ] System Admin can create new internal user
- [ ] System Admin can edit user details
- [ ] System Admin can activate/deactivate user
- [ ] Company Admin can view only company users
- [ ] Company Admin can invite new company user
- [ ] Company Admin can deactivate company user

### Role Management (System Admin)
- [ ] Can view all roles
- [ ] Can create custom role
- [ ] Can edit role permissions
- [ ] Cannot delete system roles

### Activity Monitoring
- [ ] System Admin can view all login history
- [ ] Can filter by date range
- [ ] Can view failed login attempts

### Thresholds
- [ ] Can create approval threshold
- [ ] Can edit threshold amount
- [ ] Can delete threshold

### Profile
- [ ] User can view own profile
- [ ] User can change password
- [ ] User can enable MFA
- [ ] User can disable MFA

---

## 9. Out of Scope (Phase 1)

- Password reset via email
- SMS OTP MFA (TOTP only)
- Two-factor enrollment for external users
- Advanced analytics dashboard
- Notification preferences
