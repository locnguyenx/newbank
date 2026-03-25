# Account Management - User Guide

**Module:** Account Management  
**Application:** Web Portal  
**Version:** 1.0  
**Date:** 2026-03-24

---

## Overview

The Account Management module allows you to open, view, and manage bank accounts. You can handle different account types including Current, Savings, Fixed Deposit, and Loan accounts.

---

## Accessing Account Management

1. Log in to the banking portal
2. Click on **Accounts** in the main navigation menu
3. The Account List page will be displayed

---

## Account List View

The Account List shows all accounts with:
- **Account Number** - Unique account identifier
- **Account Name** - Nickname or product name
- **Customer** - Associated customer name
- **Type** - CURRENT, SAVINGS, FIXED_DEPOSIT, or LOAN
- **Currency** - Account currency (USD, EUR, etc.)
- **Status** - ACTIVE, PENDING, FROZEN, or CLOSED
- **Balance** - Current account balance

### Searching Accounts

Use the search bar to find specific accounts:
- Type account number or customer name
- Results update as you type

### Filtering

Filter accounts by:
- **Type** - Current, Savings, etc.
- **Status** - Active, Frozen, Closed
- **Customer** - Specific customer

---

## Opening a New Account

### Starting Account Opening

1. From the Account List, click **Open Account** or **+ New Account**
2. The Account Opening form will appear

### Filling the Account Form

**Select Customer:**
- Search and select the customer
- Customer must be active

**Select Product:**
- Choose the account product type:
  - **Current Account** - For daily transactions
  - **Savings Account** - For savings
  - **Fixed Deposit** - For term deposits
  - **Loan Account** - For credit facilities

**Currency:**
- Select the account currency (USD, EUR, GBP, etc.)

**Account Details:**
- **Nickname** - Optional friendly name for the account

**Account Holders:**
- **Primary Holder** - The main account owner (auto-selected)
- **Additional Holders** - Add joint account holders if needed

### Submitting

1. Review all information
2. Click **Submit** or **Open Account**
3. Account will be created with ACTIVE status

---

## Viewing Account Details

1. From the Account List, click on an account row
2. The Account Detail page shows:

**Header Information:**
- Account Number
- Account Name/Nickname
- Customer Name
- Product Type
- Status
- Opened Date

**Balance Section:**
- **Available Balance** - Funds available for use
- **Current Balance** - Actual balance including holds
- **Hold Amount** - Temporary holds

**Account Holders:**
- List of all account holders with their roles

**Recent Transactions:**
- Latest transactions on the account

---

## Viewing Account Statement

1. Open an account detail page
2. Click **Statement** or **View Statement**
3. Select date range:
   - **From Date** - Start of period
   - **To Date** - End of period
4. Click **Generate** or **View**

The statement shows:
- Opening balance
- All transactions (date, description, amount)
- Closing balance
- Total credits and debits

### Exporting Statement

Look for an **Export** or **Download** button to:
- Export as PDF
- Export as Excel
- Export as CSV

---

## Managing Account Status

### Freezing an Account

To temporarily disable an account (e.g., for suspicious activity):

1. Open the account detail page
2. Click **Freeze** or **Freeze Account**
3. Confirm the action
4. Account status changes to **FROZEN**

**Effects:**
- No transactions allowed
- Incoming funds may be rejected
- Customer cannot access funds

### Unfreezing an Account

1. Open the frozen account
2. Click **Unfreeze** or **Activate**
3. Confirm the action
4. Account returns to **ACTIVE** status

### Closing an Account

To permanently close an account:

1. Open the account detail page
2. Click **Close** or **Close Account**
3. Ensure:
   - Balance is zero
   - No pending transactions
4. Confirm the closure
5. Account status changes to **CLOSED**

**Note:** Closed accounts cannot be reopened.

---

## Understanding Account Types

| Type | Description | Key Features |
|------|-------------|--------------|
| **Current** | Checking account | Daily transactions, overdraft option |
| **Savings** | Savings account | Earn interest, limited withdrawals |
| **Fixed Deposit** | Term deposit | Higher interest, locked term |
| **Loan** | Credit facility | Borrowed funds, repayment schedule |

---

## Best Practices

1. **Account Opening**
   - Verify customer identity before opening
   - Ensure correct product selection
   - Double-check currency

2. **Monitoring**
   - Review account balances regularly
   - Check for unauthorized transactions
   - Monitor frozen accounts

3. **Statements**
   - Generate monthly statements
   - Review for discrepancies
   - Keep records for audit

---

## Troubleshooting

### Can't Open Account

- **Customer not found**: Verify customer exists
- **Customer inactive**: Activate customer first
- **Product not available**: Check product is active

### Can't View Balance

- **Account frozen**: Contact administrator
- **Permission denied**: Request access

### Can't Close Account

- **Non-zero balance**: Transfer remaining funds
- **Pending transactions**: Wait for completion

---

## Support

For assistance:
1. Contact your supervisor
2. Reach out to system administrator
3. Check internal procedures
