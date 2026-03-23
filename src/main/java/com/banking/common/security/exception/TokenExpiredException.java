package com.banking.common.security.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class TokenExpiredException extends BaseException {

    public TokenExpiredException(String message) {
        super(MessageCatalog.AUTH_TOKEN_EXPIRED, message);
    }

    public static TokenExpiredException expired() {
        return new TokenExpiredException(MessageCatalog.getMessage(MessageCatalog.AUTH_TOKEN_EXPIRED));
    }
}