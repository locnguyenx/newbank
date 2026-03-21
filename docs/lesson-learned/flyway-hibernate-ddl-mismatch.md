# Lesson Learned: Flyway + Hibernate ddl-auto Mismatch

**Date:** 2026-03-21
**Module:** Product Configuration (and retroactively: customer, account)
**Severity:** Runtime failure (hidden by test environment)

## Problem

When switching `ddl-auto: create-drop` → `ddl-auto: none` to preserve Flyway-seeded data on startup, the application failed at runtime with table/column not found errors. Tests had passed, but `bootRun` failed.

## Root Cause

With `ddl-auto: create-drop`, Hibernate **recreates the database from entity metadata alone** on every test run. Flyway migrations are bypassed entirely — so any inconsistency between entity annotations and Flyway SQL is invisible.

With `ddl-auto: none`, Flyway runs first and creates tables with its own naming conventions. Hibernate then maps entities to those tables. When entity names don't match Flyway names, Hibernate can't find the tables/columns.

## What Went Wrong

### 1. Subclass entity table names
Flyway V1 created `corporate_customers` (plural), but subclass entities used default naming:
```java
@Entity
public class CorporateCustomer extends Customer { } // maps to "corporate_customer"
```

### 2. Discriminator column conflict
```java
@DiscriminatorColumn(name = "account_type", ...)
public abstract class Account { ... }
private AccountType type; // mapped to "type" by default, conflicted with discriminator
```

### 3. Missing Flyway migrations
- `account_holders` table was Hibernate-managed, not in Flyway
- `product_customer_segments` entity had `AuditFields` but migration lacked audit columns

## Symptoms

```
Column "TYPE" not found                    // Account.type vs account_type
Table "ACCOUNT_HOLDERS" not found         // entity exists, Flyway table missing
Column "CS1_0.CREATED_AT" not found        // ProductCustomerSegment audit columns
Table "corporate_customer" not found      // entity default vs Flyway plural
```

## Solution

All entity annotations must match Flyway SQL exactly:

```java
@Entity
@Table(name = "corporate_customers")       // match Flyway plural
@DiscriminatorValue("CORPORATE")
public class CorporateCustomer extends Customer { }

@Enumerated(EnumType.STRING)
@Column(name = "account_type", insertable = false, updatable = false) // match discriminator
private AccountType type;
```

New Flyway migrations for missing tables/columns.

## Lesson

**Every module should verify `ddl-auto: none` + Flyway works, not just tests.**

Never assume entity annotations and Flyway migrations are in sync just because tests pass. The only way to verify is to run the application with `ddl-auto: none` and hit the code paths that persist entities managed by Flyway.

## Prevention

1. **Always use `@Table`, `@Column` explicitly** on entities — don't rely on default naming
2. **When a field shadows a `@DiscriminatorColumn`**, mark it `insertable=false, updatable=false`
3. **Keep all schema definitions in Flyway** — no Hibernate-only tables if `ddl-auto: none`
4. **After any entity change, run `bootRun`** with `ddl-auto: none` before committing
5. **AuditFields on entity = audit columns in Flyway migration** — maintain them together

## Checklist for New Entities

- [ ] `@Table(name = "...")` matches Flyway `CREATE TABLE` name exactly
- [ ] `@Column(name = "...")` matches Flyway column names (especially for enums/fields that shadow discriminators)
- [ ] If entity has `@Embedded AuditFields`, Flyway migration must have `created_at, created_by, updated_at, updated_by`
- [ ] No Hibernate-only tables — all tables must be in Flyway if `ddl-auto: none`
- [ ] Run `bootRun` with `ddl-auto: none` and trigger a persist before considering work done
