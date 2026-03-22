package com.banking.customer.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class CustomerNotFoundException extends BaseException {
    public CustomerNotFoundException(String customerNumber) {
        super(MessageCatalog.CUSTOMER_NOT_FOUND, "Customer not found with customer number: " + customerNumber);
    }

    public CustomerNotFoundException(Long id) {
        super(MessageCatalog.CUSTOMER_NOT_FOUND, "Customer not found with id: " + id);
    }
}
