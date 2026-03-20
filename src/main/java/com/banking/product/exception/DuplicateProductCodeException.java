package com.banking.product.exception;

public class DuplicateProductCodeException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-003";

    public DuplicateProductCodeException(String productCode) {
        super("Duplicate product code: " + productCode);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
