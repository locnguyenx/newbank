package com.banking.product.exception;

public class InvalidProductStatusException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-004";

    public InvalidProductStatusException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
