package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.PayrollBatchRequest;
import com.banking.cashmanagement.dto.PayrollBatchResponse;
import com.banking.cashmanagement.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'DEPARTMENT_MAKER')")
    public ResponseEntity<Map<String, Object>> createPayrollBatch(
            @Valid @RequestBody PayrollBatchRequest request) {
        PayrollBatchResponse response = payrollService.createPayrollBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/batches/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getPayrollBatch(@PathVariable Long id) {
        PayrollBatchResponse response = payrollService.getPayrollBatch(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/batches")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> listPayrollBatches(
            @RequestParam(required = false) Long customerId) {
        List<PayrollBatchResponse> batches = payrollService.listPayrollBatches(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", batches));
    }
}
