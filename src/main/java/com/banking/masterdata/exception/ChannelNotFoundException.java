package com.banking.masterdata.exception;

public class ChannelNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-009";

    public ChannelNotFoundException(String code) {
        super("Channel not found: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
