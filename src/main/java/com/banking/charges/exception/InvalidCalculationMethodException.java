package com.banking.charges.exception;

public class InvalidCalculationMethodException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-007";

    public InvalidCalculationMethodException(String method) {
        super("Invalid calculation method: " + method);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}