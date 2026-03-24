# AGENTS.md - Guidelines for Agentic Coding Agents

This document provides instructions and guidelines for AI coding agents working in this repository.

## Project Overview

Modern digital banking system for corporate and SME banking, focusing on business accounts, cash management, and trade finance.

## Architecture

**Modular Monolith** - Single deployable unit with clear module boundaries. ACID transactions, simpler deployment than microservices.

## Technology Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3.2 |
| Build | Gradle (Kotlin DSL) |
| Database | PostgreSQL |
| Migrations | Flyway |
| Frontend | React 18, TypeScript, Vite |
| UI Library | Ant Design |
| State | Redux Toolkit |

## Documentation
- `docs` - at project root
- Master Plan: `docs/superpowers/plans/2026-03-19-master-implementation-plan.md`
- Master System Design: `docs/superpowers/architecture/system-design.md`
- brds location: `docs/superpowers/brds/`
- specs location: `docs/superpowers/specs/`
- module plans: `docs/superpowers/plans/`

## Code Style Guidelines

### Java Backend

**Package structure:** `com.banking.<module>.<layer>`

```
src/main/java/com/banking/customer/
├── domain/
│   ├── entity/           # JPA entities
│   ├── enums/            # Enumerations
│   └── embeddable/       # Embeddable types
├── repository/           # Data access
├── service/              # Business logic
├── controller/           # REST endpoints
└── dto/                  # Data transfer objects
```

**Naming:**
- Classes: `PascalCase` (e.g., `CustomerService`)
- Methods/variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Interfaces: No `I` prefix (e.g., `CustomerRepository`, not `ICustomerRepository`)

**ID strategy:** Auto-increment BIGINT for all entities.

### React Frontend

**Imports order:**
1. React / Node built-ins
2. External packages
3. Internal modules (`@/` aliases)
4. Types (`import type`)
5. Relative imports

**Files:** `kebab-case.tsx`

**Components:** Functional components with hooks, explicit return types.

## Testing Guidelines

**Enforcement**
1. Ensure backend test reports are generated with configuration or add Gradle task
2. Ensure frontend tests with Vitest must generate coverage reports using --coverage flag
3. Add coverage thresholds to Vitest/Gradle config to fail builds below minimum
4. Development workflow or CI/CD must follow these rules

**Usage:** 
- Frontend tests: `npm run test:coverage` generates HTML report in `coverage/` directory
- Backend tests: XML reports automatically generated in `build/test-results/test/`

**Test Strategy**
In your design spec's Testing Strategy section, reference this template: `docs/superpowers/templates/test-strategy.md`

**Frontend Test Requirements:**
- Mock data MUST match actual API response (verify by calling API or checking OpenAPI spec)
- Test BOTH create AND edit modes
- Verify form → API data mapping (not just rendering)
- Test error handling with user-friendly messages
- Common API field names: `type` (not `customerType`), `name` (not `firstName`/`lastName`), `emails[]` (not `email`)

**Flyway Migration Tests**
- `FlywayMigrationIntegrationTest` extends `AbstractIntegrationTest` to use shared PostgreSQL container
- Auto-discovers all `@Entity` classes under `com/banking/**` on the classpath and verifies their tables exist in the schema
- **When adding a new module**, update only `src/main/java/com/banking/BankingApplication.java` — add the new package to `@ComponentScan`, `@EntityScan`, and `@EnableJpaRepositories`. The test auto-discovers entity classes via classpath scanning — no manual registration needed.

## Banking-Specific Guidelines

### Money Handling
- Store amounts as `BigInteger` (minor units) or `java.math.BigDecimal`
- Always store currency code with monetary values
- Use banker's rounding for calculations

### Audit Trail
- All entities include `AuditFields` embeddable
- Track: createdAt, createdBy, updatedAt, updatedBy

### Error Codes & Message System

**MUST use the centralized error handling system with message templates by Message ID.**

See: `docs/superpowers/architecture/error-handling.md`

**Pattern:**
1. Add message code to `MessageCatalog.java` with user-friendly message
2. Create exception extending `BaseException` with static factory methods
3. Register handler in module's `@ExceptionHandler`
4. Add message to frontend `messageService.ts`

**Example:**
```java
// 1. MessageCatalog.java
public static final String ACCOUNT_NON_ZERO_BALANCE = "ACCOUNT_004";

// 2. Exception class
public class InvalidAccountStateException extends BaseException {
    public static InvalidAccountStateException nonZeroBalance() {
        return new InvalidAccountStateException(
            MessageCatalog.ACCOUNT_NON_ZERO_BALANCE, 
            MessageCatalog.getMessage(MessageCatalog.ACCOUNT_NON_ZERO_BALANCE)
        );
    }
}

// 3. Frontend
message.error(getErrorMessage(err));
```

**Code Review Checklist:** `docs/superpowers/guidelines/error-handling-checklist.md`

## Git Workflow

- Use **git worktrees** for isolated feature work
- Worktrees are stored in `.worktrees/` directory
- Never commit to main directly

## Database Guidelines

**`application.yml` uses `ddl-auto: none`** — Flyway manages all schema. Hibernate does NOT create tables. **All entity changes MUST use the [flyway-hibernate-entity-validation] skill before committing.** This is a hard requirement, not optional.

See: `docs/lesson-learned/flyway-hibernate-ddl-mismatch.md`

## Test Infrastructure & Strategy

All tests must follow the standardized approach defined in:
**`docs/superpowers/templates/test-strategy.md`**

### ENFORCEMENT RULES (MANDATORY)

| # | Rule | Enforcement |
|---|------|-------------|
| 1 | **Flyway: NEVER rename applied migrations** | Use `flyway.clean-on-validation-error: true` in `application-test.yml` instead |
| 2 | **All database tests MUST extend `AbstractIntegrationTest`** | Builds fail without it |
| 3 | **Controller tests use `@SpringBootTest` + `@AutoConfigureMockMvc`** | NOT `@WebMvcTest` (causes duplicate mock errors) |
| 4 | **Use `@BeforeEach` cleanup for ALL repositories** | Even with `@Transactional`, seed data persists |
| 5 | **Delete in FK dependency order** | Child → Parent (e.g., charge_rules before charge_definitions) |
| 6 | **Run `./gradlew clean build` before merge** | Clean, not incremental |
| 7 | **Mock external adapters only** | DON'T mock internal module services or repositories |
| 8 | **TOTP tests disabled** | Timing sensitivity - codes expire every 30 seconds |

### Quick Reference

```bash
# Before ANY merge
./gradlew clean build

# Run tests with coverage
./gradlew test

# Test report location
open build/reports/tests/test/index.html
```

See the template (`docs/superpowers/templates/test-strategy.md`) for complete guidelines, checklists, and examples.

## Architecture Enforcement

To maintain clean module boundaries in our modular monolith, all implementations MUST follow these rules:

### Module Boundary Rules

**Rule 1: No domain entity sharing across modules**
- ❌ WRONG: Importing `com.banking.customer.domain.entity.Customer` from account module
- ✅ RIGHT: Importing only `com.banking.customer.api.*` or `com.banking.customer.dto.*` from account module
- Rationale: Prevents schema coupling and allows independent evolution of modules

**Rule 2: Only `api` and `dto` packages are public**
- Each module must expose services in `<module>.api` package
- Data transfer objects go in `<module>.dto` package  
- Other modules must ONLY import from `<module>.api` or `<module>.dto`
- Never import `<module>.domain.entity`, `<module>.repository`, or internal implementation packages
- Rationale: Enforces encapsulation and dependency inversion

**Rule 3: JPA relationships only within same module**
- `@ManyToOne`, `@OneToMany`, `@ElementCollection` can only reference entities from the SAME module
- Cross-module references must use `Long id` or `String identifier` + service lookup
- Rationale: Prevents tight database coupling between modules

**Rule 4: Async for side-effects, sync for validation**
- Use domain events (`@EventListener`) for:
  - Notifications, analytics, cache updates, limit assignments, fee applications
- Use direct service calls for:
  - Validation that affects immediate response (e.g., `limitCheckService.checkLimit()`)
  - Required data for business logic computations
- Rationale: Separates concerns, improves resilience, enables eventual consistency where appropriate

### Module API Contract

Each module MUST expose:

| Package | Purpose | Can be imported by other modules? |
|---------|---------|-----------------------------------|
| `<module>.api` | Service interfaces, facade interfaces | ✅ YES (only these) |
| `<module>.dto` | Data transfer objects (no JPA annotations) | ✅ YES (only these) |
| `<module>.domain.entity` | JPA entities | ❌ NO (internal only) |
| `<module>.repository` | Spring Data repositories | ❌ NO (internal only) |
| `<module>.service` | Internal service implementations | ❌ NO (internal only) |

**Example of correct dependency:**
```java
// AccountService.java (in account module)
@Service
public class AccountService {
    // ✅ CORRECT: Only depends on api interfaces and dto
    private final CustomerQueryService customerQueryService; // from customer.api
    private final LimitCheckService limitCheckService;      // from limits.api
    private final ProductQueryService productQueryService; // from product.api
    private final CurrencyRepository currencyRepository;    // ❌ WRONG! Should be from api

    // Should be:
    // private final CurrencyQueryService currencyQueryService; // from masterdata.api
}
```

## API Contract Enforcement

**OpenAPI 3.0 Specification** is the single source of truth for all REST APIs.

### Rules

- **All backend REST endpoints** MUST be documented in `docs/api/openapi.yaml`
- **Frontend API clients and TypeScript types** MUST be generated from `openapi.yaml`
- **No manual hand-written API mocks** in frontend - use generated mocks from OpenAPI spec
- **CI validation**: Run `openapi-generator-cli validate` and diff check

### Workflow

See `docs/superpowers/architecture/openapi-workflow.md` for complete workflow documentation.

**Quick Start:**

```bash
# Terminal 1: Start backend
./gradlew bootRun

# Terminal 2: Export OpenAPI spec (after server starts, ~30 seconds)
./gradlew exportOpenApiSpec

# Generate frontend types
./gradlew regenerateFrontendTypes

# Verify build
cd frontend && npm run build
```

**Or use sync command** (keeps frontend open):
```bash
# Full sync: export + generate types (requires running server for export, or spec file exists to generate types only)
./gradlew syncOpenApi
```

**Frontend-only commands:**
```bash
cd frontend
npm run openapi:validate   # Validate spec
npm run openapi:generate   # Generate types
npm run openapi:sync       # Generate + build
```

## Superpowers Skills

Invoke relevant skills before coding tasks:
- **brainstorming** - Design decisions, feature requirements
- **writing-plans** - Implementation plans
- **test-driven-development** - Write tests first
- **systematic-debugging** - Bug investigation
- **flyway-hibernate-entity-validation** - JPA entity changes, before committing
