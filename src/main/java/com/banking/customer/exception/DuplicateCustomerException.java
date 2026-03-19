package com.banking.customer.exception;

public class DuplicateCustomerException extends RuntimeException {

    private static final String ERROR_CODE_PREFIX = "CUST-";
    private final String errorCode;

    public DuplicateCustomerException(String field, String value) {
        super("Customer already exists with " + field + ": " + value);
        this.errorCode = ERROR_CODE_PREFIX + "001";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
