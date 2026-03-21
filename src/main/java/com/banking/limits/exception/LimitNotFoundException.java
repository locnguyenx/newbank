package com.banking.limits.exception;

public class LimitNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-001";

    public LimitNotFoundException(Long id) {
        super("Limit definition not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
