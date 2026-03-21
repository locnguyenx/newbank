package com.banking.masterdata.exception;

public class BranchNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-007";

    public BranchNotFoundException(String code) {
        super("Branch not found: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
