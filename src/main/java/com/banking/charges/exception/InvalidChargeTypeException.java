package com.banking.charges.exception;

public class InvalidChargeTypeException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-003";

    public InvalidChargeTypeException(String type) {
        super("Invalid charge type: " + type);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}