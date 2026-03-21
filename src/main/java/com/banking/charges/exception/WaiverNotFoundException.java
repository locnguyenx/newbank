package com.banking.charges.exception;

public class WaiverNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-004";

    public WaiverNotFoundException(Long id) {
        super("Waiver not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
