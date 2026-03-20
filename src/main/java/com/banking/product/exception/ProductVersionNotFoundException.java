package com.banking.product.exception;

public class ProductVersionNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-002";

    public ProductVersionNotFoundException(Long productVersionId) {
        super("Product version not found: " + productVersionId);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
