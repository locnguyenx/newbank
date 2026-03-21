package com.banking.masterdata.exception;

public class CurrencyAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-002";

    public CurrencyAlreadyExistsException(String code) {
        super("Currency already exists: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
