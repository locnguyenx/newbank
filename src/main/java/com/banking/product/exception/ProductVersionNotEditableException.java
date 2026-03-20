package com.banking.product.exception;

public class ProductVersionNotEditableException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-005";

    public ProductVersionNotEditableException(Long productVersionId) {
        super("Product version is not editable: " + productVersionId);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
