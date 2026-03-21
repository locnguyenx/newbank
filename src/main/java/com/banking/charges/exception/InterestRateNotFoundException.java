package com.banking.charges.exception;

public class InterestRateNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-006";

    public InterestRateNotFoundException(Long id) {
        super("Interest rate not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
