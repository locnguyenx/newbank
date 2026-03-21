package com.banking.masterdata.exception;

public class BranchAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-008";

    public BranchAlreadyExistsException(String code) {
        super("Branch already exists: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
