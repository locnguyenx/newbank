package com.banking.customer.exception;

public class KYCNotFoundException extends RuntimeException {

    private static final String ERROR_CODE_PREFIX = "KYC-";
    private final String errorCode;

    public KYCNotFoundException(Long kycCheckId) {
        super("KYC check not found with id: " + kycCheckId);
        this.errorCode = ERROR_CODE_PREFIX + "001";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
