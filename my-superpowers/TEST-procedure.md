## OpenAI spec

### Customer Module (/api/customers)

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /api/customers | List all customers (requires Pageable) |
| GET | /api/customers/{id} | Get customer by ID |
| GET | /api/customers/search | Search customers |
| POST | /api/customers/corporate | Create corporate customer |
| POST | /api/customers/sme | Create SME customer |
| POST | /api/customers/individual | Create individual customer
| PUT | /api/customers/{id} | Update customer |

### Account Module (/api/accounts)

Method | Endpoint | Description
GET | /api/accounts | List accounts (with filters)
GET | /api/accounts/{accountNumber} | Get account details
GET | /api/accounts/{accountNumber}/balance | Get account balance
GET | /api/accounts/{accountNumber}/statement | Get account statement
POST | /api/accounts | Open account
PUT | /api/accounts/{accountNumber}/close | Close account
PUT | /api/accounts/{accountNumber}/freeze | Freeze account
PUT | /api/accounts/{accountNumber}/unfreeze | Unfreeze account
POST | /api/accounts/{accountNumber}/holders | Add account holder
DELETE | /api/accounts/{accountNumber}/holders/{holderId} | Remove holder

### Product Module (/api/products)

Method | Endpoint | Description
GET | /api/products | List products
GET | /api/products/{id} | Get product by ID
GET | /api/products/{id}/detail | Get product detail
GET | /api/products/code/{code} | Get product by code
GET | /api/products/search | Search products
POST | /api/products | Create product
PUT | /api/products/{id} | Update product
GET | /api/products/{productId}/versions | List versions
GET | /api/products/{productId}/versions/{versionId} | Get version detail
POST | /api/products/{productId}/versions/{versionId}/submit | Submit for approval
POST | /api/products/{productId}/versions/{versionId}/approve | Approve version
POST | /api/products/{productId}/versions/{versionId}/reject | Reject version
POST | /api/products/{productId}/versions/{versionId}/activate | Activate version
POST | /api/products/{productId}/versions/{versionId}/retire | Retire version
GET | /api/products/{productId}/versions/compare | Compare versions

**Product Query (/api/product-query)**

Method | Endpoint | Description
GET | /api/product-query/active?code=X | Get active product
GET | /api/product-query/version/{versionId} | Get version by ID
GET | /api/product-query/family/{family} | Get products by family
GET | /api/product-query/customer-type/{type} | Get products by customer type

### Master Data

**Currencies (/api/master-data/currencies)**
Method | Endpoint | Description
GET | /api/master-data/currencies | List currencies
GET | /api/master-data/currencies/{code} | Get currency
POST | /api/master-data/currencies | Create currency
PUT | /api/master-data/currencies/{code} | Update currency
PUT | /api/master-data/currencies/{code}/deactivate | Deactivate

**Countries (/api/master-data/countries)**
Method | Endpoint | Description
GET | /api/master-data/countries | List countries
GET | /api/master-data/countries/{isoCode} | Get country
POST | /api/master-data/countries | Create country
PUT | /api/master-data/countries/{isoCode}/deactivate | Deactivate

**Branches (/api/master-data/branches)**
Method | Endpoint | Description
GET | /api/master-data/branches | List branches
GET | /api/master-data/branches/{code} | Get branch
POST | /api/master-data/branches | Create branch
PUT | /api/master-data/branches/{code}/deactivate | Deactivate

**Channels (/api/master-data/channels)**
Method | Endpoint | Description
GET | /api/master-data/channels | List channels
POST | /api/master-data/channels | Create channel
PUT | /api/master-data/channels/{code}/deactivate | Deactivate

**Industries (/api/master-data/industries)**
Method | Endpoint | Description
GET | /api/master-data/industries | List industries
GET | /api/master-data/industries/{code} | Get industry
POST | /api/master-data/industries | Create industry

**Document Types (/api/master-data/document-types)**
Method | Endpoint | Description
GET | /api/master-data/document-types | List document types
POST | /api/master-data/document-types | Create document type
PUT | /api/master-data/document-types/{code}/deactivate | Deactivate

**Exchange Rates (/api/master-data/exchange-rates)**
Method | Endpoint | Description
GET | /api/master-data/exchange-rates/latest | Get latest rate
GET | /api/master-data/exchange-rates/convert | Convert amount
POST | /api/master-data/exchange-rates | Create exchange rate

### Limits Module (/api/limits)

**Definitions (/api/limits/definitions)**
Method | Endpoint | Description
GET | /api/limits/definitions | List limit definitions
GET | /api/limits/definitions/{id} | Get limit definition
POST | /api/limits/definitions | Create limit
PUT | /api/limits/definitions/{id} | Update limit
PUT | /api/limits/definitions/{id}/activate | Activate
PUT | /api/limits/definitions/{id}/deactivate | Deactivate

**Check (/api/limits/check)**
Method | Endpoint | Description
POST | /api/limits/check | Check limit
GET | /api/limits/check/effective | Get effective limits

**Assignments (/api/limits/assignments)**
Method | Endpoint | Description
POST | /api/limits/assignments/product | Assign to product
POST | /api/limits/assignments/customer | Assign to customer
POST | /api/limits/assignments/account | Assign to account
GET | /api/limits/assignments/product/{code} | Get product limits
GET | /api/limits/assignments/customer/{id} | Get customer limits
GET | /api/limits/assignments/account/{num} | Get account limits

**Approvals (/api/limits/approvals)**
Method | Endpoint | Description
GET | /api/limits/approvals/pending | Get pending approvals
POST | /api/limits/approvals/{id}/approve | Approve
POST | /api/limits/approvals/{id}/reject | Reject

### Charges Module (/api/charges)

Method | Endpoint | Description
POST | /api/charges/calculate | Calculate charge

**Definitions (/api/charges/definitions)**
Method | Endpoint | Description
GET | /api/charges/definitions | List charge definitions
GET | /api/charges/definitions/{id} | Get charge
POST | /api/charges/definitions | Create charge
PUT | /api/charges/definitions/{id} | Update charge
PUT | /api/charges/definitions/{id}/activate | Activate
PUT | /api/charges/definitions/{id}/deactivate | Deactivate

**Rules (/api/charges/definitions/{chargeId}/rules)**
Method | Endpoint | Description
GET | /api/charges/definitions/{chargeId}/rules | List rules
POST | /api/charges/definitions/{chargeId}/rules | Add rule
PUT | /api/charges/definitions/{chargeId}/rules/{ruleId} | Update rule
DELETE | /api/charges/definitions/{chargeId}/rules/{ruleId} | Remove rule

**Assignments (/api/charges/assignments)**
Method | Endpoint | Description
POST | /api/charges/assignments/product | Assign to product
POST | /api/charges/assignments/customer | Assign to customer
DELETE | /api/charges/assignments/product | Unassign from product
DELETE | /api/charges/assignments/customer | Unassign from customer
GET | /api/charges/assignments/product/{productCode} | Get product charges
GET | /api/charges/assignments/customer/{customerId} | Get customer overrides

**Interest (/api/charges/interest)**
Method | Endpoint | Description
GET | /api/charges/interest/{id} | Get interest rate
GET | /api/charges/interest/product/{productCode} | Get by product
POST | /api/charges/interest | Create interest rate
PUT | /api/charges/interest/{id} | Update interest rate

**Waivers (/api/charges/waivers)**
Method | Endpoint | Description
GET | /api/charges/waivers | List waivers
GET | /api/charges/waivers/applicable | Get applicable waivers
POST | /api/charges/waivers | Create waiver
DELETE | /api/charges/waivers/{id} | Remove waiver

### KYC (/api/kyc)

Method | Endpoint | Description
GET | /api/kyc/{id} | Get KYC check
POST | /api/kyc | Initiate KYC
POST | /api/kyc/{id}/documents | Submit documents
POST | /api/kyc/{id}/submit | Submit for review
POST | /api/kyc/{id}/approve | Approve KYC
POST | /api/kyc/{id}/reject | Reject KYC
PUT | /api/kyc/{id} | Update KYC

### Employment (/api/employments)

Method | Endpoint | Description
GET | /api/employments/{id} | Get employment
POST | /api/employments | Create employment
POST | /api/employments/bulk | Bulk upload

### Authorizations (/api/authorizations)

Method | Endpoint | Description
GET | /api/authorizations/{id} | Get authorization
GET | /api/authorizations/customer/{customerId} | Get by customer
POST | /api/authorizations | Create authorization
PUT | /api/authorizations/{id} | Update authorization
POST | /api/authorizations/{id}/revoke | Revoke

---
Note: Most endpoints require X-Username header for audit. The API returns paginated responses for list endpoints.

## Manual API Testing (cURL)

You need to quote the URL to escape the ?:
curl -H "X-Username: system" "http://localhost:8080/api/customers?page=0&size=100"

### Customers
curl -H "X-Username: system" "http://localhost:8080/api/customers?page=0&size=100"

### Accounts  
curl -H "X-Username: system" "http://localhost:8080/api/accounts?page=0&size=100"

### Products (already has seed data)
curl -H "X-Username: system" "http://localhost:8080/api/products/search"