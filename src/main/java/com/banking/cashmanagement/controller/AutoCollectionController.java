package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.AutoCollectionRuleRequest;
import com.banking.cashmanagement.dto.AutoCollectionRuleResponse;
import com.banking.cashmanagement.service.AutoCollectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cash-management/auto-collection")
public class AutoCollectionController {
    
    private final AutoCollectionService autoCollectionService;
    
    public AutoCollectionController(AutoCollectionService autoCollectionService) {
        this.autoCollectionService = autoCollectionService;
    }
    
    @PostMapping("/rules")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'DEPARTMENT_MAKER')")
    public ResponseEntity<Map<String, Object>> createRule(@Valid @RequestBody AutoCollectionRuleRequest request) {
        AutoCollectionRuleResponse response = autoCollectionService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/rules/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.getRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/rules")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> listRules(@RequestParam(required = false) Long customerId) {
        List<AutoCollectionRuleResponse> rules = autoCollectionService.listRules(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", rules));
    }
    
    @PostMapping("/rules/{id}/activate")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER')")
    public ResponseEntity<Map<String, Object>> activateRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.activateRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @PostMapping("/rules/{id}/deactivate")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER')")
    public ResponseEntity<Map<String, Object>> deactivateRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.deactivateRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
}
