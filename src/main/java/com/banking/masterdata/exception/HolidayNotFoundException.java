package com.banking.masterdata.exception;

public class HolidayNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-006";

    public HolidayNotFoundException(String id) {
        super("Holiday not found: " + id);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
