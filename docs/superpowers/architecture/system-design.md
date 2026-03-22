# Corporate & SME Banking System Design

**Version:** 1.0  
**Date:** 2026-03-19  
**Status:** Draft

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [System Architecture](#2-system-architecture)
3. [Foundation Modules](#3-foundation-modules)
4. [Business Modules](#4-business-modules)
5. [Integration Architecture](#5-integration-architecture)
6. [Technology Stack](#6-technology-stack)
7. [Internal Module Integration](#7-internal-module-integration)
8. [Security Architecture](#8-security-architecture)
9. [Deployment Architecture](#9-deployment-architecture)
10. [Quality Attributes](#10-quality-attributes)
11. [Compliance & Regulatory](#11-compliance--regulatory)

---

## 1. Executive Summary

This document defines the high-level architecture for a modern digital banking system serving **Corporate and SME customers**. The system provides comprehensive banking services including account management, cash management, trade finance, and payments.

### Key Design Principles

- **Modular Monolith** architecture for simplicity and data consistency
- **Foundation/Business module separation** for clean dependencies
- **Event-driven** internal communication for loose coupling
- **API-first** design for external integrations
- **Security by design** with encryption, audit trails, and compliance

### Scope

| In Scope | Out of Scope |
|----------|--------------|
| Business Banking Accounts | Retail/Consumer Banking |
| Cash Management Services | Investment Banking |
| Trade Finance | Insurance Products |
| Domestic & International Payments | Wealth Management |
| Customer Management | Mobile Banking App (Phase 2) |
| Product Configuration | Web Portal UI (Phase 2) |

---

## 2. System Architecture

### Architecture Style: Modular Monolith

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Banking Platform                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Business Modules                          │   │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │   │
│  │  │ Cash         │ │ Trade        │ │ Payments     │        │   │
│  │  │ Management   │ │ Finance      │ │              │        │   │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                              ▲                                       │
│                              │ depends on                            │
│                              ▼                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                   Foundation Modules                         │   │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │   │
│  │  │ Customer     │ │ Account      │ │ Product      │        │   │
│  │  │ Management   │ │ Management   │ │ Configuration│        │   │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │   │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │   │
│  │  │ Limits       │ │ Charges      │ │ Master Data  │        │   │
│  │  │ Management   │ │ Management   │ │ Management   │        │   │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
├─────────────────────────────────────────────────────────────────────┤
│                        Infrastructure                                │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────┐  │
│  │ Database     │ │ Message      │ │ Cache        │ │ Search   │  │
│  │ PostgreSQL   │ │ Queue        │ │ Redis        │ │ Elastic  │  │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

### Module Dependency Rules

```
Foundation Modules (STABLE)
    ↓
Business Modules (EVOLVING)
    ↓
External Integrations (VARIABLE)

RULE: Dependencies flow DOWN only. Foundation modules NEVER depend on business modules.
```

### Communication Patterns

| Pattern | Use Case | Example |
|---------|----------|---------|
| **Direct Interface Call** | Synchronous operations within process | Customer lookup during payment |
| **Event Publishing** | Asynchronous notifications | Customer created, account opened |
| **Domain Events** | Cross-module state changes | KYC status changed, limit exceeded |
| **Command Query Separation** | Read vs Write operations | Queries use read replicas, commands use primary |

---

## 3. Foundation Modules

Foundation modules provide core services used by all business modules. They are **stable** and change infrequently.

### 3.1 Customer Management

**Purpose:** Manage corporate and SME customer lifecycle, KYC/AML, and authorization

| Aspect | Details |
|--------|---------|
| **Entities** | Customer, EntityRelationship, AuthorizedSignatory, Role, Permission |
| **Key Services** | Customer onboarding, KYC verification, signatory management |
| **Dependencies** | External KYC providers (sanctions, PEP screening) |
| **Data Owned** | customers, entity_relationships, authorized_signatories, kyc_checks, documents |
| **Spec** | [customer-management-spec.md](./modules/customer-management-spec.md) |

### 3.2 Account Management

**Purpose:** Manage business banking account lifecycle, balances, and sub-accounts

| Aspect | Details |
|--------|---------|
| **Entities** | Account, SubAccount, Balance, Statement, AccountHolder |
| **Key Services** | Account opening, balance inquiries, statement generation |
| **Dependencies** | Customer Management, Product Configuration |
| **Data Owned** | accounts, sub_accounts, balances, statements |
| **Spec** | [account-management-spec.md](./modules/account-management-spec.md) |

### 3.3 Product Configuration

**Purpose:** Define banking products, features, and pricing rules

| Aspect | Details |
|--------|---------|
| **Entities** | Product, ProductCategory, Feature, PricingRule, ProductBundle |
| **Key Services** | Product catalog, feature toggles, pricing engine |
| **Dependencies** | Master Data Management |
| **Data Owned** | products, product_categories, features, pricing_rules |
| **Spec** | [product-configuration-spec.md](./modules/product-configuration-spec.md) |

### 3.4 Limits Management

**Purpose:** Configure and enforce transaction limits and approval thresholds

| Aspect | Details |
|--------|---------|
| **Entities** | Limit, LimitRule, ApprovalThreshold, LimitOverride |
| **Key Services** | Limit checking, approval workflows, limit monitoring |
| **Dependencies** | Customer Management, Account Management |
| **Data Owned** | limits, limit_rules, approval_thresholds |
| **Spec** | [limits-management-spec.md](./modules/limits-management-spec.md) |

### 3.5 Charges Management

**Purpose:** Calculate and apply fees, commissions, and interest

| Aspect | Details |
|--------|---------|
| **Entities** | Charge, ChargeRule, InterestRate, FeeSchedule |
| **Key Services** | Charge calculation, fee application, interest accrual |
| **Dependencies** | Product Configuration, Master Data |
| **Data Owned** | charges, charge_rules, interest_rates |
| **Spec** | [charges-management-spec.md](./modules/charges-management-spec.md) |

### 3.6 Master Data Management

**Purpose:** Maintain reference data (currencies, countries, industries, etc.)

| Aspect | Details |
|--------|---------|
| **Entities** | Currency, Country, Industry, ExchangeRate, Holiday |
| **Key Services** | Reference data queries, exchange rate updates |
| **Dependencies** | None (leaf module) |
| **Data Owned** | currencies, countries, industries, exchange_rates |
| **Spec** | [master-data-spec.md](./modules/master-data-spec.md) |

---

## 4. Business Modules

Business modules implement specific banking services and depend on foundation modules.

### 4.1 Cash Management Services

**Purpose:** Provide cash flow management tools for corporate treasury

| Aspect | Details |
|--------|---------|
| **Services** | Payroll processing, receivables management, liquidity optimization |
| **Dependencies** | Customer, Account, Limits, Charges |
| **Key Features** | Batch payments, auto-collection, cash pooling |
| **Spec** | [cash-management-spec.md](./modules/cash-management-spec.md) |

### 4.2 Trade Finance

**Purpose:** Facilitate international trade through financial instruments

| Aspect | Details |
|--------|---------|
| **Services** | Letters of credit, bank guarantees, documentary collections |
| **Dependencies** | Customer, Account, Product, Limits |
| **Key Features** | LC issuance/amendment, guarantee issuance, document verification |
| **Spec** | [trade-finance-spec.md](./modules/trade-finance-spec.md) |

### 4.3 Payments

**Purpose:** Process domestic and international fund transfers

| Aspect | Details |
|--------|---------|
| **Services** | Domestic transfers, international wire transfers, SWIFT messaging |
| **Dependencies** | Customer, Account, Limits, Charges |
| **Key Features** | Payment initiation, routing, settlement, reconciliation |
| **Spec** | [payments-spec.md](./modules/payments-spec.md) |

---

## 5. Integration Architecture

A digital banking system does not operate in isolation. It must integrate with:

1. **Core Banking System** - The bank's existing core banking platform
2. **Payment Networks** - SWIFT, SEPA, FedWire, ACH, RTGS
3. **Third-Party Services** - KYC providers, credit bureaus, regulatory systems
4. **Internal Bank Systems** - Treasury, risk management, compliance

### 5.1 Core Banking System Integration

The Digital Banking Platform integrates with the bank's Core Banking System (CBS) as a **front-end channel**. The CBS remains the **system of record** for core banking operations.

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Digital Banking Platform                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  • Customer Management                                      │   │
│  │  • Account Management (local cache)                         │   │
│  │  • Cash Management                                          │   │
│  │  • Trade Finance                                            │   │
│  │  • Payments                                                 │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ Integration Layer
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    Core Banking System (CBS)                         │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  • Account Master (System of Record)                        │   │
│  │  • General Ledger                                           │   │
│  │  • Transaction Processing                                   │   │
│  │  • Regulatory Reporting                                     │   │
│  │  • Settlement & Clearing                                    │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

**Integration Patterns:**

| Pattern | Use Case | Example |
|---------|----------|---------|
| **Request-Reply** | Synchronous queries | Account balance inquiry |
| **Fire-and-Forget** | Asynchronous notifications | Customer created event |
| **Batch Processing** | Bulk operations | End-of-day reconciliation |
| **Event Streaming** | Real-time updates | Transaction posted event |

**Data Synchronization:**

| Data Type | Source of Truth | Sync Direction | Frequency |
|-----------|----------------|----------------|-----------|
| **Accounts** | CBS → Digital Platform | Pull | Real-time + Batch |
| **Balances** | CBS → Digital Platform | Pull | Real-time |
| **Transactions** | CBS → Digital Platform | Push | Real-time |
| **Customers** | Digital Platform → CBS | Push | On create/update |
| **Payments** | Digital Platform → CBS | Push | On submission |

### 5.2 Payment Network Integration

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Payments Module                                   │
└─────────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    SWIFT     │     │    SEPA      │     │   FedWire    │
│   Network    │     │   Network    │     │   (US)       │
└──────────────┘     └──────────────┘     └──────────────┘
        │                     │                     │
        └─────────────────────┼─────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    Core Banking System                               │
│  (Settlement & Clearing)                                            │
└─────────────────────────────────────────────────────────────────────┘
```

**Payment Networks:**

| Network | Type | Coverage | Message Format |
|---------|------|----------|----------------|
| **SWIFT** | International | Global | MT/MX (ISO 20022) |
| **SEPA** | Regional (EU) | Europe | ISO 20022 |
| **FedWire** | Domestic (US) | USA | Fedwire |
| **ACH** | Domestic (US) | USA | NACHA |
| **RTGS** | Domestic | Varies | Local standards |
| **FPS** | Real-time (UK) | UK | ISO 20022 |

### 5.3 Third-Party Service Integration

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Digital Banking Platform                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Integration Gateway                                        │   │
│  │  • API Management                                           │   │
│  │  • Rate Limiting                                            │   │
│  │  • Circuit Breakers                                         │   │
│  │  • Retry Logic                                              │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   KYC/AML    │     │   Credit     │     │  Regulatory  │
│  Providers   │     │   Bureaus    │     │  Reporting   │
└──────────────┘     └──────────────┘     └──────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Dow Jones   │     │   Dun &      │     │   Central    │
│  Refinitiv   │     │   Bradstreet │     │   Bank       │
└──────────────┘     └──────────────┘     └──────────────┘
```

**Third-Party Services:**

| Service | Purpose | Provider Examples | Integration |
|---------|---------|-------------------|-------------|
| **Sanctions Screening** | AML compliance | Dow Jones, Refinitiv, World-Check | REST API |
| **PEP Screening** | AML compliance | Dow Jones, Refinitiv | REST API |
| **Identity Verification** | KYC | Jumio, Onfido, Trulioo | REST API |
| **Credit Scoring** | Risk assessment | Dun & Bradstreet, Experian | REST API |
| **Document Verification** | KYC | Onfido, Veriff | REST API |
| **Address Validation** | Data quality | Loqate, SmartyStreets | REST API |
| **Tax ID Validation** | Compliance | IRS (US), Local tax authorities | REST API |

### 5.4 Integration Gateway Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Integration Gateway                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  API Management                                             │   │
│  │  • API Versioning (v1, v2, ...)                             │   │
│  │  • Rate Limiting (per client, per endpoint)                 │   │
│  │  • Authentication (OAuth2, API Keys)                        │   │
│  │  • Request/Response Logging                                 │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Resilience Patterns                                        │   │
│  │  • Circuit Breakers (Resilience4j)                          │   │
│  │  • Retry with Exponential Backoff                           │   │
│  │  • Timeouts (connect, read, write)                          │   │
│  │  • Bulkhead Isolation                                       │   │
│  │  • Fallback Responses                                       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Data Transformation                                        │   │
│  │  • Message Format Conversion (JSON, XML, SWIFT MT)          │   │
│  │  • Field Mapping (internal ↔ external)                      │   │
│  │  • Data Validation                                          │   │
│  │  • Schema Evolution                                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Monitoring & Observability                                 │   │
│  │  • Distributed Tracing (Jaeger/Zipkin)                      │   │
│  │  • Metrics (Prometheus)                                     │   │
│  │  • Alerting (PagerDuty, OpsGenie)                           │   │
│  │  • SLA Monitoring                                           │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

**Integration Specifications:**

| Specification | Description | Format |
|---------------|-------------|--------|
| **ISO 20022** | Universal financial industry message scheme | XML, JSON |
| **SWIFT MT** | SWIFT message types | Fixed-length text |
| **FIX** | Financial Information eXchange | Tag-value pairs |
| **REST** | Representational State Transfer | JSON over HTTP |
| **SOAP** | Simple Object Access Protocol | XML over HTTP |
| **MQ** | Message Queue (IBM MQ, RabbitMQ) | Various |

---

## 5.5 API Contract Enforcement

To prevent API drift between backend and frontend, OpenAPI 3.0 is used as the single source of truth for all REST API contracts.

### Contract-First Workflow

```
┌──────────────────────────────────────────────────────────────────┐
│                       API Development Flow                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  1. Define/Update OpenAPI Spec (docs/api/openapi.yaml)            │
│     • Add new endpoint paths, request/response schemas           │
│     • Define headers, query params, error responses             │
│                                                                   │
│  2. Backend Implementation                                       │
│     • Springdoc OpenAPI auto-generates spec from @RequestMapping │
│     • Implement controller & DTOs (must match spec)             │
│     • Run ./gradlew openapiValidate to verify contract          │
│                                                                   │
│  3. Frontend Synchronization                                    │
│     • npm run generate-api (regenerates TS types & client)      │
│     • Use generated apiClient in Redux slices                   │
│     • No manual endpoint strings or mock data                  │
│                                                                   │
│  4. CI/CD Gate                                                  │
│     • Fail if backend drifts from spec                          │
│     • Fail if generated frontend files not committed            │
│     • Run contract tests (see below)                            │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

### Tooling

| Purpose | Tool | Usage |
|---------|------|-------|
| **Spec Definition** | Springdoc OpenAPI | Annotate Spring controllers → auto-generates `openapi.yaml` |
| **Frontend Generation** | openapi-generator / orval | Generate TypeScript types & Axios client from spec |
| **Mock Server** | Prism / WireMock | Generate mock HTTP server from spec for frontend dev |
| **Validation** | openapi-generator-cli | `openapi-generator-cli validate -i openapi.yaml` |
| **Live Docs** | Swagger UI | `/api-docs` endpoint (auto by Springdoc) |

### Contract Tests

In addition to OpenAPI validation, add unit tests that verify DTO serialization:

```java
// Example: CustomerResponseApiContractTest
@Test
void customerResponse_hasCorrectFieldNames() throws Exception {
    CustomerResponse response = // ... create entity and map
    String json = objectMapper.writeValueAsString(response);
    assertThat(json).contains("annualRevenueAmount");
    assertThat(json).doesNotContain("annualRevenue");  // Wrong field name
}
```

### Enforced Rules

❌ **DO NOT:**
- Manually write TypeScript interface for API response (must be generated)
- Hardcode endpoint URLs in frontend (must use generated client)
- Commit uncommitted generated files (CI will check)
- Change API without updating OpenAPI spec first

✅ **REQUIRED:**
- All REST endpoints must have `@Operation(summary="...")` annotation
- All DTO fields must use exact names that match spec
- Frontend must call `npm run generate-api` after any backend change
- Generated files (`src/api/client.ts`, `src/types/*.types.ts`) must be committed

### Benefits

- **No more contract drift** – frontend always matches backend
- **Parallel development** – frontend can use mock server while backend builds
- **Live documentation** – Swagger UI auto-updates with code changes
- **Breaking change detection** – any API change requires spec update → visible to all

---

## 6. Technology Stack

### Frontend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| **Framework** | React | 18.x | Component-based, large ecosystem, TypeScript support |
| **Language** | TypeScript | 5.x | Type safety, better IDE support, maintainability |
| **State Management** | Redux Toolkit | 2.x | Predictable state, dev tools, middleware support |
| **UI Library** | Ant Design | 5.x | Enterprise components, i18n, accessibility |
| **Build Tool** | Vite | 5.x | Fast HMR, optimized builds, modern tooling |
| **Routing** | React Router | 6.x | Declarative routing, code splitting |
| **HTTP Client** | Axios | 1.x | Interceptors, request/response transforms |
| **Forms** | React Hook Form | 7.x | Performance, validation, easy integration |
| **Charts** | Recharts | 2.x | Responsive charts, composable components |
| **Testing** | Jest + React Testing Library | Latest | Unit testing, component testing |
| **E2E Testing** | Playwright | 1.x | Cross-browser, reliable, fast |

### Frontend Architecture

```
src/
├── app/                          # Application root
│   ├── App.tsx
│   ├── routes.tsx
│   └── store.ts
├── features/                     # Feature modules
│   ├── dashboard/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   └── store/
│   ├── customers/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   └── store/
│   ├── accounts/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   └── store/
│   ├── payments/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   └── store/
│   └── trade-finance/
│       ├── components/
│       ├── hooks/
│       ├── services/
│       └── store/
├── shared/                       # Shared utilities
│   ├── components/               # Reusable UI components
│   ├── hooks/                    # Custom hooks
│   ├── utils/                    # Utility functions
│   ├── types/                    # TypeScript types
│   └── constants/                # Constants
├── api/                          # API client
│   ├── client.ts                 # Axios instance
│   ├── interceptors.ts
│   └── types.ts
└── assets/                       # Static assets
    ├── images/
    ├── icons/
    └── styles/
```

### Frontend Features

| Feature | Description | Key Components |
|---------|-------------|----------------|
| **Dashboard** | Overview, KPIs, recent activity | Charts, stats cards, activity feed |
| **Customer Management** | Customer onboarding, KYC, signatories | Forms, tables, document viewer |
| **Account Management** | Account details, balances, statements | Account cards, balance charts, transaction list |
| **Payments** | Payment initiation, history, templates | Payment forms, approval workflow, status tracker |
| **Trade Finance** | LC issuance, guarantees, collections | Document viewer, workflow tracker |
| **Cash Management** | Payroll, receivables, liquidity | Batch upload, payment scheduler, reports |
| **Reports** | Regulatory, operational, analytics | Report builder, charts, export |
| **Settings** | User management, preferences, notifications | Forms, tables, permission matrix |

### Backend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| **Language** | Java | 17 | Enterprise standard, strong typing, mature ecosystem |
| **Framework** | Spring Boot | 3.x | Industry standard for banking, excellent transaction management |
| **Build Tool** | Maven/Gradle | 3.9/8.x | Dependency management, build automation |
| **ORM** | Spring Data JPA | 3.x | Database abstraction, repository pattern |
| **Validation** | Bean Validation | 3.x | Input validation, custom validators |
| **Security** | Spring Security | 6.x | Authentication, authorization, OAuth2/JWT |
| **API Documentation** | Springdoc OpenAPI | 2.x | Auto-generate OpenAPI 3.0 spec from code, Swagger UI |
| **Messaging** | Spring Kafka | 3.x | Event streaming, async processing |
| **Testing** | JUnit 5, Testcontainers | 5.x | Unit/integration testing, database testing |

### Database & Storage

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| **Primary Database** | PostgreSQL | 15+ | ACID compliance, JSON support, enterprise features |
| **Cache** | Redis | 7.x | Session management, frequently accessed data |
| **Search** | Elasticsearch | 8.x | Transaction search, document indexing |
| **File Storage** | S3-compatible | - | Document storage, statement archival |
| **Migration** | Flyway | 9.x | Database versioning, repeatable migrations |

### Infrastructure

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| **Containerization** | Docker | 24.x | Consistent environments, easy deployment |
| **Orchestration** | Kubernetes | 1.28+ | Container management, scaling, health checks |
| **API Gateway** | Kong/Spring Cloud Gateway | 3.x | Rate limiting, authentication, routing |
| **Monitoring** | Prometheus + Grafana | Latest | Metrics collection, dashboards |
| **Logging** | ELK Stack | 8.x | Centralized logging, log analysis |
| **Tracing** | Jaeger/Zipkin | Latest | Distributed tracing, performance analysis |
| **Secrets** | HashiCorp Vault | 1.x | Secrets management, encryption keys |

### External Integrations

| Service | Purpose | Protocol |
|---------|---------|----------|
| **KYC Providers** | Sanctions screening, PEP checks | REST API |
| **Payment Networks** | SWIFT, SEPA, FedWire | ISO 20022, SWIFT MT/MX |
| **Credit Bureaus** | Business credit checks | REST API |
| **Regulatory** | Reporting, compliance | SFTP, REST API |

---

## 7. Internal Module Integration

### Module Communication

```
┌─────────────────────────────────────────────────────────────────┐
│                    Application Layer                             │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                  Module Interfaces                       │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐       │   │
│  │  │ Customer    │ │ Account     │ │ Product     │       │   │
│  │  │ Service     │ │ Service     │ │ Service     │       │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘       │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                   │
│                              ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                  Event Bus (Kafka)                       │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐       │   │
│  │  │ Customer    │ │ Account     │ │ Transaction │       │   │
│  │  │ Events      │ │ Events      │ │ Events      │       │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘       │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

### External Integration

```
┌─────────────────────────────────────────────────────────────────┐
│                      API Gateway                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  • Authentication (OAuth2/JWT)                          │   │
│  │  • Rate Limiting                                        │   │
│  │  • Request Routing                                      │   │
│  │  • API Versioning                                       │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Banking Platform                              │
└─────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   KYC        │     │   Payment    │     │  Regulatory  │
│   Providers  │     │   Networks   │     │  Reporting   │
└──────────────┘     └──────────────┘     └──────────────┘
```

### Event Schema

All events follow a standard envelope:

```json
{
  "eventId": "uuid",
  "eventType": "CustomerCreated",
  "timestamp": "2026-03-19T10:30:00Z",
  "source": "customer-management",
  "correlationId": "uuid",
  "data": {
    "customerId": "uuid",
    "customerType": "CORPORATE",
    "kycStatus": "PENDING"
  },
  "metadata": {
    "version": "1.0",
    "actor": "user-id-or-system"
  }
}
```

---

## 8. Security Architecture

### Authentication & Authorization

```
┌─────────────────────────────────────────────────────────────────┐
│                    Security Layers                               │
├─────────────────────────────────────────────────────────────────┤
│  Layer 1: API Gateway                                           │
│  • OAuth2/JWT validation                                        │
│  • Rate limiting per client                                     │
│  • IP whitelisting                                              │
├─────────────────────────────────────────────────────────────────┤
│  Layer 2: Application                                           │
│  • Role-Based Access Control (RBAC)                             │
│  • Attribute-based policies                                     │
│  • Multi-factor authentication for sensitive ops                │
├─────────────────────────────────────────────────────────────────┤
│  Layer 3: Data                                                  │
│  • Field-level encryption (AES-256-GCM)                         │
│  • Row-level security                                           │
│  • Audit logging for all access                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Data Protection

| Data Type | Protection | Storage |
|-----------|-----------|---------|
| **Account Numbers** | Field-level encryption | Encrypted at rest |
| **Tax IDs (SSN/EIN)** | Field-level encryption | Encrypted at rest |
| **Personal Names** | Standard encryption | Encrypted at rest |
| **Transaction Data** | Standard encryption | Encrypted at rest |
| **Documents** | S3 server-side encryption | Encrypted at rest |
| **Passwords** | Bcrypt hashing | Never stored plaintext |

### Audit Trail

All financial operations generate immutable audit records:

```json
{
  "auditId": "uuid",
  "timestamp": "2026-03-19T10:30:00Z",
  "actor": "user-id",
  "action": "CREATE",
  "resource": "customer",
  "resourceId": "customer-uuid",
  "before": null,
  "after": { "legalName": "Acme Corp" },
  "ipAddress": "192.168.1.1",
  "userAgent": "Mozilla/5.0...",
  "correlationId": "uuid"
}
```

---

## 9. Deployment Architecture

### Environment Topology

```
┌─────────────────────────────────────────────────────────────────┐
│                         Production                                │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │   App Node 1 │  │   App Node 2 │  │   App Node 3 │         │
│  │  (Kubernetes)│  │  (Kubernetes)│  │  (Kubernetes)│         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
│                              │                                   │
│                              ▼                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │  Primary DB  │  │  Replica DB  │  │  Replica DB  │         │
│  │  (Write)     │  │  (Read)      │  │  (Read)      │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
│                              │                                   │
│                              ▼                                   │
│  ┌──────────────┐  ┌──────────────┐                            │
│  │  Kafka       │  │  Redis       │                            │
│  │  Cluster     │  │  Cluster     │                            │
│  └──────────────┘  └──────────────┘                            │
└─────────────────────────────────────────────────────────────────┘
```

### Deployment Strategy

| Strategy | Use Case | Rollback Time |
|----------|----------|---------------|
| **Blue-Green** | Major releases | < 5 minutes |
| **Canary** | Gradual rollouts | < 1 minute |
| **Feature Flags** | A/B testing, gradual enablement | Instant |
| **Database Migrations** | Schema changes | < 10 minutes |

---

## 10. Quality Attributes

### Performance Targets

| Metric | Target | Measurement |
|--------|--------|-------------|
| **API Response Time (p95)** | < 200ms | Prometheus metrics |
| **API Response Time (p99)** | < 500ms | Prometheus metrics |
| **Throughput** | 1000 TPS | Load testing |
| **Database Query Time** | < 50ms (p95) | APM tracing |
| **Page Load Time** | < 3 seconds | Real user monitoring |

### Availability Targets

| Metric | Target | Strategy |
|--------|--------|----------|
| **Uptime** | 99.9% | Multi-node deployment, health checks |
| **Recovery Time (RTO)** | < 4 hours | Automated failover |
| **Recovery Point (RPO)** | < 15 minutes | Continuous replication |
| **Planned Downtime** | < 4 hours/month | Blue-green deployment |

### Scalability

| Dimension | Strategy |
|-----------|----------|
| **Horizontal Scaling** | Kubernetes auto-scaling |
| **Database Scaling** | Read replicas, connection pooling |
| **Cache Scaling** | Redis cluster mode |
| **Message Queue Scaling** | Kafka partitioning |

---

## 11. Compliance & Regulatory

### Regulatory Frameworks

| Framework | Requirements | Implementation |
|-----------|-------------|----------------|
| **KYC/AML** | Customer identification, sanctions screening | Automated KYC workflow, external provider integration |
| **FATCA** | US tax reporting | Customer classification, reporting engine |
| **CRS** | Common Reporting Standard | Multi-jurisdiction reporting |
| **Basel III** | Capital adequacy, risk management | Risk scoring, exposure calculation |
| **PSD2** | Payment services (EU) | Strong customer authentication, API access |
| **GDPR** | Data protection (EU) | Consent management, data portability, right to erasure |

### Audit Requirements

| Requirement | Implementation |
|-------------|----------------|
| **Immutable Audit Trail** | Write-once append-only log |
| **Transaction Logging** | All financial operations logged |
| **Access Logging** | All data access logged |
| **Change Logging** | All configuration changes logged |
| **Retention Period** | 7 years (configurable per jurisdiction) |

### Reporting

| Report | Frequency | Recipient |
|--------|-----------|-----------|
| **Suspicious Activity Reports** | As needed | Financial Intelligence Unit |
| **Large Transaction Reports** | Daily | Regulatory authority |
| **Customer Risk Reports** | Monthly | Compliance team |
| **Transaction Monitoring** | Real-time | Compliance system |

---

## Related Documents

- [Master Implementation Plan](./master-implementation-plan.md)
- [Customer Management Spec](./modules/customer-management-spec.md)
- [Account Management Spec](./modules/account-management-spec.md)
- [Cash Management Spec](./modules/cash-management-spec.md)
- [Trade Finance Spec](./modules/trade-finance-spec.md)
- [Payments Spec](./modules/payments-spec.md)
- [Product Configuration Spec](./modules/product-configuration-spec.md)
- [Limits Management Spec](./modules/limits-management-spec.md)
- [Charges Management Spec](./modules/charges-management-spec.md)
- [Master Data Spec](./modules/master-data-spec.md)
