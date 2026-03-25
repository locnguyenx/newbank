# Design Specification: IAM Frontend Implementation

**Version:** 1.0  
**Date:** 2026-03-24  
**Status:** Draft  
**Module:** Frontend - Identity & Access Management  
**BRD Reference:** `docs/superpowers/brds/2026-03-24-iam-frontend.md`  
**BDD Reference:** `docs/superpowers/specs/2026-03-24-iam-frontend-bdd.md`

---

## 1. Architecture Overview

### 1.1 Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | React 18 |
| Language | TypeScript |
| UI Library | Ant Design 6 |
| State Management | Redux Toolkit |
| Routing | React Router DOM 7 |
| HTTP Client | Axios |
| Testing | Vitest |
| Build Tool | Vite |

### 1.2 Project Structure

```
frontend/src/
├── api/                    # Generated OpenAPI clients
├── components/
│   ├── common/            # Shared components
│   │   ├── ProtectedRoute.tsx
│   │   ├── PageHeader.tsx
│   │   ├── DataTable.tsx
│   │   └── ConfirmDialog.tsx
│   ├── layout/
│   │   ├── AppLayout.tsx
│   │   ├── Sidebar.tsx
│   │   ├── Header.tsx
│   │   └── AuthLayout.tsx    # For login/MFA pages
│   └── iam/               # IAM-specific components
│       ├── UserForm.tsx
│       ├── RoleForm.tsx
│       ├── ThresholdForm.tsx
│       └── PermissionSelector.tsx
├── pages/
│   ├── auth/
│   │   ├── LoginPage.tsx
│   │   ├── MfaVerifyPage.tsx
│   │   └── MfaEnrollPage.tsx
│   ├── iam/
│   │   ├── UserListPage.tsx
│   │   ├── UserFormPage.tsx
│   │   ├── RoleListPage.tsx
│   │   ├── RoleFormPage.tsx
│   │   ├── ActivityPage.tsx
│   │   └── ThresholdListPage.tsx
│   ├── company/
│   │   ├── CompanyUserListPage.tsx
│   │   ├── CompanyUserFormPage.tsx
│   │   └── CompanyThresholdPage.tsx
│   └── profile/
│       ├── ProfilePage.tsx
│       └── ChangePasswordPage.tsx
├── services/
│   ├── authService.ts
│   ├── userService.ts
│   ├── roleService.ts
│   ├── permissionService.ts
│   ├── activityService.ts
│   └── thresholdService.ts
├── store/
│   ├── slices/
│   │   ├── authSlice.ts
│   │   ├── usersSlice.ts
│   │   ├── rolesSlice.ts
│   │   ├── activitySlice.ts
│   │   └── thresholdsSlice.ts
│   └── index.ts
├── hooks/
│   ├── useAuth.ts
│   ├── usePermissions.ts
│   └── usePagination.ts
├── types/
│   ├── auth.types.ts
│   ├── user.types.ts
│   ├── role.types.ts
│   └── index.ts
├── utils/
│   ├── apiClient.ts
│   └── permissions.ts
└── App.tsx
```

---

## 2. Component Design

### 2.1 Authentication Flow

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│  Login Page │────▶│  Auth API   │────▶│  Dashboard  │
└─────────────┘     └──────────────┘     └─────────────┘
                           │
                           ▼ (if MFA required)
                    ┌──────────────┐     ┌─────────────┐
                    │ MFA Verify   │────▶│  Dashboard  │
                    │    Page      │     └─────────────┘
                    └──────────────┘
```

### 2.2 Protected Route Component

```typescript
// components/common/ProtectedRoute.tsx
interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ 
  children, 
  allowedRoles 
}) => {
  const { isAuthenticated, user, loading } = useAuth();
  
  if (loading) {
    return <Spin />;
  }
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  if (allowedRoles && user?.role && !allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }
  
  return <>{children}</>;
};
```

### 2.3 Sidebar Navigation (Role-Based)

```typescript
// components/layout/Sidebar.tsx
const menuConfig = {
  SYSTEM_ADMIN: [
    { key: 'dashboard', icon: <DashboardOutlined />, label: 'Dashboard', path: '/dashboard' },
    { key: 'users', icon: <UserOutlined />, label: 'User Management', path: '/iam/users' },
    { key: 'roles', icon: <TeamOutlined />, label: 'Role Management', path: '/iam/roles' },
    { key: 'activity', icon: <HistoryOutlined />, label: 'Activity', path: '/iam/activity' },
    { key: 'thresholds', icon: <ThresholdOutlined />, label: 'Thresholds', path: '/iam/thresholds' },
  ],
  COMPANY_ADMIN: [
    { key: 'dashboard', icon: <DashboardOutlined />, label: 'Dashboard', path: '/dashboard' },
    { key: 'company-users', icon: <UserOutlined />, label: 'Users', path: '/company/users' },
    { key: 'company-thresholds', icon: <ThresholdOutlined />, label: 'Thresholds', path: '/company/thresholds' },
  ],
  // ... other roles
};
```

---

## 3. State Management

### 3.1 Auth Slice

```typescript
// store/slices/authSlice.ts
interface AuthState {
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  mfaRequired: boolean;
  mfaToken: string | null;
  loading: boolean;
  error: string | null;
}
```

**Actions:**
- `login(credentials)` - Authenticate user
- `logout()` - Clear session
- `refreshToken()` - Refresh JWT
- `verifyMfa(code)` - Complete MFA
- `enrollMfa()` - Start MFA enrollment

### 3.2 Users Slice

```typescript
// store/slices/usersSlice.ts
interface UsersState {
  users: User[];
  selectedUser: User | null;
  loading: boolean;
  error: string | null;
  pagination: PaginationState;
  filters: UserFilters;
}
```

### 3.3 Roles Slice

```typescript
// store/slices/rolesSlice.ts
interface RolesState {
  roles: Role[];
  permissions: Permission[];
  loading: boolean;
  error: string | null;
}
```

---

## 4. API Integration

### 4.1 Service Layer

```typescript
// services/userService.ts
export const userService = {
  getUsers: (params?: UserQueryParams) => 
    apiClient.get('/iam/users', { params }),
  
  getUser: (id: number) => 
    apiClient.get(`/iam/users/${id}`),
  
  createUser: (data: CreateUserRequest) => 
    apiClient.post('/iam/users', data),
  
  updateUser: (id: number, data: UpdateUserRequest) => 
    apiClient.put(`/iam/users/${id}`, data),
  
  activateUser: (id: number) => 
    apiClient.post(`/iam/users/${id}/activate`),
  
  deactivateUser: (id: number) => 
    apiClient.post(`/iam/users/${id}/deactivate`),
};
```

### 4.2 API Client Interceptor

```typescript
// services/apiClient.ts
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  config.headers['X-Username'] = localStorage.getItem('username') || 'system';
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) {
        try {
          const { data } = await axios.post('/api/auth/refresh', { refreshToken });
          localStorage.setItem('accessToken', data.accessToken);
          localStorage.setItem('refreshToken', data.refreshToken);
          error.config.headers.Authorization = `Bearer ${data.accessToken}`;
          return apiClient(error.config);
        } catch {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);
```

---

## 5. Page Specifications

### 5.1 Login Page

**Route:** `/login`

**Components:**
- Logo
- Email input (validated)
- Password input (with show/hide toggle)
- "Sign In" button
- Error message display

**Flow:**
1. User enters credentials
2. Click Sign In
3. API call to `/api/auth/login`
4. If MFA required → redirect to MFA verify
5. If success → store tokens, redirect to dashboard

### 5.2 User List Page

**Route:** `/iam/users` (System Admin)

**Components:**
- Search bar
- Filters: Role, Status
- Table columns: Email, Role, Status, Created Date, Actions
- "Add User" button
- Pagination

**Actions:**
- View → Navigate to detail
- Edit → Navigate to edit form
- Activate/Deactivate → API call + refresh list
- Delete → Confirm dialog + API call

### 5.3 User Form Page

**Route:** `/iam/users/new` or `/iam/users/:id`

**Fields:**
- Email (required, validated)
- Password (required for create, optional for edit)
- Role (select from available roles)
- Branch (select, for internal users)
- Department (select, for internal users)
- Status (display only)

### 5.4 Role List Page

**Route:** `/iam/roles`

**Components:**
- Table: Name, Description, Type (System/Custom), User Count, Actions
- "Create Role" button (custom roles only)

**Actions:**
- View → Show permissions
- Edit → Navigate to edit (custom only)
- Delete → Confirm (custom only, no users assigned)

### 5.5 Activity Page

**Route:** `/iam/activity/login-history`

**Components:**
- Tabs: Login History, Failed Logins
- Date range picker
- User filter (autocomplete)
- Table: User, Email, Timestamp, IP, Status

### 5.6 Threshold List Page

**Route:** `/iam/thresholds`

**Components:**
- Table: Role, Amount, Currency, Type, Actions
- "Add Threshold" button

### 5.7 Profile Page

**Route:** `/profile`

**Components:**
- User info card
- Change password form
- MFA toggle/setup

### 5.8 Settings Page
SettingsPage at /settings with:
- Notification preferences (email, SMS, push)
- Alert types (transactions, security, weekly report)
- Display settings (language, timezone, date format)
- Placeholder save functionality (no backend API)

---

## 6. Security Implementation

### 6.1 Token Storage

- Access token: `localStorage` (short-lived, 15 min)
- Refresh token: `localStorage` (long-lived, 7 days)

### 6.2 Route Protection

```typescript
// Route configuration in App.tsx
const routeConfig = [
  { path: '/login', component: LoginPage, public: true },
  { path: '/mfa-verify', component: MfaVerifyPage, public: true },
  { 
    path: '/iam/users', 
    component: UserListPage, 
    roles: ['SYSTEM_ADMIN'] 
  },
  { 
    path: '/company/users', 
    component: CompanyUserListPage, 
    roles: ['COMPANY_ADMIN'] 
  },
];
```

### 6.3 Permission Check Hook

```typescript
// hooks/usePermissions.ts
const usePermissions = () => {
  const { user } = useAuth();
  
  const hasPermission = (permission: string): boolean => {
    if (!user?.permissions) return false;
    return user.permissions.includes(permission);
  };
  
  const hasRole = (role: string): boolean => {
    return user?.role === role;
  };
  
  return { hasPermission, hasRole };
};
```

---

## 7. Error Handling

### 7.1 API Error Handling

```typescript
// Centralized error handling in apiClient
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message || 'An error occurred';
    message.error(message);
    return Promise.reject(error);
  }
);
```

### 7.2 Form Validation

- Use Ant Design Form with validation rules
- Display inline errors
- Show loading state during submission
- Display success/error notifications

---

## 8. Testing Strategy

### 8.1 Unit Tests (Vitest)

**Components to test:**
- ProtectedRoute
- Sidebar navigation
- Auth form validation
- DataTable sorting/filtering

**Services to test:**
- authService
- userService
- roleService

### 8.2 Integration Tests

- Login flow
- Protected route redirect
- User CRUD operations

### 8.3 Test Coverage Target

- Minimum 80% coverage
- Focus on critical paths: login, protected routes, CRUD operations

---

## 9. Implementation Tasks

### Phase 1: Authentication (Priority: Critical)

| Task | Description |
|------|-------------|
| T-1.1 | Create Login page component |
| T-1.2 | Implement auth service |
| T-1.3 | Implement auth Redux slice |
| T-1.4 | Create ProtectedRoute component |
| T-1.5 | Add login route to App.tsx |
| T-1.6 | Update Header with user menu & logout |
| T-1.7 | Implement MFA verify page |
| T-1.8 | Implement token refresh |

### Phase 2: User Management (Priority: High)

| Task | Description |
|------|-------------|
| T-2.1 | Create user service |
| T-2.2 | Create users Redux slice |
| T-2.3 | Create UserListPage |
| T-2.4 | Create UserFormPage (create/edit) |
| T-2.5 | Add routes for user management |
| T-2.6 | Update Sidebar with role-based menu |

### Phase 3: Role Management (Priority: High)

| Task | Description |
|------|-------------|
| T-3.1 | Create role service |
| T-3.2 | Create roles Redux slice |
| T-3.3 | Create RoleListPage |
| T-3.4 | Create RoleFormPage |
| T-3.5 | Create PermissionSelector component |

### Phase 4: Activity & Thresholds (Priority: Medium)

| Task | Description |
|------|-------------|
| T-4.1 | Create activity service |
| T-4.2 | Create ActivityPage |
| T-4.3 | Create threshold service |
| T-4.4 | Create ThresholdListPage |

### Phase 5: Company Admin (Priority: Medium)

| Task | Description |
|------|-------------|
| T-5.1 | Create CompanyUserListPage |
| T-5.2 | Create CompanyUserFormPage |
| T-5.3 | Create CompanyThresholdPage |
| T-5.4 | Add company admin routes |

### Phase 6: Profile & Settings (Priority: Low)

| Task | Description |
|------|-------------|
| T-6.1 | Create ProfilePage |
| T-6.2 | Create ChangePasswordPage |
| T-6.3 | Implement MFA enrollment flow |

---

## 10. Acceptance Criteria

### Authentication
- [ ] User can log in with valid credentials
- [ ] Invalid credentials show error message
- [ ] MFA required triggers MFA page
- [ ] Tokens are stored and refreshed automatically
- [ ] Logout clears tokens and redirects to login
- [ ] Protected routes redirect to login when not authenticated

### User Management (System Admin)
- [ ] Can view all internal users
- [ ] Can create new user
- [ ] Can edit user details
- [ ] Can activate/deactivate user
- [ ] Search and filter works

### User Management (Company Admin)
- [ ] Can view only company users
- [ ] Can invite new user
- [ ] Can deactivate user
- [ ] Cannot access system admin pages

### Role Management
- [ ] Can view all roles
- [ ] Can create custom role
- [ ] Can assign permissions to role
- [ ] Cannot delete system roles

### Activity
- [ ] Can view login history
- [ ] Can filter by date and user

### Thresholds
- [ ] Can create/edit/delete thresholds

### Profile
- [ ] Can view profile
- [ ] Can change password
- [ ] Can enable/disable MFA
