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

## Commands

- Build & test: `./gradlew build`
- Dev server: `./gradlew bootRun` or `npm run dev`
- Lint: `npm run lint`

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

**Error handling:** Custom exceptions with error codes:
```java
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerNumber) {
        super("Customer not found: " + customerNumber);
    }
}
```

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

## Banking-Specific Guidelines

### Money Handling
- Store amounts as `BigInteger` (minor units) or `java.math.BigDecimal`
- Always store currency code with monetary values
- Use banker's rounding for calculations

### Audit Trail
- All entities include `AuditFields` embeddable
- Track: createdAt, createdBy, updatedAt, updatedBy

### Error Codes
- Use module prefix (e.g., `CUST-001` for customer errors)
- Document in `docs/error-codes.md`

## Git Workflow

- Use **git worktrees** for isolated feature work
- Worktrees are stored in `.worktrees/` directory
- Never commit to main directly

## Database Guidelines

**`application.yml` uses `ddl-auto: none`** — Flyway manages all schema. Hibernate does NOT create tables. **All entity changes MUST use the [flyway-hibernate-entity-validation] skill before committing.** This is a hard requirement, not optional.

See: `docs/lesson-learned/flyway-hibernate-ddl-mismatch.md`

## Superpowers Skills

Invoke relevant skills before coding tasks:
- **brainstorming** - Design decisions, feature requirements
- **writing-plans** - Implementation plans
- **test-driven-development** - Write tests first
- **systematic-debugging** - Bug investigation
- **flyway-hibernate-entity-validation** - JPA entity changes, before committing
