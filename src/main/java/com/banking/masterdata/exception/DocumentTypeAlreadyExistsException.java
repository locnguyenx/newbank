package com.banking.masterdata.exception;

public class DocumentTypeAlreadyExistsException extends RuntimeException {

    public static final String ERROR_CODE = "MDATA-012";

    public DocumentTypeAlreadyExistsException(String code) {
        super("Document type already exists: " + code);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
