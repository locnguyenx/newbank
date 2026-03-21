package com.banking.masterdata.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-001";

    public CurrencyNotFoundException(String code) {
        super("Currency not found: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
