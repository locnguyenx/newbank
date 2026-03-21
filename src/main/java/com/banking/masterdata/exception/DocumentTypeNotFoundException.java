package com.banking.masterdata.exception;

public class DocumentTypeNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-011";

    public DocumentTypeNotFoundException(String code) {
        super("Document type not found: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
