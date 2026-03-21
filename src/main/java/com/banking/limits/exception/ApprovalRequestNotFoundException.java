package com.banking.limits.exception;

public class ApprovalRequestNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "LIM-005";

    public ApprovalRequestNotFoundException(Long id) {
        super("Approval request not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
