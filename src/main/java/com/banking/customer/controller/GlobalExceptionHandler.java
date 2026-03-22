package com.banking.customer.controller;

import com.banking.common.exception.BaseException;
import com.banking.customer.exception.AuthorizationNotFoundException;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.exception.InvalidKYCStateException;
import com.banking.customer.exception.KYCNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "com.banking.customer")
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessageCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(KYCNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKYCNotFoundException(KYCNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessageCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AuthorizationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationNotFoundException(AuthorizationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessageCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCustomerException(DuplicateCustomerException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessageCode(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidKYCStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidKYCStateException(InvalidKYCStateException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessageCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse error = new ValidationErrorResponse(
                "VALIDATION_001",
                "Please check your input and try again",
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_001",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    public static class ErrorResponse {
        private String messageCode;
        private String message;
        private int status;
        private Instant timestamp;

        public ErrorResponse(String messageCode, String message, int status) {
            this.messageCode = messageCode;
            this.message = message;
            this.status = status;
            this.timestamp = Instant.now();
        }

        public String getMessageCode() {
            return messageCode;
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
        private String messageCode;
        private String message;
        private int status;
        private Instant timestamp;
        private List<FieldError> fieldErrors;

        public ValidationErrorResponse(String messageCode, String message, int status, List<FieldError> fieldErrors) {
            this.messageCode = messageCode;
            this.message = message;
            this.status = status;
            this.timestamp = Instant.now();
            this.fieldErrors = fieldErrors;
        }

        public String getMessageCode() {
            return messageCode;
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
