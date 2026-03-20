package com.banking.product.exception;

public class ProductNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-001";

    public ProductNotFoundException(String productCode) {
        super("Product not found: " + productCode);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
