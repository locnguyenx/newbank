package com.banking.masterdata.exception;

public class ChannelAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-010";

    public ChannelAlreadyExistsException(String code) {
        super("Channel already exists: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
