package com.banking.charges.controller;

import com.banking.charges.dto.request.CreateChargeDefinitionRequest;
import com.banking.charges.dto.request.UpdateChargeDefinitionRequest;
import com.banking.charges.dto.response.ChargeDefinitionResponse;
import com.banking.charges.service.ChargeDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges/definitions")
public class ChargeDefinitionController {

    private final ChargeDefinitionService service;

    public ChargeDefinitionController(ChargeDefinitionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChargeDefinitionResponse> createCharge(@Valid @RequestBody CreateChargeDefinitionRequest request) {
        ChargeDefinitionResponse response = service.createCharge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChargeDefinitionResponse>> getAllCharges(
            @RequestParam(required = false) String chargeType,
            @RequestParam(required = false) String status) {
        List<ChargeDefinitionResponse> response = service.getAllCharges(chargeType, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeDefinitionResponse> getCharge(@PathVariable Long id) {
        ChargeDefinitionResponse response = service.getCharge(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargeDefinitionResponse> updateCharge(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargeDefinitionRequest request) {
        ChargeDefinitionResponse response = service.updateCharge(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ChargeDefinitionResponse> activateCharge(@PathVariable Long id) {
        ChargeDefinitionResponse response = service.activateCharge(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ChargeDefinitionResponse> deactivateCharge(@PathVariable Long id) {
        ChargeDefinitionResponse response = service.deactivateCharge(id);
        return ResponseEntity.ok(response);
    }
}