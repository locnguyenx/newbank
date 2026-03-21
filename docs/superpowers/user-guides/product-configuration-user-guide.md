# Product Configuration Module — User Guide

**Module:** `com.banking.product`
**Version:** 1.0
**Date:** 2026-03-21

---

## Overview

The Product Configuration module provides a centralized, versioned product catalog for the Corporate & SME Banking System. It enables bank administrators to create, manage, and govern banking products — such as current accounts, savings accounts, loans, and payment products — without code deployments.

Key capabilities:

- **Versioned products** — every change to an active product creates a new draft version; originals are preserved
- **Maker-checker approval** — changes require separate submit and approve steps by different users
- **Feature toggles** — define product capabilities (e.g., overdraft enabled, cheque book available)
- **Tiered pricing** — configure flat fees, percentage fees, or volume-based tiered fee schedules
- **Customer segment targeting** — assign products to CORPORATE, SME, or INDIVIDUAL customers
- **Full audit trail** — every action is logged with actor and timestamp

---

## Product Families

Products belong to one of three families:

| Family | Description |
|--------|-------------|
| `ACCOUNT` | Deposit and lending products (current, savings, fixed deposit, loan) |
| `PAYMENT` | Payment and transfer products (domestic, international, bulk payments) |
| `TRADE_FINANCE` | Trade finance products (letters of credit, guarantees, documentary credits) |

---

## Product Lifecycle

Each product moves through a defined lifecycle:

```
DRAFT → PENDING_APPROVAL → APPROVED → ACTIVE → RETIRED
              ↓ (reject)
            DRAFT
```

| Status | Description |
|--------|-------------|
| `DRAFT` | Product is being configured. Fully editable. |
| `PENDING_APPROVAL` | Submitted for review. Not editable. Awaiting checker approval. |
| `APPROVED` | Reviewed and approved. Awaiting activation. |
| `ACTIVE` | Live and available for use. One active version per product at a time. |
| `SUPERSEDED` | Replaced by a newer active version. Remains for historical contracts. |
| `RETIRED` | Permanently withdrawn. Cannot be used for new contracts. |

**Rules:**

- Only `DRAFT` versions are editable
- Submitting a draft moves it to `PENDING_APPROVAL`
- Approving moves it to `APPROVED`; rejecting returns it to `DRAFT`
- Activating an `APPROVED` version makes it live — any previously active version is marked `SUPERSEDED`
- Only `APPROVED` versions can be activated
- Retiring is blocked if live contracts reference the version

---

## User Roles

Roles are passed via the `X-Username` header (from the auth module). Two roles are enforced:

| Role | Actions |
|------|---------|
| **Maker** | Create product, edit draft, add features/pricing/segments, submit for approval |
| **Checker** | Approve or reject pending products |
| **Admin** | Activate approved products, retire active products |

**Enforcement:** The same user cannot submit a product and also approve it (maker-checker violation).

---

## API Reference

### Product Management

#### Create Product
```
POST /api/products
X-Username: <maker_username>
Content-Type: application/json

{
  "code": "BIZ-CURRENT",
  "name": "Business Current Account",
  "family": "ACCOUNT",
  "description": "Current account with overdraft for small businesses"
}
```

- Creates the product in `DRAFT` status with version 1
- Codes must be unique and are immutable after creation

#### Get Product (flat summary)
```
GET /api/products/{productId}
```
Returns basic product info. For full details, use the detail endpoint.

#### Get Product Detail
```
GET /api/products/{productId}/detail
```
Returns the product with current version details, features, fee entries, and segments.

#### Get Product by Code
```
GET /api/products/code/{code}
```

#### Update Product (Draft Only)
```
PUT /api/products/{productId}
X-Username: <maker_username>
Content-Type: application/json

{
  "name": "Business Current Account v2",
  "description": "Updated description"
}
```

- Only works if the current version is `DRAFT`
- Editing a non-draft product creates a new draft version (existing version untouched)

#### Search Products
```
GET /api/products/search?family=ACCOUNT&status=ACTIVE&page=0&size=20
```
Paginated search with optional filters for family, status, and customer type.

---

### Version Lifecycle

#### Submit for Approval
```
POST /api/products/{productId}/versions/{versionId}/submit
X-Username: <maker_username>
```
Moves the version from `DRAFT` → `PENDING_APPROVAL`.

#### Approve
```
POST /api/products/{productId}/versions/{versionId}/approve
X-Username: <checker_username>
```
Moves the version from `PENDING_APPROVAL` → `APPROVED`.

#### Reject
```
POST /api/products/{productId}/versions/{versionId}/reject
X-Username: <checker_username>
Content-Type: application/json

{
  "comment": "Missing required fee schedule"
}
```
Moves the version back to `DRAFT`. Rejection comment is mandatory.

#### Activate
```
POST /api/products/{productId}/versions/{versionId}/activate
X-Username: <admin_username>
```
Moves from `APPROVED` → `ACTIVE`. The previous active version becomes `SUPERSEDED`.

#### Retire
```
POST /api/products/{productId}/versions/{versionId}/retire
X-Username: <admin_username>
```
Moves from `ACTIVE` → `RETIRED`. Blocked if `contractCount > 0`.

#### Version History
```
GET /api/products/{productId}/versions
```
Returns all versions for a product, ordered by version number descending.

#### Compare Two Versions
```
GET /api/products/{productId}/versions/compare?v1=1&v2=2
```
Returns a diff of features, fee entries, and segments between two versions.

---

### Features

Features are key-value pairs that define product capabilities. Keys are namespaced by family (e.g., `account.overdraft_enabled`, `payment.instant_transfer`).

#### Add Feature
```
POST /api/products/{productId}/versions/{versionId}/features
X-Username: <maker_username>
Content-Type: application/json

{
  "featureKey": "account.overdraft_enabled",
  "featureValue": "true"
}
```

#### Update Feature
```
PUT /api/products/{productId}/versions/{versionId}/features/{featureId}
X-Username: <maker_username>
Content-Type: application/json

{
  "featureKey": "account.overdraft_enabled",
  "featureValue": "false"
}
```

#### Remove Feature
```
DELETE /api/products/{productId}/versions/{versionId}/features/{featureId}
X-Username: <maker_username>
```

#### List Features
```
GET /api/products/{productId}/versions/{versionId}/features
```

---

### Pricing

Fee entries define the cost structure for a product. Each fee entry has a calculation method:

| Method | Description |
|--------|-------------|
| `FLAT` | Fixed amount per transaction or period |
| `PERCENTAGE` | Percentage of transaction value |
| `TIERED_VOLUME` | Rate changes based on volume breakpoints |

For `TIERED_VOLUME`, define tiers with `tierFrom`, `tierTo`, and `rate`.

#### Add Fee Entry
```
POST /api/products/{productId}/versions/{versionId}/fees
X-Username: <maker_username>
Content-Type: application/json

{
  "feeType": "MONTHLY_MAINTAINENCE",
  "calculationMethod": "FLAT",
  "currency": "USD",
  "amount": 25.00
}
```

For tiered pricing:
```json
{
  "feeType": "TRANSACTION_FEE",
  "calculationMethod": "TIERED_VOLUME",
  "currency": "USD",
  "tiers": [
    { "tierFrom": 0, "tierTo": 100, "rate": 0.00 },
    { "tierFrom": 101, "tierTo": 500, "rate": 0.50 },
    { "tierFrom": 501, "tierTo": null, "rate": 0.25 }
  ]
}
```

#### Remove Fee Entry
```
DELETE /api/products/{productId}/versions/{versionId}/fees/{feeId}
X-Username: <maker_username>
```

#### List Fee Entries
```
GET /api/products/{productId}/versions/{versionId}/fees
```

---

### Customer Segments

Assign eligible customer types to a product version.

#### Assign Segments
```
POST /api/products/{productId}/versions/{versionId}/segments
X-Username: <maker_username>
Content-Type: application/json

{
  "segments": ["CORPORATE", "SME"]
}
```
Replaces all existing segments for the version.

#### Get Segments
```
GET /api/products/{productId}/versions/{versionId}/segments
```
Returns the list of customer types assigned to the version.

---

### Downstream Query (for other modules)

#### Get Active Product by Code
```
GET /api/products/active/by-code/{code}
```
Returns the active version of a product by its code. Used at account opening to resolve product binding.

#### Get Active Products by Family
```
GET /api/products/active?family=ACCOUNT
```
Returns all active products in a given family.

#### Get Active Products by Customer Type
```
GET /api/products/active/by-customer-type?customerType=CORPORATE
```
Returns all active products available to a specific customer type.

#### Get Product Version by ID
```
GET /api/products/versions/{versionId}
```
Returns a specific version by ID regardless of status. Used to resolve historical product config for existing contracts.

#### Get Active Products (General)
```
GET /api/products/active
```
Returns all active products across all families.

---

### Audit Trail

#### Get Audit Logs
```
GET /api/products/{productId}/audit
```
Returns all audit log entries for a product, ordered by timestamp descending.

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `PROD-001` | 404 | Product not found |
| `PROD-002` | 404 | Product version not found |
| `PROD-003` | 409 | Duplicate product code |
| `PROD-004` | 422 | Invalid product status for requested action |
| `PROD-005` | 422 | Product version is not editable (not in DRAFT status) |
| `PROD-006` | 422 | Cannot retire: product has active contracts |
| `PROD-007` | 422 | Maker-checker violation: same user submitted and approved |
| `PROD-008` | 422 | Invalid feature key prefix |

---

## Seeded Data

The following account products are seeded as ACTIVE v1:

| Code | Name | Family |
|------|------|--------|
| `CURRENT` | Current Account | ACCOUNT |
| `SAVINGS` | Savings Account | ACCOUNT |
| `FIXED-DEPOSIT` | Fixed Deposit | ACCOUNT |
| `LOAN` | Loan Account | ACCOUNT |

---

## Account Integration

When opening a new account, the Account module resolves the product via `ProductQueryService`:

1. The account opening request includes `productCode` (e.g., `"CURRENT"`)
2. The system looks up the **active** version of that product
3. The account is bound to that specific `productVersionId`
4. The product name is stored on the account for display

This means accounts opened today always use the product configuration that was active at opening time, even if the product is later updated or superseded.

---

## Versioning Behavior

- **Creating** a product creates version 1 in DRAFT
- **Editing a DRAFT** updates it in place (no new version)
- **Editing a non-DRAFT** (ACTIVE, APPROVED, etc.) creates version N+1 in DRAFT
- **Original versions are never modified** after submission
- **Features and pricing are copied** to the new draft when editing
- **Accounts bind to the specific version** that was active when the account was opened

This ensures that all contracts and transactions can always be traced back to the exact product configuration that governed them.
