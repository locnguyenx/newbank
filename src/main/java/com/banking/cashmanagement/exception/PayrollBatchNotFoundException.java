package com.banking.cashmanagement.exception;

public class PayrollBatchNotFoundException extends RuntimeException {
    
    private static final String ERROR_CODE = "CAS-001";
    
    public PayrollBatchNotFoundException(Long id) {
        super("Payroll batch not found with id: " + id);
    }
    
    public PayrollBatchNotFoundException(String batchReference) {
        super("Payroll batch not found with reference: " + batchReference);
    }
    
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
