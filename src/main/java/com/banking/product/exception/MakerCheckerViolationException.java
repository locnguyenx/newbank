package com.banking.product.exception;

public class MakerCheckerViolationException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-007";

    public MakerCheckerViolationException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
