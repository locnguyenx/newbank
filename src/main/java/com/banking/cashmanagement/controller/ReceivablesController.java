package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.ReceivableInvoiceRequest;
import com.banking.cashmanagement.dto.ReceivableInvoiceResponse;
import com.banking.cashmanagement.service.ReceivablesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cash-management/receivables")
public class ReceivablesController {
    
    private final ReceivablesService receivablesService;
    
    public ReceivablesController(ReceivablesService receivablesService) {
        this.receivablesService = receivablesService;
    }
    
    @PostMapping("/invoices")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'DEPARTMENT_MAKER')")
    public ResponseEntity<Map<String, Object>> createInvoice(@Valid @RequestBody ReceivableInvoiceRequest request) {
        ReceivableInvoiceResponse response = receivablesService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/invoices/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getInvoice(@PathVariable Long id) {
        ReceivableInvoiceResponse response = receivablesService.getInvoice(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/invoices")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> listInvoices(@RequestParam(required = false) Long customerId) {
        List<ReceivableInvoiceResponse> invoices = receivablesService.listInvoices(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", invoices));
    }
    
    @PostMapping("/invoices/{id}/issue")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'DEPARTMENT_MAKER')")
    public ResponseEntity<Map<String, Object>> issueInvoice(@PathVariable Long id) {
        ReceivableInvoiceResponse response = receivablesService.issueInvoice(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
}
