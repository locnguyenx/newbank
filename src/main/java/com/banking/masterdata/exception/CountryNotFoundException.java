package com.banking.masterdata.exception;

public class CountryNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-003";

    public CountryNotFoundException(String isoCode) {
        super("Country not found: " + isoCode);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
