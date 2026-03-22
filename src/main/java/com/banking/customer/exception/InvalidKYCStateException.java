package com.banking.customer.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class InvalidKYCStateException extends BaseException {
    public InvalidKYCStateException(String message) {
        super(MessageCatalog.KYC_INVALID_STATE, message);
    }
}
