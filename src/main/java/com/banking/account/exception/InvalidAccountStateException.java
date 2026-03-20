package com.banking.account.exception;

public class InvalidAccountStateException extends RuntimeException {
    public InvalidAccountStateException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return "ACCT-003";
    }
}
