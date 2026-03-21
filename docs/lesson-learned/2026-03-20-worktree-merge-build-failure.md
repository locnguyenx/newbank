# Lesson Learned: Worktree Merge Build Failures

**Date:** 2026-03-20
**Module:** Account Management
**Issue:** Build passed in worktree but failed after merge to main

## Root Causes

### 1. Stale Gradle Cache
Gradle's incremental compilation reuses cached `.class` files. When code changed but wasn't fully recompiled, old artifacts were used.

**Symptom:** Build appears to work in worktree, fails after merge on fresh state.

### 2. Duplicate Exception Handler
Two `AccountExceptionHandler` classes existed:
- `com.banking.account.controller.AccountExceptionHandler`
- `com.banking.account.exception.AccountExceptionHandler`

Spring auto-registered both with the same bean name, causing `ConflictingBeanDefinitionException`.

### 3. Gradle Configuration Syntax Error
```kotlin
// WRONG
bootJar { mainClass.set = "com.banking.account.AccountModuleApplication" }

// CORRECT
springBoot { mainClass.set("com.banking.account.AccountModuleApplication") }
```

### 4. Test Mock Mismatches
- **Void methods:** Used `when().thenThrow()` instead of `doThrow().when()`
- **Error codes:** Tests expected `ACC-XXX` but actual exceptions used `ACCT-XXX`
- **Status codes:** Tests expected `201` for POST but controller returned `200`
- **DTO types:** Mocked `AccountStatementResponse` instead of `AccountStatement`

## Prevention Checklist

### Before Merging Worktree
- [ ] Run `./gradlew clean build` (not just `./gradlew build`)
- [ ] Verify all tests pass with clean state
- [ ] Check for duplicate classes with same bean name
- [ ] Review test expectations match controller responses

### When Writing Tests
- [ ] Use `doThrow()/doNothing()` for void method mocks
- [ ] Verify error codes match exception definitions
- [ ] Check response status codes match controller
- [ ] Ensure mocked DTO types match service return types

### General
- [ ] Delete duplicate classes before merge
- [ ] Run `./gradlew clean` before `./gradlew build` when in doubt
- [ ] Check Gradle configuration syntax matches Kotlin DSL requirements

## Related Files Fixed
- `build.gradle.kts` - Gradle syntax
- `src/main/java/com/banking/account/exception/AccountExceptionHandler.java` - Deleted (duplicate)
- `src/test/java/com/banking/account/controller/AccountControllerTest.java` - Test fixes
- `src/test/java/com/banking/account/controller/AccountHolderControllerTest.java` - Test fixes
