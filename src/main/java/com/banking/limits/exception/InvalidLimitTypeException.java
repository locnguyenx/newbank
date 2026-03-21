package com.banking.limits.exception;

public class InvalidLimitTypeException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-003";

    public InvalidLimitTypeException(String type) {
        super("Invalid limit type: " + type);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
