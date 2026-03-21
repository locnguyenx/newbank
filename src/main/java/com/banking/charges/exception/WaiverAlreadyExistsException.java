package com.banking.charges.exception;

public class WaiverAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "CHRG-005";

    public WaiverAlreadyExistsException(String scope, String referenceId) {
        super("Waiver already exists for " + scope + " with reference: " + referenceId);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
