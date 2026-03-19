package com.banking.customer.exception;

public class AuthorizationNotFoundException extends RuntimeException {

    private static final String ERROR_CODE_PREFIX = "AUTH-";
    private final String errorCode;

    public AuthorizationNotFoundException(Long id) {
        super("Authorization not found with id: " + id);
        this.errorCode = ERROR_CODE_PREFIX + "001";
    }

    public String getErrorCode() {
        return errorCode;
    }
}