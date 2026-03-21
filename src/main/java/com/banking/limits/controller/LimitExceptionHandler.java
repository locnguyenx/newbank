package com.banking.limits.controller;

import com.banking.limits.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.banking.limits")
public class LimitExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(LimitExceptionHandler.class);

    @ExceptionHandler(LimitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLimitNotFoundException(LimitNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidLimitTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLimitTypeException(InvalidLimitTypeException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleLimitExceededException(LimitExceededException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(LimitAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLimitAlreadyExistsException(LimitAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse error = new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorResponse error = new ErrorResponse(
                "LIM-999",
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public static class ErrorResponse {
        private String errorCode;
        private String message;
        private int status;
        private Instant timestamp;

        public ErrorResponse(String errorCode, String message, int status) {
            this.errorCode = errorCode;
            this.message = message;
            this.status = status;
            this.timestamp = Instant.now();
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }

    public static class ValidationErrorResponse {
        private String errorCode;
        private String message;
        private int status;
        private Instant timestamp;
        private List<FieldError> fieldErrors;

        public ValidationErrorResponse(String errorCode, String message, int status, List<FieldError> fieldErrors) {
            this.errorCode = errorCode;
            this.message = message;
            this.status = status;
            this.timestamp = Instant.now();
            this.fieldErrors = fieldErrors;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
