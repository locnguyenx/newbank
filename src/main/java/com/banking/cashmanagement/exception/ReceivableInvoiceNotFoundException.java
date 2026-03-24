package com.banking.cashmanagement.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class ReceivableInvoiceNotFoundException extends BaseException {
    
    public ReceivableInvoiceNotFoundException(Long id) {
        super(MessageCatalog.CAS_INVOICE_NOT_FOUND, "Invoice not found with id: " + id);
    }
    
    public static ReceivableInvoiceNotFoundException forId(Long id) {
        return new ReceivableInvoiceNotFoundException(id);
    }
}
