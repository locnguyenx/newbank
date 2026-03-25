# Limits Management Module - User Guide

**Module:** `com.banking.limits`  
**Version:** 1.0  
**Date:** 2026-03-24

---

## Overview

The Limits Management module provides control over transaction limits, authorization thresholds, and approval workflows. It ensures regulatory compliance and internal controls through dual-control mechanisms.

## Limit Types

| Type | Description |
|------|-------------|
| `TRANSACTION_LIMIT` | Maximum amount per transaction |
| `DAILY_LIMIT` | Maximum amount per day |
| `MONTHLY_LIMIT` | Maximum amount per month |
| `TRANSACTION_COUNT` | Maximum number of transactions |
| `BALANCE_LIMIT` | Maximum account balance |

## Limit Scope

| Scope | Description |
|-------|-------------|
| `GLOBAL` | System-wide limit |
| `CUSTOMER` | Customer-specific limit |
| `PRODUCT` | Product-specific limit |
| `CHANNEL` | Channel-specific limit |

## Limit Status

| Status | Description |
|--------|-------------|
| `DRAFT` | Not yet active |
| `ACTIVE` | Enforced |
| `INACTIVE` | Not enforced |
| `EXPIRED` | Past validity end date |

---

## API Reference

### Authentication

All endpoints require a valid JWT token:
```
Authorization: Bearer <token>
```

### Limit Definitions

#### Create Limit Definition

```http
POST /api/limits/definitions
Content-Type: application/json

{
  "name": "Wire Transfer Daily Limit",
  "type": "DAILY_LIMIT",
  "scope": "CUSTOMER",
  "currency": "USD",
  "maxAmount": 100000,
  "minAmount": 0,
  "priority": 1,
  "description": "Daily limit for wire transfers"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Wire Transfer Daily Limit",
  "type": "DAILY_LIMIT",
  "scope": "CUSTOMER",
  "currency": "USD",
  "maxAmount": 100000,
  "minAmount": 0,
  "status": "ACTIVE",
  "createdAt": "2026-03-24T10:00:00Z"
}
```

#### List Limit Definitions

```http
GET /api/limits/definitions?status=ACTIVE
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `status` | String | Filter by status |

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Wire Transfer Daily Limit",
    "type": "DAILY_LIMIT",
    "scope": "CUSTOMER",
    "currency": "USD",
    "maxAmount": 100000,
    "status": "ACTIVE"
  }
]
```

#### Get Limit Definition

```http
GET /api/limits/definitions/{id}
```

#### Update Limit Definition

```http
PUT /api/limits/definitions/{id}
Content-Type: application/json

{
  "name": "Wire Transfer Daily Limit Updated",
  "maxAmount": 150000
}
```

#### Activate Limit

```http
PUT /api/limits/definitions/{id}/activate
```

#### Deactivate Limit

```http
PUT /api/limits/definitions/{id}/deactivate
```

---

### Limit Assignments

#### Assign Limit to Customer

```http
POST /api/limits/assignments
Content-Type: application/json

{
  "customerId": 1,
  "limitDefinitionId": 1,
  "maxAmount": 50000,
  "validFrom": "2026-03-24",
  "validTo": "2027-03-24"
}
```

#### List Assignments

```http
GET /api/limits/assignments?customerId=1
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `customerId` | Long | Filter by customer |
| `limitDefinitionId` | Long | Filter by limit definition |

#### Get Assignment

```http
GET /api/limits/assignments/{id}
```

#### Update Assignment

```http
PUT /api/limits/assignments/{id}
Content-Type: application/json

{
  "maxAmount": 75000
}
```

---

### Limit Checking

#### Check Transaction Limit

```http
POST /api/limits/check
Content-Type: application/json

{
  "customerId": 1,
  "accountNumber": "ACC-000001-USD",
  "transactionType": "WIRE_TRANSFER",
  "amount": 25000,
  "currency": "USD"
}
```

**Response:** `200 OK`
```json
{
  "approved": true,
  "limitDefinitionId": 1,
  "remainingAmount": 25000,
  "message": "Transaction within limits"
}
```

**If limit exceeded:**
```json
{
  "approved": false,
  "limitDefinitionId": 1,
  "remainingAmount": 0,
  "message": "Daily limit exceeded",
  "requiresApproval": true,
  "approvers": ["supervisor@bank.com"]
}
```

---

### Approval Workflow

#### Submit for Approval

```http
POST /api/limits/approvals
Content-Type: application/json

{
  "customerId": 1,
  "transactionType": "WIRE_TRANSFER",
  "amount": 120000,
  "currency": "USD",
  "reason": "Vendor payment"
}
```

**Response:** `201 Created`
```json
{
  "approvalId": "APR-001",
  "status": "PENDING",
  "requiredApprovers": 1,
  "currentApprovals": 0,
  "createdAt": "2026-03-24T10:00:00Z"
}
```

#### List Approvals

```http
GET /api/limits/approvals?status=PENDING
```

#### Approve Request

```http
POST /api/limits/approvals/{id}/approve
Content-Type: application/json

{
  "comment": "Approved - verified with client"
}
```

#### Reject Request

```http
POST /api/limits/approvals/{id}/reject
Content-Type: application/json

{
  "comment": "Rejected - insufficient documentation"
}
```

---

## Dual Control

The system enforces dual control for high-value transactions:

1. **Transaction below threshold** - Processed automatically
2. **Transaction above threshold** - Requires approval
3. **Transaction above dual-control limit** - Requires two approvers

### Threshold Configuration

| Threshold | Amount | Requirement |
|-----------|--------|-------------|
| Auto-approve | < $10,000 | No approval |
| Single approval | $10,000 - $50,000 | 1 approver |
| Dual approval | > $50,000 | 2 approvers |

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `LIM-001` | 404 | Limit definition not found |
| `LIM-002` | 400 | Limit exceeded |
| `LIM-003` | 400 | Invalid amount (below minimum) |
| `LIM-004` | 400 | Assignment not found |
| `LIM-005` | 400 | Approval not found |
| `LIM-006` | 400 | Already approved/rejected |
| `LIM-007` | 400 | Expired limit assignment |

---

## Integration Points

### Account Module
- Validates account status before limit check
- Retrieves account balance for limit calculation

### Customer Module
- Validates customer exists and is active
- Retrieves customer risk rating

### Product Module
- Fetches product-specific limits
- Validates product eligibility

### Charges Module
- Applies excess usage charges

---

## Best Practices

1. **Limit Definition**
   - Set appropriate min/max amounts
   - Use clear naming conventions
   - Document limit purpose

2. **Limit Assignment**
   - Set appropriate validity periods
   - Review assignments regularly
   - Align with customer risk profile

3. **Approval Workflow**
   - Respond to approval requests promptly
   - Provide meaningful comments
   - Maintain audit trail

4. **Monitoring**
   - Review limit utilization
   - Adjust limits based on usage patterns
   - Monitor approval queue

---

## Support

For issues or questions:
1. Verify limit definition is active
2. Check assignment validity dates
3. Review approval status
4. Contact system administrator
