package com.banking.masterdata.exception;

public class IndustryNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-004";

    public IndustryNotFoundException(String code) {
        super("Industry not found: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
