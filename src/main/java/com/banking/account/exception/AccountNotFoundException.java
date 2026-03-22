package com.banking.account.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String accountNumber) {
        super(MessageCatalog.ACCOUNT_NOT_FOUND, "Account not found: " + accountNumber);
    }
}
