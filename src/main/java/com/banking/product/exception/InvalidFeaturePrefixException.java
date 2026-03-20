package com.banking.product.exception;

public class InvalidFeaturePrefixException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-008";

    public InvalidFeaturePrefixException(String prefix) {
        super("Invalid feature prefix: " + prefix);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
