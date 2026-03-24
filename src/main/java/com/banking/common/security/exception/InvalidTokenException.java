package com.banking.common.security.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException(String message) {
        super(MessageCatalog.AUTH_INVALID_TOKEN, message);
    }

    public static InvalidTokenException invalid() {
        return new InvalidTokenException(MessageCatalog.getMessage(MessageCatalog.AUTH_INVALID_TOKEN));
    }
}