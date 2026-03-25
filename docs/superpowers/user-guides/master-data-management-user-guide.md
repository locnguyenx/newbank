# Master Data Management Module - User Guide

**Module:** `com.banking.masterdata`  
**Version:** 1.0  
**Date:** 2026-03-24

---

## Overview

The Master Data Management module provides core reference data used throughout the banking system. This includes currencies, countries, branches, industries, document types, channels, exchange rates, and holidays.

## Supported Data Types

| Category | Description |
|---------|-------------|
| **Currencies** | Currency codes, names, and properties |
| **Countries** | Country codes, names, and regions |
| **Branches** | Bank branch information |
| **Industries** | Industry classifications |
| **Document Types** | KYC document types |
| **Channels** | Banking channels (ATM, Branch, Mobile, etc.) |
| **Exchange Rates** | Currency exchange rates |
| **Holidays** | Country-specific holidays |

---

## API Reference

### Currencies

#### Create Currency

```http
POST /api/master-data/currencies
Content-Type: application/json

{
  "code": "JPY",
  "name": "Japanese Yen",
  "symbol": "¥",
  "decimalPlaces": 0,
  "isActive": true
}
```

#### List Currencies

```http
GET /api/master-data/currencies?activeOnly=true
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `activeOnly` | Boolean | Return only active currencies (default: false) |

**Response:** `200 OK`
```json
[
  {
    "code": "USD",
    "name": "US Dollar",
    "symbol": "$",
    "decimalPlaces": 2,
    "isActive": true
  },
  {
    "code": "EUR",
    "name": "Euro",
    "symbol": "€",
    "decimalPlaces": 2,
    "isActive": true
  }
]
```

#### Get Currency

```http
GET /api/master-data/currencies/{code}
```

#### Update Currency

```http
PUT /api/master-data/currencies/{code}
Content-Type: application/json

{
  "name": "US Dollar Updated",
  "symbol": "$",
  "decimalPlaces": 2,
  "isActive": true
}
```

#### Deactivate Currency

```http
PUT /api/master-data/currencies/{code}/deactivate
```

---

### Countries

#### List Countries

```http
GET /api/master-data/countries
```

**Response:** `200 OK`
```json
[
  {
    "code": "US",
    "name": "United States",
    "region": "NORTH_AMERICA",
    "currencyCode": "USD",
    "isActive": true
  },
  {
    "code": "GB",
    "name": "United Kingdom",
    "region": "EUROPE",
    "currencyCode": "GBP",
    "isActive": true
  }
]
```

#### Get Country

```http
GET /api/master-data/countries/{code}
```

---

### Branches

#### List Branches

```http
GET /api/master-data/branches
```

**Response:** `200 OK`
```json
[
  {
    "code": "HQ",
    "name": "Head Office",
    "address": "123 Main Street",
    "city": "New York",
    "countryCode": "US",
    "isActive": true
  },
  {
    "code": "BR001",
    "name": "Downtown Branch",
    "address": "456 Business Ave",
    "city": "New York",
    "countryCode": "US",
    "isActive": true
  }
]
```

#### Get Branch

```http
GET /api/master-data/branches/{code}
```

---

### Industries

#### List Industries

```http
GET /api/master-data/industries
```

**Response:** `200 OK`
```json
[
  {
    "code": "IT001",
    "name": "Information Technology",
    "sector": "TECHNOLOGY"
  },
  {
    "code": "BANK",
    "name": "Banking & Financial Services",
    "sector": "FINANCE"
  }
]
```

#### Get Industry

```http
GET /api/master-data/industries/{code}
```

---

### Document Types

#### List Document Types

```http
GET /api/master-data/document-types
```

**Response:** `200 OK`
```json
[
  {
    "code": "PASSPORT",
    "name": "Passport",
    "category": "IDENTITY",
    "validityMonths": 120
  },
  {
    "code": "NATIONAL_ID",
    name": "National ID Card",
    "category": "IDENTITY",
    "validityMonths": 60
  }
]
```

---

### Channels

#### List Channels

```http
GET /api/master-data/channels
```

**Response:** `200 OK`
```json
[
  {
    "code": "BRANCH",
    "name": "Branch",
    "isActive": true
  },
  {
    "code": "MOBILE",
    "name": "Mobile Banking",
    "isActive": true
  },
  {
    "code": "ONLINE",
    "name": "Online Banking",
    "isActive": true
  }
]
```

---

### Exchange Rates

#### Get Exchange Rate

```http
GET /api/master-data/exchange-rates?fromCurrency=USD&toCurrency=EUR
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `fromCurrency` | String | Source currency code |
| `toCurrency` | String | Target currency code |

**Response:** `200 OK`
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.92,
  "effectiveDate": "2026-03-24"
}
```

#### List All Exchange Rates

```http
GET /api/master-data/exchange-rates
```

---

### Holidays

#### List Holidays

```http
GET /api/master-data/holidays?countryCode=US&year=2026
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `countryCode` | String | Country code |
| `year` | Integer | Year |

**Response:** `200 OK`
```json
[
  {
    "date": "2026-01-01",
    "name": "New Year's Day",
    "countryCode": "US"
  },
  {
    "date": "2026-07-04",
    "name": "Independence Day",
    "countryCode": "US"
  }
]
```

---

## Seeded Data

### Currencies (Pre-loaded)

| Code | Name | Symbol |
|------|------|--------|
| USD | US Dollar | $ |
| EUR | Euro | € |
| GBP | British Pound | £ |
| JPY | Japanese Yen | ¥ |

### Countries (Pre-loaded)

| Code | Name | Region |
|------|------|--------|
| US | United States | NORTH_AMERICA |
| GB | United Kingdom | EUROPE |
| DE | Germany | EUROPE |
| JP | Japan | ASIA |

### Channels (Pre-loaded)

| Code | Name |
|------|------|
| BRANCH | Branch |
| ATM | ATM |
| MOBILE | Mobile Banking |
| ONLINE | Online Banking |
| API | API Channel |

---

## Integration Points

### Customer Module
- Country codes for addresses
- Industry codes for business classification

### Account Module
- Currency validation for accounts
- Exchange rates for multi-currency operations

### Product Module
- Currency availability in products

### Charges Module
- Currency for charge calculations

---

## Best Practices

1. **Currency Management**
   - Deactivate currencies rather than delete
   - Maintain accurate decimal places
   - Update exchange rates regularly

2. **Reference Data Maintenance**
   - Review holidays quarterly
   - Keep branch information current
   - Update document validity periods

3. **Data Integrity**
   - Use standard ISO country codes
   - Use standard currency codes (ISO 4217)
   - Maintain consistent naming conventions

---

## Support

For issues or questions:
1. Verify currency code format (ISO 4217)
2. Check country code format (ISO 3166-1)
3. Contact system administrator
