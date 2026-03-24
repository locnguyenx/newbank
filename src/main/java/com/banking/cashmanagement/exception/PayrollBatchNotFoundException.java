package com.banking.cashmanagement.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class PayrollBatchNotFoundException extends BaseException {
    
    public PayrollBatchNotFoundException(Long id) {
        super(MessageCatalog.CAS_PAYROLL_BATCH_NOT_FOUND, "Payroll batch not found with id: " + id);
    }
    
    public PayrollBatchNotFoundException(String batchReference) {
        super(MessageCatalog.CAS_PAYROLL_BATCH_NOT_FOUND, "Payroll batch not found with reference: " + batchReference);
    }
    
    public static PayrollBatchNotFoundException forId(Long id) {
        return new PayrollBatchNotFoundException(id);
    }
}
