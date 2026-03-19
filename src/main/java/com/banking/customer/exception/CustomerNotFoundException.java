package com.banking.customer.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String ERROR_CODE_PREFIX = "CUST-";
    private final String errorCode;

    public CustomerNotFoundException(String customerNumber) {
        super("Customer not found with customer number: " + customerNumber);
        this.errorCode = ERROR_CODE_PREFIX + "002";
    }

    public CustomerNotFoundException(Long id) {
        super("Customer not found with id: " + id);
        this.errorCode = ERROR_CODE_PREFIX + "002";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
