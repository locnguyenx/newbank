package com.banking.account.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class DuplicateAccountException extends BaseException {
    public DuplicateAccountException(String accountNumber) {
        super(MessageCatalog.ACCOUNT_ALREADY_EXISTS, "Account already exists: " + accountNumber);
    }
}
