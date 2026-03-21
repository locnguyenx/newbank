package com.banking.limits.exception;

public class LimitAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-002";

    public LimitAlreadyExistsException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
