# Charges & Fees Management Module - User Guide

**Module:** `com.banking.charges`  
**Version:** 1.0  
**Date:** 2026-03-24

---

## Overview

The Charges & Fees Management module handles all banking charges, fees, interest rates, and waivers. It supports flexible pricing structures including flat fees, percentage-based fees, and tiered pricing.

## Charge Types

| Type | Description |
|------|-------------|
| `ACCOUNT_MAINTENANCE` | Monthly/annual account fees |
| `TRANSACTION_FEE` | Per-transaction charges |
| `WIRE_TRANSFER` | Domestic/international transfers |
| `ATM_WITHDRAWAL` | Cash withdrawal fees |
| `CARD_ISSUANCE` | New card fees |
| `CARD_MAINTENANCE` | Annual card fees |
| `OVERDRAFT_INTEREST` | Interest on overdraft |
| `LOAN_INTEREST` | Interest on loans |
| `LATE_PAYMENT` | Penalty for late payments |
| `FOREIGN_EXCHANGE` | FX transaction fees |

## Calculation Methods

| Method | Description |
|--------|-------------|
| `FLAT` | Fixed amount per transaction |
| `PERCENTAGE` | Percentage of transaction amount |
| `TIERED` | Rate varies by transaction volume |

## Charge Status

| Status | Description |
|--------|-------------|
| `DRAFT` | Not yet active |
| `ACTIVE` | Applied to transactions |
| `INACTIVE` | Not applied |

---

## API Reference

### Authentication

All endpoints require a valid JWT token:
```
Authorization: Bearer <token>
```

### Charge Definitions

#### Create Charge Definition

```http
POST /api/charges/definitions
Content-Type: application/json

{
  "name": "Wire Transfer Fee",
  "chargeType": "WIRE_TRANSFER",
  "calculationMethod": "FLAT",
  "currency": "USD",
  "amount": 25.00,
  "description": "Fee for outgoing wire transfers"
}
```

**For percentage-based charges:**
```json
{
  "name": "FX Transaction Fee",
  "chargeType": "FOREIGN_EXCHANGE",
  "calculationMethod": "PERCENTAGE",
  "currency": "USD",
  "percentage": 0.5,
  "minAmount": 10.00,
  "maxAmount": 100.00
}
```

**For tiered pricing:**
```json
{
  "name": "Volume Discount Tier",
  "chargeType": "TRANSACTION_FEE",
  "calculationMethod": "TIERED",
  "currency": "USD",
  "tiers": [
    { "tierFrom": 0, "tierTo": 1000, "rate": 2.00 },
    { "tierFrom": 1001, "tierTo": 5000, "rate": 1.50 },
    { "tierFrom": 5001, "tierTo": null, "rate": 1.00 }
  ]
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Wire Transfer Fee",
  "chargeType": "WIRE_TRANSFER",
  "calculationMethod": "FLAT",
  "currency": "USD",
  "amount": 25.00,
  "status": "ACTIVE",
  "createdAt": "2026-03-24T10:00:00Z"
}
```

#### List Charge Definitions

```http
GET /api/charges/definitions?chargeType=WIRE_TRANSFER&status=ACTIVE
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `chargeType` | String | Filter by charge type |
| `status` | String | Filter by status |

#### Get Charge Definition

```http
GET /api/charges/definitions/{id}
```

#### Update Charge Definition

```http
PUT /api/charges/definitions/{id}
Content-Type: application/json

{
  "amount": 30.00,
  "description": "Updated fee for outgoing wire transfers"
}
```

#### Activate Charge

```http
PUT /api/charges/definitions/{id}/activate
```

#### Deactivate Charge

```http
PUT /api/charges/definitions/{id}/deactivate
```

---

### Charge Calculation

#### Calculate Charge

```http
POST /api/charges/calculate
Content-Type: application/json

{
  "chargeType": "WIRE_TRANSFER",
  "customerId": 1,
  "accountNumber": "ACC-000001-USD",
  "transactionAmount": 10000,
  "currency": "USD"
}
```

**Response:** `200 OK`
```json
{
  "chargeType": "WIRE_TRANSFER",
  "calculatedAmount": 25.00,
  "currency": "USD",
  "breakdown": {
    "baseFee": 25.00,
    "tax": 0,
    "total": 25.00
  }
}
```

---

### Interest Rates

#### Create Interest Rate

```http
POST /api/charges/interest-rates
Content-Type: application/json

{
  "name": "Savings Account Interest",
  "rateType": "SAVINGS_INTEREST",
  "interestRate": 3.5,
  "currency": "USD",
  "effectiveFrom": "2026-01-01"
}
```

**Response:** `201 Created`

#### List Interest Rates

```http
GET /api/charges/interest-rates?rateType=SAVINGS_INTEREST
```

#### Update Interest Rate

```http
PUT /api/charges/interest-rates/{id}
Content-Type: application/json

{
  "interestRate": 4.0
}
```

---

### Fee Waivers

#### Create Fee Waiver

```http
POST /api/charges/waivers
Content-Type: application/json

{
  "customerId": 1,
  "chargeType": "WIRE_TRANSFER",
  "waiverType": "PERMANENT",
  "discountPercentage": 100,
  "validFrom": "2026-01-01",
  "validTo": "2026-12-31",
  "reason": "VIP Customer"
}
```

**Response:** `201 Created`

#### List Waivers

```http
GET /api/charges/waivers?customerId=1
```

#### Get Waiver

```http
GET /api/charges/waivers/{id}
```

#### Update Waiver

```http
PUT /api/charges/waivers/{id}
Content-Type: application/json

{
  "discountPercentage": 50
}
```

#### Revoke Waiver

```http
DELETE /api/charges/waivers/{id}
```

---

### Charge Assignments

#### Assign Charge to Customer

```http
POST /api/charges/assignments
Content-Type: application/json

{
  "customerId": 1,
  "chargeDefinitionId": 1,
  "customAmount": 20.00,
  "validFrom": "2026-03-24"
}
```

#### List Assignments

```http
GET /api/charges/assignments?customerId=1
```

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `CHG-001` | 404 | Charge definition not found |
| `CHG-002` | 400 | Invalid calculation method |
| `CHG-003` | 400 | Charge is inactive |
| `CHG-004` | 400 | Waiver not found |
| `CHG-005` | 400 | Waiver expired |
| `CHG-006` | 400 | Assignment not found |

---

## Pricing Examples

### Example 1: Flat Fee
```json
{
  "name": "Wire Transfer Fee",
  "calculationMethod": "FLAT",
  "amount": 25.00
}
```
- $5,000 transfer: $25.00
- $10,000 transfer: $25.00
- $100,000 transfer: $25.00

### Example 2: Percentage Fee
```json
{
  "name": "FX Commission",
  "calculationMethod": "PERCENTAGE",
  "percentage": 0.5,
  "minAmount": 10.00,
  "maxAmount": 100.00
}
```
- €1,000 exchange: €10.00 (min)
- €5,000 exchange: €25.00
- €50,000 exchange: €100.00 (max)

### Example 3: Tiered Pricing
```json
{
  "name": "Transaction Volume Discount",
  "calculationMethod": "TIERED",
  "tiers": [
    { "tierFrom": 0, "tierTo": 1000, "rate": 2.00 },
    { "tierFrom": 1001, "tierTo": 5000, "rate": 1.50 },
    { "tierFrom": 5001, "tierTo": null, "rate": 1.00 }
  ]
}
```
- 500 transactions: 500 × $2.00 = $1,000
- 3,000 transactions: 3,000 × $1.50 = $4,500
- 10,000 transactions: 10,000 × $1.00 = $10,000

---

## Integration Points

### Account Module
- Applies charges to account balance
- Calculates overdraft interest

### Customer Module
- Customer-specific pricing
- Waivers based on customer tier

### Product Module
- Product-specific charges
- Default pricing from product

### Limits Module
- Excess usage charges

---

## Best Practices

1. **Charge Definition**
   - Use clear, descriptive names
   - Set appropriate min/max amounts for percentages
   - Document charge purpose

2. **Tiered Pricing**
   - Ensure tiers are contiguous (no gaps)
   - Set reasonable break points
   - Test calculations thoroughly

3. **Waivers**
   - Set appropriate validity periods
   - Document waiver reasons
   - Review expiring waivers

4. **Interest Rates**
   - Update rates promptly
   - Maintain audit trail
   - Consider regulatory requirements

---

## Support

For issues or questions:
1. Verify charge definition is active
2. Check for applicable waivers
3. Review calculation method
4. Contact system administrator
