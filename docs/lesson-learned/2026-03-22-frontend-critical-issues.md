# Frontend Critical Issues - 2026-03-22

## Summary

During debugging of frontend issues with OpenAPI integration, several critical problems were identified. This document captures all issues for future reference.

## Critical Issues

### 1. Demo Data Fallbacks Violate Business Logic

**Problem:** Redux slices used demo/fake data as fallback when API failed or returned empty. This is fundamentally wrong for banking applications.

**Impact:** Users see fake account balances, customer data, or products instead of real backend data or proper error messages.

**Root Cause:** Development convenience was prioritized over business logic correctness.

**Fix:** Remove all demo data fallbacks from Redux slices. Backend is the only source of truth.

```typescript
// ❌ WRONG: Fallback to demo data
try {
  return await accountService.getAll(params);
} catch {
  return { content: [...demoAccounts], ... };
}

// ✅ CORRECT: Propagate error
return await accountService.getAll(params);
```

**Lesson:** Never cache or fake business entity data on frontend. Show real data or error. Development convenience must not compromise production correctness.

---

### 2. Tests Pass But App Has Syntax Errors

**Problem:** ProductFormPage had duplicate code (syntax error), preventing compilation, but all 98 tests passed.

**Impact:** Critical runtime error undetected by test suite.

**Root Cause:** Tests use mocked implementations that don't import/execute real component code.

**Fix:** Add integration tests that actually mount components, or use smoke tests that verify basic compilation.

**Lesson:** Unit tests with mocks don't catch syntax/type errors in actual component code. Need build verification (tsc --noEmit) in CI pipeline.

---

### 3. Generated API Client Parameter Mismatch

**Problem:** `createProduct(data)` passed data as first parameter (xUsername) instead of second (createProductRequest), causing "Required parameter null" error.

**Impact:** Product creation always fails with confusing error message.

**Root Cause:** Generated OpenAPI client has parameter order: `(xUsername, createProductRequest)`. Our code called `createProduct(data)` which passes data as xUsername.

**Fix:** Pass correct parameters matching generated API signature.

```typescript
// ❌ WRONG: data passed as xUsername
productAPI.createProduct(data)

// ✅ CORRECT: xUsername first, then data
productAPI.createProduct(this.getXUsername(), data)
```

**Lesson:** Generated API clients have specific parameter orders. Always check generated method signatures, not just assume parameter order.

---

### 4. Field Name Mismatches Between API and UI

**Problem:** CustomerListPage used `customer.customerType` and `customer.companyName`, but API returns `type` and `name`.

**Impact:** Customer names show as empty in list view.

**Root Cause:** Frontend code assumed old field names from earlier API version, not matching current generated types.

**Fix:** Use correct field names from generated API types.

**Lesson:** After regenerating OpenAPI client, verify all field references match new type definitions.

---

### 5. Missing `unwrap()` on Redux Dispatch

**Problem:** `dispatch(closeAccount(accountNumber))` without `.unwrap()` meant rejection errors weren't caught by try/catch.

**Impact:** Close Account fails silently - no error message shown to user.

**Root Cause:** Redux toolkit's `dispatch()` returns a promise that doesn't throw on rejection. `.unwrap()` is needed to propagate rejection.

**Fix:** Add `.unwrap()` to all dispatch calls that need error handling.

```typescript
// ❌ WRONG: rejection not caught
await dispatch(closeAccount(accountNumber));

// ✅ CORRECT: rejection propagates to catch
await dispatch(closeAccount(accountNumber)).unwrap();
```

---

### 6. `erasableSyntaxOnly` Blocks Generated API Code

**Problem:** Generated `base.ts` uses `protected` constructor parameters, which TypeScript's `erasableSyntaxOnly` flag disallows.

**Impact:** Build fails with cryptic syntax errors.

**Root Cause:** Generated OpenAPI client code uses syntax not compatible with strict TypeScript settings.

**Fix:** Disable `erasableSyntaxOnly` in tsconfig.app.json or regenerate with compatible template.

**Lesson:** Generated code may not match strict TypeScript configs. Verify compatibility after generation.

---

### 7. Missing Type Definitions After API Regeneration

**Problem:** After regenerating OpenAPI client, several types (AccountDetails, CustomerVariant, etc.) were no longer exported.

**Impact:** Build fails with "has no exported member" errors.

**Root Cause:** Custom frontend types that extended generated types were broken when API schema changed.

**Fix:** Define adapter types in local type files that extend generated types.

**Lesson:** Maintain local type files that bridge differences between generated API types and frontend expectations.

---

## Prevention Checklist

Before claiming "frontend works":

1. [ ] Run `npx tsc --noEmit` - must show 0 errors
2. [ ] Run `npm run test` - all tests pass
3. [ ] Run `npm run build` - build succeeds
4. [ ] Start backend AND frontend together
5. [ ] Verify API calls succeed through network tab
6. [ ] Verify no demo/fake data in production code paths
7. [ ] Verify error messages display to user (not just console)
8. [ ] Verify all field names match generated API types

## Coding Discipline: Always Verify After Edit

**Critical rule:** After ANY code edit, run `npx tsc --noEmit` before proceeding.

I introduced a duplicate `})` in accountSlice.ts that broke compilation. This is caught immediately by:
```bash
npx tsc --noEmit
```

**Procedure after every edit:**
1. Save file
2. Run `npx tsc --noEmit`
3. If errors → fix immediately, don't continue
4. Only then proceed to next task

This is basic coding discipline, not optional.
