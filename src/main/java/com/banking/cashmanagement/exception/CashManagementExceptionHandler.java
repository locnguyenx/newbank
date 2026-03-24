package com.banking.cashmanagement.exception;

import com.banking.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.banking.cashmanagement.controller")
public class CashManagementExceptionHandler {
    
    @ExceptionHandler(PayrollBatchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePayrollBatchNotFound(
            PayrollBatchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "success", false,
                "errorCode", ex.getMessageCode(),
                "message", ex.getMessage()
            ));
    }
    
    @ExceptionHandler(BatchPaymentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBatchPaymentNotFound(
            BatchPaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "success", false,
                "errorCode", ex.getMessageCode(),
                "message", ex.getMessage()
            ));
    }
    
    @ExceptionHandler(ReceivableInvoiceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInvoiceNotFound(
            ReceivableInvoiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "success", false,
                "errorCode", ex.getMessageCode(),
                "message", ex.getMessage()
            ));
    }
    
    @ExceptionHandler(AutoCollectionRuleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRuleNotFound(
            AutoCollectionRuleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "success", false,
                "errorCode", ex.getMessageCode(),
                "message", ex.getMessage()
            ));
    }
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "success", false,
                "errorCode", ex.getMessageCode(),
                "message", ex.getMessage()
            ));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "success", false,
                "errorCode", "CAS-999",
                "message", "An unexpected error occurred"
            ));
    }
}
