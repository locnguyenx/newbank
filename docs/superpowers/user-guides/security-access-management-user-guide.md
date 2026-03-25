# Security & Access Management (IAM) - User Guide

**Module:** Security & Access Management  
**Application:** Web Portal  
**Version:** 1.0  
**Date:** 2026-03-24

---

## Overview

The Security & Access Management module handles user authentication, user management, and role-based access control in the banking portal.

---

## Logging In

### Accessing the Portal

1. Open your browser and navigate to the application URL
2. The login page will be displayed

### Login Credentials

Use your registered email and password:

```
Email:    [your email]
Password: [your password]
```

Click **Sign In** to access the dashboard.

### First Login

If this is your first time logging in:
- Use the default admin credentials: `admin@bank.com` / `admin123`
- You will be redirected to the dashboard

---

## Dashboard

After successful login, you will see the **Dashboard** which provides:
- Overview of key metrics
- Quick access to recent activities
- Navigation to all modules

---

## User Menu

After logging in, you can access user options by clicking your profile icon in the top-right corner:

### View Profile
Click on your profile to view your account details.

### Logout
Click **Sign Out** to securely log out of the application.

---

## Managing Users (Admin Only)

If you have administrator privileges, you can manage users in the system.

### Access User Management

1. Click on your profile icon in the top-right corner
2. Look for **Settings** or **User Management** in the menu
3. Navigate to the Users section

### Creating a New User

1. Click the **Add User** or **Create User** button
2. Fill in the required information:
   - **Email** - User's email address
   - **Password** - Initial password
   - **User Type** - INTERNAL or EXTERNAL
   - **Role** - Appropriate role for the user
3. Click **Save** or **Create**

### Editing a User

1. Find the user in the list
2. Click on the user's row or the **Edit** icon
3. Modify the necessary fields
4. Click **Save**

### Deactivating/Activating a User

1. Find the user in the list
2. Click the **Deactivate** button to disable access
3. Click **Activate** to restore access

---

## Roles & Permissions

### Available Roles

| Role | Description |
|------|-------------|
| **System Admin** | Full access to all system functions |
| **Company Admin** | Full access within company |
| **Company Maker** | Create transactions (may require approval) |
| **Company Checker** | Approve/reject transactions |
| **Company Viewer** | View-only access |
| **Department Maker** | Create within department |
| **Department Checker** | Approve within department |
| **Department Viewer** | View-only within department |

### Role Assignment

Administrators can assign roles when creating or editing users.

---

## Multi-Factor Authentication (MFA)

### Enabling MFA

For enhanced security, you can enable Multi-Factor Authentication:

1. Go to your **Profile** or **Security Settings**
2. Find **Two-Factor Authentication** or **MFA**
3. Click **Enable** or **Setup**
4. Scan the QR code with your authenticator app (Google Authenticator, Authy)
5. Enter the verification code from your app
6. Click **Verify** to confirm

### Using MFA

After enabling MFA:
1. Log in with your email and password
2. Enter the 6-digit code from your authenticator app
3. Access will be granted

### Disabling MFA

If needed, you can disable MFA through your security settings.

---

## Password Management

### Changing Password

1. Go to your **Profile** or **Settings**
2. Click **Change Password**
3. Enter your current password
4. Enter your new password (must meet requirements)
5. Confirm the new password
6. Click **Save**

### Password Requirements

Your password must:
- Be at least 8 characters
- Contain at least one uppercase letter
- Contain at least one lowercase letter
- Contain at least one number
- Contain at least one special character

---

## Session Management

### Session Timeout

- Sessions automatically expire after 15 minutes of inactivity
- You will need to log in again after timeout

### Logging Out

Always log out when finished:
1. Click your profile icon (top-right)
2. Click **Sign Out**
3. Close your browser

---

## Troubleshooting

### Can't Log In

- **Wrong password**: Click "Forgot Password" or contact admin
- **Account locked**: Contact administrator to unlock
- **Invalid credentials**: Verify email address is correct

### MFA Issues

- **Lost phone**: Contact admin to reset MFA
- **Code not working**: Check time sync on authenticator app

### Access Denied

- **No permission**: Contact administrator to request access
- **Role insufficient**: Request elevated permissions

---

## Support

For assistance:
1. Contact your system administrator
2. Check with your supervisor for access permissions
3. Report technical issues through helpdesk
