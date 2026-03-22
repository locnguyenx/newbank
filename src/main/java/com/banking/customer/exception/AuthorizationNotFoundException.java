package com.banking.customer.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class AuthorizationNotFoundException extends BaseException {
    public AuthorizationNotFoundException(Long id) {
        super(MessageCatalog.AUTH_NOT_FOUND, "Authorization not found with id: " + id);
    }
}
