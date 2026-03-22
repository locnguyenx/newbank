package com.banking.customer.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class KYCNotFoundException extends BaseException {
    public KYCNotFoundException(Long kycCheckId) {
        super(MessageCatalog.KYC_NOT_FOUND, "KYC check not found with id: " + kycCheckId);
    }
}
