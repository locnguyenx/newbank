package com.banking.masterdata.exception;

public class ExchangeRateNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-005";

    public ExchangeRateNotFoundException(String base, String target) {
        super("Exchange rate not found: " + base + "/" + target);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
