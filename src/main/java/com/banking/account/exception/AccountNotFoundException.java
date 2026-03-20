package com.banking.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }

    public String getErrorCode() {
        return "ACCT-002";
    }
}
