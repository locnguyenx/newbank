package com.banking.customer.exception;

public class InvalidKYCStateException extends RuntimeException {

    private static final String ERROR_CODE_PREFIX = "KYC-";
    private final String errorCode;

    public InvalidKYCStateException(String message) {
        super(message);
        this.errorCode = ERROR_CODE_PREFIX + "002";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
