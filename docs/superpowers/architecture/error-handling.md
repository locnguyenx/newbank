# Error Handling System Design

## Overview

All API errors MUST return structured responses with message codes for consistent, user-friendly error messages across frontend and backend.

## Architecture

### 1. Message Catalog (`com.banking.common.message.MessageCatalog`)

Central registry of all error codes and their default messages.

```java
public final class MessageCatalog {
    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_001";
    public static final String ACCOUNT_ALREADY_CLOSED = "ACCOUNT_002";
    // ... more codes
}
```

### 2. Base Exception (`com.banking.common.exception.BaseException`)

All exceptions MUST extend `BaseException` which enforces message codes.

```java
public class BaseException extends RuntimeException {
    private final String messageCode;
    
    public String getMessageCode() {
        return messageCode;
    }
}
```

### 3. API Response Format

All exception handlers return structured responses:

```json
{
  "messageCode": "ACCOUNT_004",
  "message": "Cannot close account with non-zero balance. Please withdraw all funds first.",
  "status": 400
}
```

### 4. Frontend Message Service (`services/messageService.ts`)

Maps message codes to user-friendly messages with fallbacks.

## Message Code Naming Convention

Format: `{MODULE}_{SEVERITY}{NUMBER}`

| Module | Prefix |
|--------|--------|
| Account | ACCOUNT_ |
| Customer | CUSTOMER_ |
| Product | PRODUCT_ |
| Limit | LIMIT_ |
| Charge | CHARGE_ |
| KYC | KYC_ |
| Validation | VALIDATION_ |
| System | SYSTEM_ |

Severity:
- 0xx: Not Found
- 1xx: Already Exists / Invalid State
- 2xx: Validation / Business Rule
- 3xx: Authorization / Security
- 9xx: System / Internal

## Example Implementation

### Backend Exception Class

```java
public class InvalidAccountStateException extends BaseException {
    public static InvalidAccountStateException alreadyClosed() {
        return new InvalidAccountStateException(
            MessageCatalog.ACCOUNT_ALREADY_CLOSED, 
            MessageCatalog.getMessage(MessageCatalog.ACCOUNT_ALREADY_CLOSED)
        );
    }
}
```

### Frontend Usage

```typescript
import { getErrorMessage } from '@/services/messageService';

try {
  await dispatch(closeAccount(accountNumber)).unwrap();
  message.success('Account closed');
} catch (err) {
  message.error(getErrorMessage(err));
}
```

## Enforcement Mechanisms

1. **Code Review Checklist**: See `docs/guidelines/error-handling-checklist.md`
2. **Unit Tests**: `ErrorCodeTest.java` verifies all exceptions have codes
3. **Integration Tests**: API responses validated against message catalog
4. **Linting**: Custom ESLint rule for frontend error handling
