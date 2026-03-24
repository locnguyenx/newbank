# Lesson Learned: Test Infrastructure Fix

**Date:** 2026-03-24  
**Category:** Testing / Infrastructure  
**Status:** Resolved

---

## Problem

102 tests failing after Flyway migration file renames.

## Root Cause

1. Migration files had duplicate version numbers (V23 and V24 each existed twice)
2. One file from each pair was renamed to V23.1 and V24.1
3. This broke Flyway's schema history - DB still had old V23/V24, but files no longer existed
4. Result: `FlywayValidateException: Migration checksum mismatch`

## Solution

### 1. Flyway Configuration
Added to `application-test.yml`:
```yaml
flyway:
  clean-on-validation-error: true
```

This automatically cleans and re-migrates when validation fails.

### 2. NEVER Rename Applied Migrations
Once a migration is applied to any database, it must never be renamed.

**Correct approach:**
- Keep both V23 files and delete one
- Or create new migration V25 with additional changes

### 3. Mock Conflicts
Multiple `@MockBean` for same type caused duplicate errors.

**Fix:** Don't mock internal module services - only mock external adapters.

### 4. Seed Data
Tests failed with wrong values because seeded data from migrations persisted.

**Fix:** Add explicit `@BeforeEach` cleanup - `@Transactional` doesn't clear seed data.

## Files Modified

| File | Change |
|------|--------|
| `application-test.yml` | Added `flyway.clean-on-validation-error: true` |
| `AbstractIntegrationTest.java` | Simplified to basic container setup |
| `ChargesTestApplication.java` | Removed duplicate MfaService mock |
| `PayrollServiceIntegrationTest.java` | Added extends AbstractIntegrationTest |
| `ChargeCalculationControllerTest.java` | Added @BeforeEach cleanup |

## Prevention

1. **Never rename applied migrations** - use new version instead
2. **Use `flyway.clean-on-validation-error: true`** in test config
3. **Don't mock internal services** - only external adapters
4. **Always add `@BeforeEach` cleanup** - even with `@Transactional`

## Related

- `docs/superpowers/templates/test-strategy.md`
- `docs/superpowers/reports/test-infra-fix-report.md`
