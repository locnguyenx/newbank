# Error Handling Code Review Checklist

## New Exception Classes

- [ ] Extends `BaseException` (not `RuntimeException` directly)
- [ ] Has message code constant in `MessageCatalog.java`
- [ ] Has human-readable default message in `MessageCatalog.java`
- [ ] Uses static factory methods for common scenarios
- [ ] Exception is caught by appropriate `@ExceptionHandler`

## Exception Handler Updates

- [ ] New exception class is imported
- [ ] `@ExceptionHandler` method exists for the exception
- [ ] Returns structured `ErrorResponse` with `messageCode`
- [ ] Uses correct HTTP status code:
  - 400: Bad Request / Business Rule Violation
  - 401: Unauthorized
  - 403: Forbidden
  - 404: Not Found
  - 409: Conflict / Already Exists
  - 500: Internal Server Error

## API Responses

- [ ] Response contains `messageCode` field
- [ ] Response contains human-readable `message` field
- [ ] Response contains `status` field (HTTP code)

## Frontend Updates

- [ ] Error message code is added to `messageService.ts`
- [ ] User-facing message is clear and actionable
- [ ] Error handling uses `getErrorMessage()` utility

## Test Coverage

- [ ] Unit test verifies exception has correct message code
- [ ] Unit test verifies exception message
- [ ] Integration test verifies API response format
