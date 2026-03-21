package com.banking.charges.controller;

import com.banking.charges.dto.request.CreateFeeWaiverRequest;
import com.banking.charges.dto.response.FeeWaiverResponse;
import com.banking.charges.service.FeeWaiverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/charges/waivers")
public class FeeWaiverController {

    private final FeeWaiverService feeWaiverService;

    public FeeWaiverController(FeeWaiverService feeWaiverService) {
        this.feeWaiverService = feeWaiverService;
    }

    @PostMapping
    public ResponseEntity<FeeWaiverResponse> createWaiver(@Valid @RequestBody CreateFeeWaiverRequest request) {
        FeeWaiverResponse response = feeWaiverService.createWaiver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWaiver(@PathVariable Long id) {
        feeWaiverService.removeWaiver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FeeWaiverResponse>> getWaivers(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) String referenceId) {
        List<FeeWaiverResponse> response = feeWaiverService.getWaivers(scope, referenceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applicable")
    public ResponseEntity<List<FeeWaiverResponse>> getApplicableWaivers(
            @RequestParam Long chargeId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String productCode,
            @RequestParam LocalDate date) {
        List<FeeWaiverResponse> response = feeWaiverService.getApplicableWaivers(
                chargeId, customerId, accountNumber, productCode, date);
        return ResponseEntity.ok(response);
    }
}
