package com.banking.limits.exception;

public class InvalidApprovalActionException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-006";

    public InvalidApprovalActionException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
