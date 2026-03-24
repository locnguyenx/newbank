package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.PayrollBatchRequest;
import com.banking.cashmanagement.dto.PayrollBatchResponse;
import com.banking.cashmanagement.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cash-management/payroll")
public class PayrollController {
    
    private final PayrollService payrollService;
    
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }
    
    @PostMapping("/batches")
    public ResponseEntity<Map<String, Object>> createPayrollBatch(
            @Valid @RequestBody PayrollBatchRequest request) {
        PayrollBatchResponse response = payrollService.createPayrollBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/batches/{id}")
    public ResponseEntity<Map<String, Object>> getPayrollBatch(@PathVariable Long id) {
        PayrollBatchResponse response = payrollService.getPayrollBatch(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/batches")
    public ResponseEntity<Map<String, Object>> listPayrollBatches(
            @RequestParam(required = false) Long customerId) {
        List<PayrollBatchResponse> batches = payrollService.listPayrollBatches(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", batches));
    }
}
