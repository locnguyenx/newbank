package com.banking.product.exception;

public class ProductHasActiveContractsException extends RuntimeException {

    public static final String ERROR_CODE = "PROD-006";

    public ProductHasActiveContractsException(String productCode) {
        super("Product has active contracts: " + productCode);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
