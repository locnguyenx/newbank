package com.banking.cashmanagement.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class BatchPaymentNotFoundException extends BaseException {
    
    public BatchPaymentNotFoundException(Long id) {
        super(MessageCatalog.CAS_BATCH_PAYMENT_NOT_FOUND, "Batch payment not found with id: " + id);
    }
    
    public static BatchPaymentNotFoundException forId(Long id) {
        return new BatchPaymentNotFoundException(id);
    }
}
