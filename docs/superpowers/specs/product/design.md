# Technical Design: Product Configuration

**Version:** 1.0
**Date:** 2026-03-20
**Status:** Draft
**Module:** Product Configuration (`com.banking.product`)
**BRD Reference:** `docs/superpowers/brds/2026-03-20-product-configuration.md`
**BDD Reference:** `docs/superpowers/specs/2026-03-20-product-configuration-bdd.md`

---

## 1. Architecture Overview

### Module Placement

Product Configuration is a **foundation module** in the modular monolith. It provides a shared product catalog consumed by business modules (Account, Payment, Trade Finance).

```
┌─────────────────────────────────────────────────┐
│                  Modular Monolith               │
│                                                 │
│  ┌──────────────┐  ┌──────────────┐             │
│  │   Customer   │  │   Account    │             │
│  │   Module     │  │   Module     │             │
│  └──────────────┘  └──────┬───────┘             │
│                           │ reads product       │
│  ┌──────────────┐  ┌──────▼───────┐             │
│  │  (Payments)  │  │   Product    │  ◄── Foundation │
│  │   (Phase 3)  │──│   Module     │      Module    │
│  └──────────────┘  └──────────────┘             │
│  ┌──────────────┐         ▲                     │
│  │ (Trade Fin.) │─────────┘                     │
│  │   (Phase 3)  │                               │
│  └──────────────┘                               │
└─────────────────────────────────────────────────┘
```

### Module Boundaries

- **Inbound:** Other modules query products via `ProductQueryService` (read-only interface)
- **Outbound:** Product module publishes lifecycle events (ProductActivatedEvent, ProductRetiredEvent)
- **No direct DB joins:** Other modules store `productVersionId` (Long) as a foreign key reference, not a JPA relationship

### Package Structure

```
com.banking.product/
├── domain/
│   ├── entity/
│   │   ├── Product.java
│   │   ├── ProductVersion.java
│   │   ├── ProductFeature.java
│   │   ├── ProductFeeEntry.java
│   │   ├── ProductFeeTier.java
│   │   ├── ProductCustomerSegment.java
│   │   └── ProductAuditLog.java
│   ├── enums/
│   │   ├── ProductFamily.java
│   │   ├── ProductStatus.java
│   │   ├── FeeCalculationMethod.java
│   │   └── AuditAction.java
│   └── embeddable/
│       └── AuditFields.java (per-module copy, following existing pattern)
├── repository/
│   ├── ProductRepository.java
│   ├── ProductVersionRepository.java
│   ├── ProductFeatureRepository.java
│   ├── ProductFeeEntryRepository.java
│   ├── ProductFeeTierRepository.java
│   ├── ProductCustomerSegmentRepository.java
│   └── ProductAuditLogRepository.java
├── service/
│   ├── ProductService.java
│   ├── ProductVersionService.java
│   ├── ProductFeatureService.java
│   ├── ProductPricingService.java
│   ├── ProductSegmentService.java
│   ├── ProductQueryService.java
│   ├── ProductAuditService.java
│   └── ProductEventPublisher.java
├── controller/
│   ├── ProductController.java
│   ├── ProductVersionController.java
│   ├── ProductQueryController.java
│   └── ProductExceptionHandler.java
├── dto/
│   ├── request/
│   │   ├── CreateProductRequest.java
│   │   ├── UpdateProductRequest.java
│   │   ├── SubmitProductRequest.java
│   │   ├── ApproveProductRequest.java
│   │   ├── RejectProductRequest.java
│   │   ├── ActivateProductRequest.java
│   │   ├── RetireProductRequest.java
│   │   ├── ProductFeatureRequest.java
│   │   ├── ProductFeeEntryRequest.java
│   │   └── ProductSegmentRequest.java
│   └── response/
│       ├── ProductResponse.java
│       ├── ProductVersionResponse.java
│       ├── ProductDetailResponse.java
│       ├── ProductSummaryResponse.java
│       ├── ProductFeatureResponse.java
│       ├── ProductFeeEntryResponse.java
│       ├── ProductFeeTierResponse.java
│       ├── ProductAuditLogResponse.java
│       └── ProductVersionDiffResponse.java
├── mapper/
│   └── ProductMapper.java
├── event/
│   ├── ProductActivatedEvent.java
│   ├── ProductRetiredEvent.java
│   └── ProductVersionCreatedEvent.java
└── exception/
    ├── ProductNotFoundException.java
    ├── ProductVersionNotFoundException.java
    ├── DuplicateProductCodeException.java
    ├── InvalidProductStatusException.java
    ├── ProductVersionNotEditableException.java
    ├── ProductHasActiveContractsException.java
    ├── MakerCheckerViolationException.java
    └── InvalidFeaturePrefixException.java
```

---

## 2. Entity Design (Data Model)

### Entity Relationship Diagram

```
products (1) ──── (N) product_versions
                          │
                          ├── (N) product_features
                          ├── (N) product_fee_entries
                          │         └── (N) product_fee_tiers
                          ├── (N) product_customer_segments
                          └── (N) product_audit_logs
```

### Entity Definitions

#### Product (root entity)

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String code;          // immutable, e.g., "BIZ-CURRENT"

    @Column(nullable = false)
    private String name;          // e.g., "Business Current Account"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductFamily family; // ACCOUNT, PAYMENT, TRADE_FINANCE

    @Column(columnDefinition = "TEXT")
    private String description;

    @Version
    private Long version;         // optimistic locking

    @Embedded
    private AuditFields audit;
}
```

#### ProductVersion

```java
@Entity
@Table(name = "product_versions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "version_number"})
})
public class ProductVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;  // 1, 2, 3...

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;   // DRAFT, PENDING_APPROVAL, APPROVED, ACTIVE, SUPERSEDED, RETIRED

    @Column(name = "submitted_by")
    private String submittedBy;     // maker username

    @Column(name = "approved_by")
    private String approvedBy;      // checker username

    @Column(name = "rejection_comment", columnDefinition = "TEXT")
    private String rejectionComment;

    @Column(name = "contract_count")
    private Long contractCount;     // cached count of live contracts referencing this version

    @Embedded
    private AuditFields audit;

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> features = new ArrayList<>();

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeEntry> feeEntries = new ArrayList<>();

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCustomerSegment> customerSegments = new ArrayList<>();
}
```

#### ProductFeature

```java
@Entity
@Table(name = "product_features")
public class ProductFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Column(name = "feature_key", nullable = false)
    private String featureKey;     // e.g., "account.overdraft_enabled"

    @Column(name = "feature_value", nullable = false)
    private String featureValue;   // "true", "false", or string values

    @Embedded
    private AuditFields audit;
}
```

#### ProductFeeEntry

```java
@Entity
@Table(name = "product_fee_entries")
public class ProductFeeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Column(name = "fee_type", nullable = false)
    private String feeType;        // e.g., "MONTHLY_MAINTENANCE"

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false)
    private FeeCalculationMethod method; // FLAT, PERCENTAGE, TIERED_VOLUME

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;     // for FLAT method

    @Column(precision = 10, scale = 6)
    private BigDecimal rate;       // for PERCENTAGE method

    @Column(nullable = false, length = 3)
    private String currency;

    @OneToMany(mappedBy = "feeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeTier> tiers = new ArrayList<>();

    @Embedded
    private AuditFields audit;
}
```

#### ProductFeeTier

```java
@Entity
@Table(name = "product_fee_tiers")
public class ProductFeeTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_entry_id", nullable = false)
    private ProductFeeEntry feeEntry;

    @Column(name = "tier_from", nullable = false)
    private Long tierFrom;         // volume range start (inclusive)

    @Column(name = "tier_to")
    private Long tierTo;           // volume range end (inclusive), null = unbounded

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;       // fee rate for this tier
}
```

#### ProductCustomerSegment

```java
@Entity
@Table(name = "product_customer_segments")
public class ProductCustomerSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType; // CORPORATE, SME, INDIVIDUAL — imported from com.banking.customer.domain.enums

    // Extensibility: add eligibility_rule_id column in future for rules-based eligibility
}
```

#### ProductAuditLog

```java
@Entity
@Table(name = "product_audit_logs")
public class ProductAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;    // CREATE, SUBMIT, APPROVE, REJECT, ACTIVATE, RETIRE, UPDATE

    @Column(nullable = false)
    private String actor;          // username who performed the action

    @Column(name = "from_status")
    private String fromStatus;

    @Column(name = "to_status")
    private String toStatus;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "maker_username")
    private String makerUsername;   // for maker-checker attribution

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // No @Embedded AuditFields — audit logs are immutable, created once
}
```

### Enums

```java
public enum ProductFamily {
    ACCOUNT, PAYMENT, TRADE_FINANCE
}

public enum ProductStatus {
    DRAFT, PENDING_APPROVAL, APPROVED, ACTIVE, SUPERSEDED, RETIRED
}

public enum FeeCalculationMethod {
    FLAT, PERCENTAGE, TIERED_VOLUME
}

public enum AuditAction {
    CREATE, UPDATE, SUBMIT, APPROVE, REJECT, ACTIVATE, RETIRE
}
```

### DTOs: Version Diff Response

```java
public class ProductVersionDiffResponse {
    private Long versionId1;
    private Long versionId2;
    private List<FieldDiff> generalFields;    // name, description changes
    private List<FeatureDiff> features;       // added/removed/changed features
    private List<FeeDiff> fees;              // added/removed/changed fee entries
    private List<SegmentDiff> segments;      // added/removed segments
}
```

### DTOs: Error Response

The `ProductExceptionHandler` uses the same `ErrorResponse` / `ValidationErrorResponse` pattern as `AccountExceptionHandler`. These inner classes are duplicated per module (not shared) following the existing convention.

### Extensibility Design: Customer Eligibility

Current design stores customer types directly on `ProductCustomerSegment`. For future rules-based eligibility:

```
Option A (recommended): Add eligibility_rule_id FK to product_customer_segments table
  - When null, segment is type-based (current behavior)
  - When set, references an eligibility_rules table with complex criteria
  
Option B: JSONB column for rule definition
  - Simpler but harder to validate and query
```

Data model supports Option A without schema migration — the `customer_type` column stays, a nullable `eligibility_rule_id` column is added later.

---

## 3. Service Layer Design

### ProductService

Handles product CRUD and the product root entity.

```java
@Service
@Transactional
public class ProductService {

    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(Long productId, UpdateProductRequest request);
    ProductDetailResponse getProduct(Long productId);
    ProductDetailResponse getProductByCode(String code);
    Page<ProductSummaryResponse> searchProducts(ProductSearchCriteria criteria, Pageable pageable);
}
```

### ProductVersionService

Handles version lifecycle (state machine) and version management.

```java
@Service
@Transactional
public class ProductVersionService {

    // Lifecycle transitions
    ProductVersionResponse submitForApproval(Long versionId, String username);
    ProductVersionResponse approve(Long versionId, String username);
    ProductVersionResponse reject(Long versionId, String username, String comment);
    ProductVersionResponse activate(Long versionId, String username);
    ProductVersionResponse retire(Long versionId, String username);

    // Version operations
    ProductVersionResponse createNewVersionFromActive(Long productId);
    ProductVersionResponse getVersion(Long versionId);
    List<ProductVersionResponse> getVersionHistory(Long productId);
    ProductVersionDiffResponse compareVersions(Long versionId1, Long versionId2);
}
```

**Version copying strategy (`createNewVersionFromActive`):**

When editing an ACTIVE/SUPERSEDED/RETIRED product, a new DRAFT version is created with deep-copied child entities:

1. Create new `ProductVersion` with `versionNumber = max + 1`, status = DRAFT
2. Deep copy all `ProductFeature` entries (new IDs, same key/value pairs)
3. Deep copy all `ProductFeeEntry` entries, and for each, deep copy all `ProductFeeTier` entries
4. Deep copy all `ProductCustomerSegment` entries
5. Do NOT copy `ProductAuditLog` entries (each version has its own audit trail)

This ensures the new DRAFT version starts as an independent copy that can be modified without affecting the source version.
- `submitForApproval`: status must be DRAFT
- `approve`: status must be PENDING_APPROVAL, approver != submitter
- `reject`: status must be PENDING_APPROVAL, comment required
- `activate`: status must be APPROVED, deactivate previous active version
- `retire`: status must be ACTIVE, contractCount == 0

### ProductFeatureService

```java
@Service
@Transactional
public class ProductFeatureService {

    ProductFeatureResponse addFeature(Long versionId, ProductFeatureRequest request);
    ProductFeatureResponse updateFeature(Long featureId, ProductFeatureRequest request);
    void removeFeature(Long featureId);
    List<ProductFeatureResponse> getFeatures(Long versionId);
}
```

**Feature prefix validation:** When adding/updating a feature, validate that the key prefix matches the product's family (e.g., `account.` for ACCOUNT family).

### ProductPricingService

```java
@Service
@Transactional
public class ProductPricingService {

    ProductFeeEntryResponse addFeeEntry(Long versionId, ProductFeeEntryRequest request);
    ProductFeeEntryResponse updateFeeEntry(Long feeEntryId, ProductFeeEntryRequest request);
    void removeFeeEntry(Long feeEntryId);
    List<ProductFeeEntryResponse> getFeeEntries(Long versionId);
}
```

### ProductSegmentService

```java
@Service
@Transactional
public class ProductSegmentService {

    void assignSegments(Long versionId, List<CustomerType> customerTypes);
    List<CustomerType> getSegments(Long versionId);
}
```

### ProductQueryService (read interface for downstream modules)

```java
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    // For business use — only ACTIVE products
    ProductVersionResponse getActiveProductByCode(String code);
    List<ProductVersionResponse> getActiveProductsByFamily(ProductFamily family);
    List<ProductVersionResponse> getActiveProductsByCustomerType(CustomerType customerType);

    // For historical lookup — any status
    ProductVersionResponse getProductVersionById(Long versionId);
    ProductVersionResponse getProductVersion(String productCode, Integer versionNumber);
}
```

### ProductAuditService

```java
@Service
@Transactional
public class ProductAuditService {

    void log(Long versionId, AuditAction action, String actor, String fromStatus, String toStatus, String comment);
    void logWithMaker(Long versionId, AuditAction action, String actor, String maker, String comment);
    List<ProductAuditLogResponse> getAuditTrail(Long versionId);
}
```

### ProductEventPublisher

```java
@Service
public class ProductEventPublisher {

    void publishActivated(Long versionId, String productCode);
    void publishRetired(Long versionId, String productCode);
    void publishVersionCreated(Long versionId, String productCode);
}
```

Events are Spring `ApplicationEvent` for now (internal monolith). Can be upgraded to Kafka later.

---

## 4. Controller / API Design

### ProductController (`/api/products`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/products` | Create new product | S1.1 |
| GET | `/api/products/{id}` | Get product by ID | S7.3 |
| GET | `/api/products/code/{code}` | Get product by code | S7.3 |
| PUT | `/api/products/{id}` | Update product (creates new version if non-DRAFT) | S1.4, S1.5 |
| GET | `/api/products/search` | Search/list products with filters | S1.9-12 |

### ProductVersionController (`/api/products/{productId}/versions`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| GET | `/api/products/{productId}/versions` | List version history | S1.13 |
| GET | `/api/products/{productId}/versions/{versionId}` | Get version details | S7.5 |
| POST | `/api/products/{productId}/versions/{versionId}/submit` | Submit for approval | S2.1 |
| POST | `/api/products/{productId}/versions/{versionId}/approve` | Approve | S2.2 |
| POST | `/api/products/{productId}/versions/{versionId}/reject` | Reject | S2.3 |
| POST | `/api/products/{productId}/versions/{versionId}/activate` | Activate | S2.5 |
| POST | `/api/products/{productId}/versions/{versionId}/retire` | Retire | S2.8 |
| GET | `/api/products/{productId}/versions/compare?v1={id}&v2={id}` | Compare versions | S1.14 |

### ProductFeatureController (`/api/products/{productId}/versions/{versionId}/features`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/.../features` | Add feature | S4.1 |
| PUT | `/api/.../features/{featureId}` | Update feature | S4.2 |
| DELETE | `/api/.../features/{featureId}` | Remove feature | - |
| GET | `/api/.../features` | List features | - |

### ProductPricingController (`/api/products/{productId}/versions/{versionId}/fees`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/.../fees` | Add fee entry (validates DRAFT status, S5.5) | S5.1-4 |
| PUT | `/api/.../fees/{feeEntryId}` | Update fee entry | - |
| DELETE | `/api/.../fees/{feeEntryId}` | Remove fee entry | - |
| GET | `/api/.../fees` | List fee entries | - |

### ProductSegmentController (`/api/products/{productId}/versions/{versionId}/segments`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| PUT | `/api/.../segments` | Assign customer segments | S6.1 |
| GET | `/api/.../segments` | Get segments | - |

### ProductQueryController (`/api/product-query`) — for downstream modules

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| GET | `/api/product-query/active?code={code}` | Get active product by code | S7.3 |
| GET | `/api/product-query/version/{versionId}` | Get product version by ID | S7.6 |
| GET | `/api/product-query/family/{family}?status={status}` | Get products by family+status | S7.7, S7.8 |
| GET | `/api/product-query/customer-type/{type}?status={status}` | Get products by segment | S6.2, S6.3 |

### Maker-Checker Enforcement

At the controller level:
- `approve` endpoint checks that `username` header/param != `submittedBy` on the version
- `reject` endpoint checks that `username` != `submittedBy` and `comment` is non-empty

---

## 5. Frontend Design

### Sidebar Entry

Add "Products" to the sidebar between "Accounts" and "Transactions" (which is currently commented out).

### Pages

| Page | Route | Description |
|------|-------|-------------|
| ProductListPage | `/products` | List with search/filter by family, status, segment |
| ProductDetailPage | `/products/:id` | View product with all versions, features, pricing |
| ProductFormPage | `/products/new` | Create new product |
| ProductFormPage | `/products/:id/edit` | Edit product (creates new version if non-DRAFT) |
| ProductVersionComparePage | `/products/:id/compare?v1=&v2=` | Side-by-side version comparison |

### Redux Slice

```
store/slices/productSlice.ts
```

State shape:
```typescript
interface ProductState {
  products: ProductSummary[];
  selectedProduct: ProductDetail | null;
  selectedVersion: ProductVersionDetail | null;
  versionHistory: ProductVersionSummary[];
  loading: boolean;
  error: string | null;
  filters: ProductSearchFilters;
  pagination: PaginationState;
}
```

Async thunks:
- `fetchProducts` — search/list with filters
- `fetchProductById` — get product detail
- `createProduct` — POST
- `updateProduct` — PUT
- `submitForApproval` — POST submit
- `approveProduct` — POST approve
- `rejectProduct` — POST reject
- `activateProduct` — POST activate
- `retireProduct` — POST retire
- `fetchVersionHistory` — GET versions
- `compareVersions` — GET compare

### Services

```
services/productService.ts
```

All API calls via existing `apiClient` (Axios instance). Service methods:

```typescript
// Product CRUD
getProducts(filters: ProductSearchFilters): Promise<PaginatedResponse<ProductSummary>>
getProductById(id: number): Promise<ProductDetail>
createProduct(request: CreateProductRequest): Promise<Product>
updateProduct(id: number, request: UpdateProductRequest): Promise<Product>

// Version lifecycle
submitForApproval(productId: number, versionId: number): Promise<ProductVersion>
approveProduct(productId: number, versionId: number): Promise<ProductVersion>
rejectProduct(productId: number, versionId: number, comment: string): Promise<ProductVersion>
activateProduct(productId: number, versionId: number): Promise<ProductVersion>
retireProduct(productId: number, versionId: number): Promise<ProductVersion>

// Version history
getVersionHistory(productId: number): Promise<ProductVersionSummary[]>
compareVersions(productId: number, v1Id: number, v2Id: number): Promise<ProductVersionDiff>

// Features CRUD
addFeature(productId: number, versionId: number, feature: FeatureRequest): Promise<ProductFeature>
updateFeature(productId: number, versionId: number, featureId: number, feature: FeatureRequest): Promise<ProductFeature>
removeFeature(productId: number, versionId: number, featureId: number): Promise<void>

// Fees CRUD
addFeeEntry(productId: number, versionId: number, fee: FeeEntryRequest): Promise<ProductFeeEntry>
updateFeeEntry(productId: number, versionId: number, feeId: number, fee: FeeEntryRequest): Promise<ProductFeeEntry>
removeFeeEntry(productId: number, versionId: number, feeId: number): Promise<void>

// Segments
assignSegments(productId: number, versionId: number, segments: CustomerType[]): Promise<void>
```

### Types

```
types/product.types.ts
```

Key types: `Product`, `ProductVersion`, `ProductFeature`, `ProductFeeEntry`, `ProductFeeTier`, `ProductSegment`, `ProductFamily`, `ProductStatus`, `FeeCalculationMethod`.

---

## 6. Database Migration

### V2__create_product_schema.sql

```sql
-- Product root
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    family VARCHAR(30) NOT NULL,
    description TEXT,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Product versions
CREATE TABLE product_versions (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    version_number INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    submitted_by VARCHAR(100),
    approved_by VARCHAR(100),
    rejection_comment TEXT,
    contract_count BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (product_id, version_number)
);

CREATE INDEX idx_product_versions_product_id ON product_versions(product_id);
CREATE INDEX idx_product_versions_status ON product_versions(status);

-- Features per version
CREATE TABLE product_features (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    feature_key VARCHAR(100) NOT NULL,
    feature_value VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (product_version_id, feature_key)
);

-- Fee entries per version
CREATE TABLE product_fee_entries (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    fee_type VARCHAR(100) NOT NULL,
    calculation_method VARCHAR(30) NOT NULL,
    amount NUMERIC(19,4),
    rate NUMERIC(10,6),
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Fee tiers
CREATE TABLE product_fee_tiers (
    id BIGSERIAL PRIMARY KEY,
    fee_entry_id BIGINT NOT NULL REFERENCES product_fee_entries(id) ON DELETE CASCADE,
    tier_from BIGINT NOT NULL,
    tier_to BIGINT,
    rate NUMERIC(19,6) NOT NULL
);

CREATE INDEX idx_fee_tiers_entry_id ON product_fee_tiers(fee_entry_id);

-- Customer segments per version
CREATE TABLE product_customer_segments (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    customer_type VARCHAR(30) NOT NULL,
    UNIQUE (product_version_id, customer_type)
);

-- Audit log (immutable)
CREATE TABLE product_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    action VARCHAR(30) NOT NULL,
    actor VARCHAR(100) NOT NULL,
    from_status VARCHAR(30),
    to_status VARCHAR(30),
    comment TEXT,
    maker_username VARCHAR(100),
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_version_id ON product_audit_logs(product_version_id);

-- Prevent audit log modification
REVOKE UPDATE, DELETE ON product_audit_logs FROM PUBLIC;
```

### V3__seed_product_data.sql

Seed the 4 existing account products. Codes use banking-standard naming (BDD scenarios use example codes like "BIZ-CURRENT" for test readability — those are not the production seed codes).

```sql
-- Seed products (codes match standard banking product naming)
INSERT INTO products (code, name, family, description, created_by) VALUES
('CURRENT', 'Current Account', 'ACCOUNT', 'Standard current account with overdraft facility', 'system'),
('SAVINGS', 'Savings Account', 'ACCOUNT', 'Interest-bearing savings account', 'system'),
('FIXED-DEPOSIT', 'Fixed Deposit', 'ACCOUNT', 'Term deposit with fixed interest rate', 'system'),
('LOAN', 'Loan Account', 'ACCOUNT', 'Business loan account', 'system');

-- Seed versions (all ACTIVE v1)
INSERT INTO product_versions (product_id, version_number, status, created_by) VALUES
(1, 1, 'ACTIVE', 'system'),
(2, 1, 'ACTIVE', 'system'),
(3, 1, 'ACTIVE', 'system'),
(4, 1, 'ACTIVE', 'system');

-- Update existing accounts to reference product_version_id instead of product_id
-- (This requires a migration strategy — see Integration section)
```

---

## 7. Integration with Existing Modules

### Account Module Integration

**Current state:** `Account.productId` is a bare `Long`. Demo data uses hardcoded values 1-4.

**Migration strategy:**

1. Add `product_version_id BIGINT` column to `accounts` table
2. Map existing `productId` values (1-4) to seeded `product_versions.id` values
3. Keep `product_id` column temporarily for backward compatibility
4. Update `AccountOpeningRequest` to accept `productCode` (resolved to active version by service)
5. Update `AccountMapper` to populate `productVersionId` and resolve product name

**Account entity change:**
```java
@Column(name = "product_version_id")
private Long productVersionId;  // replaces productId over time
```

**AccountOpeningRequest change:**
```java
// Existing fields kept for backward compat
private String productCode;  // resolved to active product version in service
private Long productId;      // deprecated
```

**Account service integration:**
```java
// In AccountService.openAccount():
ProductVersionResponse pv = productQueryService.getActiveProductByCode(request.getProductCode());
account.setProductVersionId(pv.getId());
```

### Query Interface for Downstream

Other modules access products through `ProductQueryService` (in-process call) or `ProductQueryController` (HTTP). For the monolith, in-process is preferred:

```java
// In Account module
@Autowired
private ProductQueryService productQueryService;
```

No direct JPA joins between Account entities and Product entities — loose coupling maintained by ID references.

---

## 8. Maker-Checker Implementation

### Role Detection

For the initial implementation, roles are passed via HTTP header `X-User-Role` (values: `MAKER`, `CHECKER`, `ADMIN`) and `X-Username`.

### Enforcement Rules

| Action | Allowed Role | Additional Check |
|--------|-------------|-----------------|
| Create/Edit product | MAKER | - |
| Submit for approval | MAKER | Version must be DRAFT |
| Approve | CHECKER | `username != submittedBy` |
| Reject | CHECKER | `username != submittedBy`, comment required |
| Activate | ADMIN | Version must be APPROVED |
| Retire | ADMIN | Version must be ACTIVE, no live contracts |

### Audit Attribution

- `submittedBy` stored on `ProductVersion` entity
- `approvedBy` stored on `ProductVersion` entity
- Every action logged to `ProductAuditLog` with `actor` and `makerUsername`

---

## 9. Error Handling

### Exception Classes

| Exception | Error Code | HTTP Status | Trigger |
|-----------|-----------|-------------|---------|
| `ProductNotFoundException` | PROD-001 | 404 | Product ID/code not found |
| `ProductVersionNotFoundException` | PROD-002 | 404 | Version ID not found |
| `DuplicateProductCodeException` | PROD-003 | 409 | Code already exists |
| `InvalidProductStatusException` | PROD-004 | 400 | Invalid state transition |
| `ProductVersionNotEditableException` | PROD-005 | 400 | Edit on non-DRAFT version |
| `ProductHasActiveContractsException` | PROD-006 | 409 | Retire with live contracts |
| `MakerCheckerViolationException` | PROD-007 | 403 | Same user submit+approve |
| `InvalidFeaturePrefixException` | PROD-008 | 400 | Feature prefix mismatch |

### Global Exception Handler

```java
@RestControllerAdvice(basePackages = "com.banking.product")
public class ProductExceptionHandler {
    // Follows existing pattern from GlobalExceptionHandler in customer module
}
```

---

## 10. Testing Strategy

### Standards

Follows [Test Strategy Template](../templates/test-strategy.md) for:
- Merge failure prevention (stale cache, duplicate classes, Gradle syntax, mock conventions)
- Test conventions (error code constants, HTTP status verification, mock patterns, DTO types)
- Pre-merge checklist
- CI pipeline requirements
- Test data isolation
- Coverage requirements

### Module-Specific Exception Constants

Each exception class must define `ERROR_CODE` as a public constant:

```java
public class ProductNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "PROD-001";
    // ...
}
```

Full list: PROD-001 through PROD-008 (see Section 9 Error Handling).

### Module-Specific Unit Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `ProductServiceTest` | Product CRUD | S1.1, S1.2, S1.3, S1.4 |
| `ProductVersionServiceTest` | Lifecycle state machine | S2.1-S2.9, S3.1-S3.2 |
| `ProductFeatureServiceTest` | Feature CRUD + prefix validation | S4.1-S4.5, S8.5 |
| `ProductPricingServiceTest` | Fee CRUD + tier validation | S5.1-S5.6 |
| `ProductSegmentServiceTest` | Segment assignment | S6.1-S6.3 |
| `ProductQueryServiceTest` | Downstream queries | S7.1-S7.8 |

### Module-Specific Integration Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `ProductControllerTest` | API endpoints with MockMvc | S1.1-S1.14 |
| `ProductVersionControllerTest` | Version lifecycle endpoints | S2.1-S2.9 |
| `ProductQueryControllerTest` | Downstream query endpoints | S7.1-S7.8 |
| `MakerCheckerIntegrationTest` | Full maker-checker flow | S3.1-S3.2, S9.1-S9.4 |

### Module-Specific Frontend Tests

| Test File | Covers |
|-----------|--------|
| `ProductListPage.test.tsx` | Filtering, pagination |
| `ProductDetailPage.test.tsx` | Version display, features, pricing |
| `ProductFormPage.test.tsx` | Create/edit form validation |
| `productSlice.test.ts` | Redux state management |
| `productService.test.ts` | API service methods |

---

## 11. Deprecation Notice: ProductFeeEntry in Favor of Charges Module

Product-level fees (`ProductFeeEntry`, `ProductFeeTier`, `FeeCalculationMethod`) are **deprecated** in favor of the fully-featured Charges Management module (`com.banking.charges`).

### Why Migrate?

The Charges module provides richer capabilities that the product module's fee system lacks:

- **Min/Max caps** — `ChargeRule.minAmount` / `ChargeRule.maxAmount`
- **Customer overrides** — `CustomerChargeOverride` per customer
- **Fee waivers** — `FeeWaiver` with scope, validity period, and percentage
- **Interest rates** — `InterestRate` with accrual schedules and tiered interest
- **Status management** — Active/Inactive/Deprecated status on `ChargeDefinition`
- **Single source of truth** — `ProductCharge` links charges to product codes (not versions), which is more appropriate since fees don't change per version

### Migration

- **Migration script:** `V14__migrate_product_fees_to_charges.sql`
- **Approach:** Data is copied from `product_fee_entries` / `product_fee_tiers` to `charge_definitions` / `charge_rules` / `charge_tiers` / `product_charges`
- **No data deletion:** The `product_fee_entries` and `product_fee_tiers` tables are retained for backward compatibility and historical version diffs

### Deprecated Components

| Component | Replacement |
|-----------|-------------|
| `ProductFeeEntry` | `ChargeDefinition` + `ChargeRule` |
| `ProductFeeTier` | `ChargeTier` |
| `FeeCalculationMethod` enum | Same values, used in `ChargeRule.calculationMethod` |
| `ProductPricingService` | `ChargeDefinitionService`, `ChargeRuleService`, `ChargeAssignmentService` |
| `ProductDetailResponse.feeEntries` | Query via Charges module endpoints |
| `ProductVersionDiffResponse.FeeDiff` | Kept for historical diffs (deprecated) |

### Fee Type Mapping

The migration maps free-text `fee_type` strings to `ChargeType` enum values:

| fee_type pattern | ChargeType |
|-----------------|------------|
| `%MAINTENANCE%` | `MONTHLY_MAINTENANCE` |
| `%TRANSACTION%` | `TRANSACTION_FEE` |
| `%OVERDRAFT%` | `OVERDRAFT_FEE` |
| `%WIRE%` | `WIRE_TRANSFER_FEE` |
| `%STATEMENT%` | `STATEMENT_FEE` |
| `%CLOSURE%` | `EARLY_CLOSURE_FEE` |
| `%INTEREST%` | `INTEREST` |
| `%PROCESSING%` | `PROCESSING_FEE` |
| `%ANNUAL%` | `ANNUAL_FEE` |
| Default | `TRANSACTION_FEE` |

### Architecture

```
Deprecated (retained):
  ProductVersion (1) ── (N) ProductFeeEntry ── (N) ProductFeeTier

New (recommended):
  Product (1) ── (N) ProductCharge ── (1) ChargeDefinition ── (N) ChargeRule ── (N) ChargeTier
                                     └── (N) CustomerChargeOverride
                                     └── (N) FeeWaiver
                                     └── (N) InterestRate
```

---

## 8. Implementation Guardrails

**For Future Developers:**
The Product module is a **foundation module** that provides product catalog services to all business modules. It must maintain strict architectural compliance.

### Module Boundary Rules (AGENTS.md)

| Rule | Compliance Status |
|------|-------------------|
| **Rule 1: No domain entity sharing** | ✅ Other modules must not import `Product` entity directly; they use `ProductDTO` from `.dto` package |
| **Rule 2: Only api/dto public** | ✅ Query services in `com.banking.product.api`; DTOs in `com.banking.product.dto` |
| **Rule 3: JPA relationships within module** | ✅ Product entities only reference other Product entities; cross-module refs use `Long productId` |
| **Rule 4: Async for side-effects** | ✅ Product lifecycle events published asynchronously (`ProductActivatedEvent`, etc.) |

**API Contract Enforcement:**
- `ProductQueryService` (read-only) in `api` package - consumed by Account, Payments, Trade Finance
- `ProductService` (write) in `api` package - consumed by admin/management modules
- All DTOs must be in `dto` package, with no JPA annotations
- Service interfaces define the contract; implementations are internal

**Event Publishing:**
- `ProductActivatedEvent` - when product becomes available for sale
- `ProductRetiredEvent` - when product is discontinued
- `ProductVersionCreatedEvent` - when new version is created
- Events should contain minimal data (IDs, codes, status); consumers fetch details via API if needed

**Code Review Checklist:**
- [ ] New services added to `.api` package, not `.service`
- [ ] DTOs used by other modules are in `.dto` package
- [ ] No domain entities exposed outside the module
- [ ] No direct repository access in API interfaces
- [ ] Events are published for lifecycle changes that affect other modules
- [ ] No circular dependencies (product should not depend on account/payments/etc.)

See `AGENTS.md` for complete architecture enforcement rules.

---

## 9. Error Handling
