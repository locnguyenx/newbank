package com.banking.customer.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class DuplicateCustomerException extends BaseException {
    public DuplicateCustomerException(String field, String value) {
        super(MessageCatalog.CUSTOMER_ALREADY_EXISTS, "Customer already exists with " + field + ": " + value);
    }
}
