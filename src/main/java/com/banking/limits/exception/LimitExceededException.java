package com.banking.limits.exception;

public class LimitExceededException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-004";

    public LimitExceededException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
