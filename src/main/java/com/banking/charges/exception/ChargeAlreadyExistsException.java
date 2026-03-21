package com.banking.charges.exception;

public class ChargeAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-002";

    public ChargeAlreadyExistsException(String name) {
        super("Charge already exists: " + name);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}