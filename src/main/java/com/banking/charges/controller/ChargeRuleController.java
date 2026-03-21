package com.banking.charges.controller;

import com.banking.charges.dto.request.CreateChargeRuleRequest;
import com.banking.charges.dto.response.ChargeRuleResponse;
import com.banking.charges.service.ChargeRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges/definitions/{chargeId}/rules")
public class ChargeRuleController {

    private final ChargeRuleService chargeRuleService;

    public ChargeRuleController(ChargeRuleService chargeRuleService) {
        this.chargeRuleService = chargeRuleService;
    }

    @PostMapping
    public ResponseEntity<ChargeRuleResponse> addRule(
            @PathVariable Long chargeId,
            @Valid @RequestBody CreateChargeRuleRequest request) {
        ChargeRuleResponse response = chargeRuleService.addRule(chargeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ChargeRuleResponse> updateRule(
            @PathVariable Long chargeId,
            @PathVariable Long ruleId,
            @Valid @RequestBody CreateChargeRuleRequest request) {
        ChargeRuleResponse response = chargeRuleService.updateRule(ruleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> removeRule(
            @PathVariable Long chargeId,
            @PathVariable Long ruleId) {
        chargeRuleService.removeRule(ruleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChargeRuleResponse>> getRules(@PathVariable Long chargeId) {
        List<ChargeRuleResponse> response = chargeRuleService.getRules(chargeId);
        return ResponseEntity.ok(response);
    }
}
