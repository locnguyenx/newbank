# Cash Management Module - User Guide

**Date:** 2026-03-24  
**Version:** 1.0  
**Status:** Complete  

## Overview

The Cash Management module provides comprehensive cash management services for corporate and SME banking, including payroll processing, receivables management, liquidity optimization, batch payments, and auto-collection.

## Getting Started

### Accessing Cash Management

1. Log in to the banking portal
2. Navigate to **Cash Management** in the sidebar
3. Select the desired service:
   - **Payroll** - Manage payroll processing
   - **Liquidity** - View cash positions
   - **Receivables** - Manage invoices
   - **Batch Payments** - Process bulk payments
   - **Auto-Collection** - Configure automatic collections

## Features

### Payroll Management

#### Viewing Payroll Batches

1. Navigate to **Cash Management > Payroll**
2. The system displays all payroll batches with:
   - Batch Reference
   - Status (Pending, Processing, Completed, Failed)
   - Record Count
   - Total Amount
   - Currency
   - Payment Date

#### Creating Payroll Batches

1. Click **Create Payroll Batch**
2. Upload a CSV file containing employee payment details
3. System validates:
   - File format
   - Employee bank account information
   - Payment amounts
4. Upon successful validation, batch status changes to **Pending**

#### Processing Payroll

1. System automatically verifies:
   - Customer exists
   - Sufficient funds in funding account
   - Employee account validity
2. Batch status updates to **Processing**
3. Payment file generated in ACH/SEPA format
4. Status changes to **Completed** or **Failed**

### Liquidity Management

#### Viewing Cash Position

1. Navigate to **Cash Management > Liquidity**
2. Dashboard displays:
   - Total Balance
   - Account Count
   - Last Updated
   - Balance by currency

#### Cash Position History

1. View historical cash positions
2. Filter by date range
3. Export data for analysis

### Receivables Management

#### Viewing Invoices

1. Navigate to **Cash Management > Receivables**
2. View all invoices with:
   - Invoice Number
   - Amount
   - Currency
   - Status (Draft, Issued, Partially Paid, Paid, Overdue)
   - Due Date

#### Creating Invoices

1. Click **Create Invoice**
2. Enter invoice details:
   - Customer
   - Amount
   - Currency
   - Due Date
   - Description
3. System generates invoice number
4. Status defaults to **Draft**

#### Issuing Invoices

1. Select an invoice in **Draft** status
2. Click **Issue Invoice**
3. Status changes to **Issued**
4. Customer receives notification

### Batch Payments

#### Viewing Batch Payments

1. Navigate to **Cash Management > Batch Payments**
2. View all batch payments with:
   - Batch Reference
   - Status (Pending, Processing, Completed, Failed)
   - File Format
   - Instruction Count
   - Total Amount
   - Currency
   - Payment Date

#### Creating Batch Payments

1. Click **Create Batch Payment**
2. Upload payment file (CSV, ISO 20022)
3. System validates:
   - File format
   - Beneficiary information
   - Payment limits
4. Batch status changes to **Pending**

#### Processing Batch Payments

1. System checks:
   - Customer validity
   - Payment limits
   - Available funds
2. Payment instructions processed individually
3. Status updates to **Processing** then **Completed**

### Auto-Collection

#### Viewing Rules

1. Navigate to **Cash Management > Auto-Collection**
2. View all collection rules with:
   - Rule Name
   - Trigger Type
   - Amount Type
   - Status (Active/Inactive)

#### Creating Rules

1. Click **Create Rule**
2. Configure rule details:
   - Rule Name
   - Customer
   - Trigger Condition (On Invoice Due, Scheduled, Balance Threshold)
   - Collection Amount Type (Full, Partial, Fixed)
   - Funding Account
   - Pre-notification Days
   - Retry Attempts
3. Rule activates automatically

#### Managing Rules

- **Activate Rule** - Enable automatic collection
- **Deactivate Rule** - Disable automatic collection

## Security

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| Company Admin | Full access to all functions |
| Company Maker | Create payroll, payments, invoices |
| Company Checker | View and approve |
| Company Viewer | Read-only access |
| Department Maker | Create within department |
| Department Checker | View and approve within department |
| Department Viewer | Read-only within department |

### Audit Trail

All actions are logged with:
- User who performed the action
- Timestamp
- Action type
- Before/after states

## Error Handling

### Common Error Codes

| Code | Description | Action |
|------|-------------|--------|
| CAS_001 | Payroll batch not found | Verify batch reference |
| CAS_002 | Invoice not found | Verify invoice number |
| CAS_003 | Batch payment not found | Verify batch reference |
| CAS_004 | Auto-collection rule not found | Verify rule ID |
| CAS_005 | Insufficient funds | Add funds to account |
| CAS_006 | Invalid customer | Verify customer ID |

### Error Messages

- **Invalid customer** - Customer does not exist in system
- **Insufficient funds** - Funding account has insufficient balance
- **Invalid file format** - Uploaded file does not match expected format
- **Payment limit exceeded** - Amount exceeds user/account limit

## Integration Points

### Account Module

- Balance verification
- Account validation
- Transaction posting

### Customer Module

- Customer validation
- Customer data retrieval
- Address verification

### Limits Module

- Payment limit checking
- Authorization threshold verification
- Dual control requirements

### Charges Module

- Fee calculation
- Charge application
- Waiver processing

## Best Practices

### Payroll Processing

1. Upload files during off-peak hours
2. Verify employee account information
3. Ensure sufficient funds before processing
4. Monitor batch status regularly

### Receivables Management

1. Issue invoices promptly
2. Set clear payment terms
3. Monitor overdue invoices
4. Configure dunning rules

### Batch Payments

1. Validate files before upload
2. Check payment limits
3. Use appropriate file formats
4. Monitor processing status

### Auto-Collection

1. Set realistic retry parameters
2. Configure pre-notification
3. Monitor collection success rates
4. Adjust rules based on performance

## Troubleshooting

### Batch Not Processing

1. Check batch status
2. Verify customer validity
3. Ensure sufficient funds
4. Check payment limits

### Invoice Not Issuing

1. Verify invoice is in Draft status
2. Check customer information
3. Ensure all required fields are filled
4. Verify due date is future

### Collection Failed

1. Check funding account balance
2. Verify rule is active
3. Review retry attempts
4. Check for system errors

## Support

For issues or questions:

1. Check error messages for specific details
2. Verify all required information is provided
3. Contact your system administrator
4. Refer to API documentation for technical details

---

**Document Version:** 1.0  
**Last Updated:** 2026-03-24  
**Next Review:** After Phase 2 implementation
