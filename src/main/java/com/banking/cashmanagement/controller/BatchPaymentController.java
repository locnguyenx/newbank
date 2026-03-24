package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.BatchPaymentRequest;
import com.banking.cashmanagement.dto.BatchPaymentResponse;
import com.banking.cashmanagement.service.BatchPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cash-management/batch-payments")
public class BatchPaymentController {
    
    private final BatchPaymentService batchPaymentService;
    
    public BatchPaymentController(BatchPaymentService batchPaymentService) {
        this.batchPaymentService = batchPaymentService;
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'DEPARTMENT_MAKER')")
    public ResponseEntity<Map<String, Object>> createBatchPayment(@Valid @RequestBody BatchPaymentRequest request) {
        BatchPaymentResponse response = batchPaymentService.createBatchPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getBatchPayment(@PathVariable Long id) {
        BatchPaymentResponse response = batchPaymentService.getBatchPayment(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> listBatchPayments(@RequestParam(required = false) Long customerId) {
        List<BatchPaymentResponse> batches = batchPaymentService.listBatchPayments(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", batches));
    }
}
