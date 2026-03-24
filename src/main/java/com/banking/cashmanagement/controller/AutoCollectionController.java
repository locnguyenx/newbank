package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.AutoCollectionRuleRequest;
import com.banking.cashmanagement.dto.AutoCollectionRuleResponse;
import com.banking.cashmanagement.service.AutoCollectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> createRule(@Valid @RequestBody AutoCollectionRuleRequest request) {
        AutoCollectionRuleResponse response = autoCollectionService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/rules/{id}")
    public ResponseEntity<Map<String, Object>> getRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.getRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/rules")
    public ResponseEntity<Map<String, Object>> listRules(@RequestParam(required = false) Long customerId) {
        List<AutoCollectionRuleResponse> rules = autoCollectionService.listRules(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", rules));
    }
    
    @PostMapping("/rules/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.activateRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @PostMapping("/rules/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateRule(@PathVariable Long id) {
        AutoCollectionRuleResponse response = autoCollectionService.deactivateRule(id);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
}
