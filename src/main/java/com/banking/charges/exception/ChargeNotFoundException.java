package com.banking.charges.exception;

public class ChargeNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-001";

    public ChargeNotFoundException(Long id) {
        super("Charge not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}