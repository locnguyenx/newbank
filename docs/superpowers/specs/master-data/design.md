# Technical Design: Master Data Management

**Version:** 1.0  
**Date:** 2026-03-21  
**Status:** Draft  
**Module:** Master Data Management (`com.banking.masterdata`)  
**BRD Reference:** `docs/superpowers/brds/master-data/brd.md`  
**BDD Reference:** `docs/superpowers/specs/master-data/bdd.md`

---

## 1. Architecture Overview

### Module Placement

Master Data is a **leaf foundation module** — no other module depends on it above, but everything below uses it. Provides reference data (currencies, countries, exchange rates, holidays, etc.) consumed by Customer, Account, Product, and all business modules.

```
┌─────────────────────────────────────────────────────────┐
│                  Modular Monolith                       │
│                                                         │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐  │
│  │ Customer │ │ Account  │ │ Product  │ │ (Limi    │  │
│  │ Module   │ │ Module   │ │ Module   │ │ ts)      │  │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘  │
│       │             │            │             │         │
│       └─────────────┼────────────┼─────────────┘         │
│                     ▼            ▼                        │
│              ┌─────────────────────┐                     │
│              │   Master Data       │  ◄── Leaf Module    │
│              │   Module            │      (no downstream │
│              └─────────────────────┘       dependencies) │
└─────────────────────────────────────────────────────────┘
```

### Module Boundaries

- **Inbound:** Other modules query via `MasterDataQueryService` (read-only interface) or use lookup by code on repositories
- **Outbound:** None — this is a leaf module
- **No direct DB joins:** Other modules store entity codes (e.g., `currencyCode VARCHAR(3)`) as references, not JPA relationships

### Package Structure

```
com.banking.masterdata/
├── domain/
│   ├── entity/
│   │   ├── Currency.java
│   │   ├── Country.java
│   │   ├── Industry.java
│   │   ├── ExchangeRate.java
│   │   ├── Holiday.java
│   │   ├── Branch.java
│   │   ├── Channel.java
│   │   └── DocumentType.java
│   └── embeddable/
│       └── AuditFields.java
├── repository/
│   ├── CurrencyRepository.java
│   ├── CountryRepository.java
│   ├── IndustryRepository.java
│   ├── ExchangeRateRepository.java
│   ├── HolidayRepository.java
│   ├── BranchRepository.java
│   ├── ChannelRepository.java
│   └── DocumentTypeRepository.java
├── service/
│   ├── CurrencyService.java
│   ├── CountryService.java
│   ├── IndustryService.java
│   ├── ExchangeRateService.java
│   ├── HolidayService.java
│   ├── BranchService.java
│   ├── ChannelService.java
│   ├── DocumentTypeService.java
│   └── MasterDataQueryService.java
├── controller/
│   ├── CurrencyController.java
│   ├── CountryController.java
│   ├── IndustryController.java
│   ├── ExchangeRateController.java
│   ├── HolidayController.java
│   ├── BranchController.java
│   ├── ChannelController.java
│   ├── DocumentTypeController.java
│   └── MasterDataExceptionHandler.java
├── dto/
│   ├── request/
│   │   ├── CreateCurrencyRequest.java
│   │   ├── UpdateCurrencyRequest.java
│   │   ├── CreateCountryRequest.java
│   │   ├── CreateIndustryRequest.java
│   │   ├── CreateExchangeRateRequest.java
│   │   ├── CreateHolidayRequest.java
│   │   ├── CreateBranchRequest.java
│   │   ├── UpdateBranchRequest.java
│   │   ├── CreateChannelRequest.java
│   │   ├── UpdateChannelRequest.java
│   │   ├── CreateDocumentTypeRequest.java
│   │   └── UpdateDocumentTypeRequest.java
│   └── response/
│       ├── CurrencyResponse.java
│       ├── CountryResponse.java
│       ├── IndustryResponse.java
│       ├── ExchangeRateResponse.java
│       ├── HolidayResponse.java
│       ├── BranchResponse.java
│       ├── ChannelResponse.java
│       └── DocumentTypeResponse.java
├── mapper/
│   └── MasterDataMapper.java
└── exception/
    ├── CurrencyNotFoundException.java
    ├── CurrencyAlreadyExistsException.java
    ├── CountryNotFoundException.java
    ├── IndustryNotFoundException.java
    ├── ExchangeRateNotFoundException.java
    ├── HolidayNotFoundException.java
    ├── BranchNotFoundException.java
    ├── BranchAlreadyExistsException.java
    ├── ChannelNotFoundException.java
    ├── ChannelAlreadyExistsException.java
    ├── DocumentTypeNotFoundException.java
    └── DocumentTypeAlreadyExistsException.java
```

---

## 2. Entity Design (Data Model)

### Entity Relationship Diagram

```
currencies (1) ◄── exchange_rates.base_currency (FK)
currencies (1) ◄── exchange_rates.target_currency (FK)
countries  (1) ◄── holidays.country_code (FK)
countries  (1) ◄── branches.country_code (FK)
industries (1) ◄── industries.parent_code (self-ref)

currencies, countries, industries, channels, document_types are independent entities
```

### Entity Definitions

#### Currency

```java
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(length = 3)
    private String code;                    // ISO 4217, e.g., "USD"

    @Column(nullable = false)
    private String name;

    @Column(length = 5)
    private String symbol;

    @Column(name = "decimal_places", nullable = false)
    private Integer decimalPlaces;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

#### Country

```java
@Entity
@Table(name = "countries")
public class Country {
    @Id
    @Column(name = "iso_code", length = 2)
    private String isoCode;                 // ISO 3166-1, e.g., "US"

    @Column(nullable = false)
    private String name;

    @Column(length = 20)
    private String region;                  // ASIA, EUROPE, AMERICAS, AFRICA, OCEANIA

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

#### Industry

```java
@Entity
@Table(name = "industries")
public class Industry {
    @Id
    @Column(length = 10)
    private String code;                    // NACE/SIC code, e.g., "64"

    @Column(nullable = false)
    private String name;

    @Column(name = "parent_code", length = 10)
    private String parentCode;              // null for top-level

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

#### ExchangeRate

```java
@Entity
@Table(name = "exchange_rates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"base_currency", "target_currency", "effective_date"})
})
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency", nullable = false)
    private Currency targetCurrency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Embedded
    private AuditFields audit;
}
```

#### Holiday

```java
@Entity
@Table(name = "holidays", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"country_code", "holiday_date"})
})
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(columnDefinition = "TEXT")
    private String description;
}
```

#### Branch

```java
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @Column(length = 20)
    private String code;                    // e.g., "NYC-001"

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

#### Channel

```java
@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @Column(length = 20)
    private String code;                    // e.g., "MOBILE"

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

#### DocumentType

```java
@Entity
@Table(name = "document_types")
public class DocumentType {
    @Id
    @Column(length = 30)
    private String code;                    // e.g., "PASSPORT"

    @Column(nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String category;                // IDENTITY, ADDRESS, CORPORATE

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Embedded
    private AuditFields audit;
}
```

---

## 3. Service Layer Design

### CurrencyService

```java
@Service
@Transactional
public class CurrencyService {

    CurrencyResponse createCurrency(CreateCurrencyRequest request);
    CurrencyResponse updateCurrency(String code, UpdateCurrencyRequest request);
    CurrencyResponse deactivateCurrency(String code);
    CurrencyResponse getCurrency(String code);
    List<CurrencyResponse> getAllCurrencies(boolean activeOnly);
}
```

### CountryService

```java
@Service
@Transactional
public class CountryService {

    CountryResponse createCountry(CreateCountryRequest request);
    CountryResponse deactivateCountry(String isoCode);
    CountryResponse getCountry(String isoCode);
    List<CountryResponse> getAllCountries(boolean activeOnly);
}
```

### IndustryService

```java
@Service
@Transactional
public class IndustryService {

    IndustryResponse createIndustry(CreateIndustryRequest request);
    IndustryResponse getIndustry(String code);
    List<IndustryResponse> getAllIndustries(boolean activeOnly);
    List<IndustryResponse> getChildren(String parentCode);
}
```

### ExchangeRateService

```java
@Service
@Transactional
public class ExchangeRateService {

    ExchangeRateResponse createExchangeRate(CreateExchangeRateRequest request);
    ExchangeRateResponse getLatestRate(String baseCurrency, String targetCurrency);
    List<ExchangeRateResponse> getRateHistory(String baseCurrency, String targetCurrency, LocalDate from, LocalDate to);
    BigDecimal convertAmount(String baseCurrency, String targetCurrency, BigDecimal amount, LocalDate date);
}
```

**Key logic — `getLatestRate`:**
```java
// SELECT * FROM exchange_rates
// WHERE base_currency = ? AND target_currency = ?
// ORDER BY effective_date DESC LIMIT 1
```

**Key logic — `convertAmount`:**
```java
BigDecimal rate = getLatestRate(baseCurrency, targetCurrency, date).getRate();
return amount.multiply(rate).setScale(targetCurrency.getDecimalPlaces(), RoundingMode.HALF_UP);
```

### HolidayService

```java
@Service
@Transactional
public class HolidayService {

    HolidayResponse createHoliday(CreateHolidayRequest request);
    boolean isHoliday(String countryCode, LocalDate date);
    List<HolidayResponse> getHolidays(String countryCode, int year);
    List<HolidayResponse> getHolidaysInRange(String countryCode, LocalDate from, LocalDate to);
}
```

### BranchService

```java
@Service
@Transactional
public class BranchService {

    BranchResponse createBranch(CreateBranchRequest request);
    BranchResponse updateBranch(String code, UpdateBranchRequest request);
    BranchResponse deactivateBranch(String code);
    BranchResponse getBranch(String code);
    List<BranchResponse> getActiveBranches(String countryCode);
}
```

### ChannelService

```java
@Service
@Transactional
public class ChannelService {

    ChannelResponse createChannel(CreateChannelRequest request);
    ChannelResponse updateChannel(String code, UpdateChannelRequest request);
    ChannelResponse deactivateChannel(String code);
    List<ChannelResponse> getActiveChannels();
}
```

### DocumentTypeService

```java
@Service
@Transactional
public class DocumentTypeService {

    DocumentTypeResponse createDocumentType(CreateDocumentTypeRequest request);
    DocumentTypeResponse updateDocumentType(String code, UpdateDocumentTypeRequest request);
    DocumentTypeResponse deactivateDocumentType(String code);
    List<DocumentTypeResponse> getActiveDocumentTypes(String category);
}
```

### MasterDataQueryService (read interface for downstream modules)

```java
@Service
@Transactional(readOnly = true)
public class MasterDataQueryService {

    // Currency
    List<CurrencyResponse> getActiveCurrencies();

    // Country
    List<CountryResponse> getActiveCountries();

    // Exchange Rate
    ExchangeRateResponse getLatestExchangeRate(String base, String target);
    BigDecimal convertAmount(String base, String target, BigDecimal amount);

    // Holiday
    boolean isHoliday(String countryCode, LocalDate date);

    // Branch
    List<BranchResponse> getActiveBranches();

    // Channel
    List<ChannelResponse> getActiveChannels();

    // Document Type
    List<DocumentTypeResponse> getActiveDocumentTypes(String category);
}
```

---

## 4. Controller / API Design

### CurrencyController (`/api/master-data/currencies`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/currencies` | Create currency | S1.1 |
| GET | `/api/master-data/currencies` | List currencies (query param: `activeOnly=true`) | S1.3 |
| GET | `/api/master-data/currencies/{code}` | Get currency by code | - |
| PUT | `/api/master-data/currencies/{code}` | Update currency | S1.4 |
| PUT | `/api/master-data/currencies/{code}/deactivate` | Deactivate currency | S1.5 |

### CountryController (`/api/master-data/countries`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/countries` | Create country | S2.1 |
| GET | `/api/master-data/countries` | List countries | S2.2 |
| GET | `/api/master-data/countries/{isoCode}` | Get country by code | - |

### IndustryController (`/api/master-data/industries`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/industries` | Create industry | S3.1 |
| GET | `/api/master-data/industries` | List industries | - |
| GET | `/api/master-data/industries/{code}` | Get industry by code | - |
| GET | `/api/master-data/industries/{code}/children` | Get child industries | S3.2 |

### ExchangeRateController (`/api/master-data/exchange-rates`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/exchange-rates` | Create exchange rate | S4.1 |
| GET | `/api/master-data/exchange-rates/latest?base=X&target=Y` | Latest rate | S4.4 |
| GET | `/api/master-data/exchange-rates/history?base=X&target=Y&from=&to=` | Rate history | S4.2 |
| GET | `/api/master-data/exchange-rates/convert?base=X&target=Y&amount=100` | Convert amount | S4.5 |

### HolidayController (`/api/master-data/holidays`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/holidays` | Create holiday | S5.1 |
| GET | `/api/master-data/holidays?countryCode=US&year=2026` | Holidays for year | S5.3 |
| GET | `/api/master-data/holidays/check?countryCode=US&date=2026-01-01` | Is holiday? | S5.4 |

### BranchController (`/api/master-data/branches`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/branches` | Create branch | S6.1 |
| GET | `/api/master-data/branches` | List branches | S6.2 |
| GET | `/api/master-data/branches/{code}` | Get branch by code | - |

### ChannelController (`/api/master-data/channels`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/channels` | Create channel | S7.1 |
| GET | `/api/master-data/channels` | List channels | S7.2 |

### DocumentTypeController (`/api/master-data/document-types`)

| Method | Endpoint | Description | BDD |
|--------|----------|-------------|-----|
| POST | `/api/master-data/document-types` | Create document type | S8.1 |
| GET | `/api/master-data/document-types` | List document types (query: `category`) | S8.2 |

---

## 5. Frontend Design

### Sidebar Entry

Add "Master Data" under a "System" section in the sidebar (below existing modules).

### Pages

| Page | Route | Description |
|------|-------|-------------|
| MasterDataListPage | `/master-data/:entityType` | List of currencies/countries/industries/etc. |
| MasterDataFormPage | `/master-data/:entityType/new` | Create entity |
| MasterDataFormPage | `/master-data/:entityType/:code/edit` | Edit entity |
| ExchangeRatePage | `/master-data/exchange-rates` | Exchange rate management + converter |

### Redux Slice

```
store/slices/masterDataSlice.ts
```

State shape:
```typescript
interface MasterDataState {
  currencies: Currency[];
  countries: Country[];
  industries: Industry[];
  exchangeRates: ExchangeRate[];
  holidays: Holiday[];
  branches: Branch[];
  channels: Channel[];
  documentTypes: DocumentType[];
  loading: boolean;
  error: string | null;
}
```

### Services

```
services/masterDataService.ts
```

All API calls via existing `apiClient` with base path `/api/master-data`.

### Types

```
types/masterData.types.ts
```

---

## 6. Database Migration

### V1__create_master_data_schema.sql

```sql
-- Currency (ISO 4217)
CREATE TABLE currencies (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(5),
    decimal_places INTEGER NOT NULL DEFAULT 2,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Country (ISO 3166-1)
CREATE TABLE countries (
    iso_code VARCHAR(2) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Industry (NACE/SIC)
CREATE TABLE industries (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    parent_code VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Exchange Rates (effective-dated)
CREATE TABLE exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    base_currency VARCHAR(3) NOT NULL REFERENCES currencies(code),
    target_currency VARCHAR(3) NOT NULL REFERENCES currencies(code),
    rate NUMERIC(19,6) NOT NULL,
    effective_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100),
    UNIQUE (base_currency, target_currency, effective_date)
);

CREATE INDEX idx_exchange_rates_pair ON exchange_rates(base_currency, target_currency);
CREATE INDEX idx_exchange_rates_date ON exchange_rates(effective_date DESC);

-- Holiday Calendar
CREATE TABLE holidays (
    id BIGSERIAL PRIMARY KEY,
    country_code VARCHAR(2) NOT NULL REFERENCES countries(iso_code),
    holiday_date DATE NOT NULL,
    description TEXT,
    UNIQUE (country_code, holiday_date)
);

CREATE INDEX idx_holidays_country_date ON holidays(country_code, holiday_date);

-- Branch
CREATE TABLE branches (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    country_code VARCHAR(2) NOT NULL REFERENCES countries(iso_code),
    address TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Channel
CREATE TABLE channels (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

-- Document Type
CREATE TABLE document_types (
    code VARCHAR(30) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_document_types_category ON document_types(category);
```

### V2__seed_master_data.sql

Seed ISO 4217 currencies (major), ISO 3166 countries, and sample channels/document types.

---

## 7. Integration with Existing Modules

### Read Interface

Other modules access master data through `MasterDataQueryService` (in-process). No JPA relationship joins — modules store codes like `currencyCode VARCHAR(3)` as plain strings.

### Future Event Publishing

When admin deactivates a currency or country, publish event so downstream modules can react:
- `CurrencyDeactivatedEvent` — Payments can reject new payments in that currency
- `CountryDeactivatedEvent` — Customer can flag accounts in that jurisdiction

For initial implementation, events are not needed (read-only queries sufficient).

---

## 8. Error Handling

### Exception Classes

| Exception | Error Code | HTTP Status | Trigger |
|-----------|-----------|-------------|---------|
| `CurrencyNotFoundException` | MDATA-001 | 404 | Currency code not found |
| `CurrencyAlreadyExistsException` | MDATA-002 | 409 | Duplicate currency code |
| `CountryNotFoundException` | MDATA-003 | 404 | Country ISO code not found |
| `IndustryNotFoundException` | MDATA-004 | 404 | Industry code not found |
| `ExchangeRateNotFoundException` | MDATA-005 | 404 | No rate found for currency pair + date |
| `HolidayNotFoundException` | MDATA-006 | 404 | Holiday not found |
| `BranchNotFoundException` | MDATA-007 | 404 | Branch code not found |
| `BranchAlreadyExistsException` | MDATA-008 | 409 | Duplicate branch code |
| `ChannelNotFoundException` | MDATA-009 | 404 | Channel code not found |
| `ChannelAlreadyExistsException` | MDATA-010 | 409 | Duplicate channel code |
| `DocumentTypeNotFoundException` | MDATA-011 | 404 | Document type code not found |
| `DocumentTypeAlreadyExistsException` | MDATA-012 | 409 | Duplicate document type code |

### Global Exception Handler

```java
@RestControllerAdvice(basePackages = "com.banking.masterdata")
public class MasterDataExceptionHandler {
    // Follows existing pattern from GlobalExceptionHandler in customer module
}
```

---

## 9. Testing Strategy

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
public class CurrencyNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "MDATA-001";
    // ...
}
```

Full list: MDATA-001 through MDATA-012 (see Section 8 Error Handling).

### Module-Specific Unit Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `CurrencyServiceTest` | CRUD, deactivation | S1.1-S1.6 |
| `CountryServiceTest` | CRUD | S2.1-S2.2 |
| `IndustryServiceTest` | CRUD, hierarchy | S3.1-S3.2 |
| `ExchangeRateServiceTest` | CRUD, latest query, conversion | S4.1-S4.5 |
| `HolidayServiceTest` | CRUD, isHoliday query | S5.1-S5.5 |
| `BranchServiceTest` | CRUD | S6.1-S6.3 |
| `ChannelServiceTest` | CRUD | S7.1-S7.2 |
| `DocumentTypeServiceTest` | CRUD, category filter | S8.1-S8.3 |

### Module-Specific Integration Tests

| Test Class | Covers | BDD Scenarios |
|------------|--------|---------------|
| `CurrencyControllerTest` | Currency API endpoints | S1.1-S1.6 |
| `ExchangeRateControllerTest` | Exchange rate API + conversion | S4.1-S4.5 |
| `HolidayControllerTest` | Holiday API + isHoliday | S5.1-S5.5 |
| `BranchControllerTest` | Branch API | S6.1-S6.3 |
| `DocumentTypeControllerTest` | Document type API | S8.1-S8.3 |
