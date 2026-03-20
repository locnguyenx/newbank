package com.banking.account.exception;

public class DuplicateAccountException extends RuntimeException {
    public DuplicateAccountException(String accountNumber) {
        super("Duplicate account number: " + accountNumber);
    }

    public String getErrorCode() {
        return "ACCT-001";
    }
}
