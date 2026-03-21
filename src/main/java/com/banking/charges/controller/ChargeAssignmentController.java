package com.banking.charges.controller;

import com.banking.charges.dto.request.AssignChargeRequest;
import com.banking.charges.dto.response.CustomerChargeOverrideResponse;
import com.banking.charges.dto.response.ProductChargeResponse;
import com.banking.charges.service.ChargeAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges/assignments")
public class ChargeAssignmentController {

    private final ChargeAssignmentService service;

    public ChargeAssignmentController(ChargeAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductChargeResponse> assignToProduct(@Valid @RequestBody AssignChargeRequest request) {
        ProductChargeResponse response = service.assignToProduct(
                request.getChargeId(),
                request.getReferenceId(),
                request.getOverrideAmount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerChargeOverrideResponse> assignToCustomer(@Valid @RequestBody AssignChargeRequest request) {
        CustomerChargeOverrideResponse response = service.assignToCustomer(
                request.getChargeId(),
                Long.parseLong(request.getReferenceId()),
                request.getOverrideAmount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/product")
    public ResponseEntity<Void> unassignFromProduct(@RequestParam Long chargeId, @RequestParam String productCode) {
        service.unassignFromProduct(chargeId, productCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/customer")
    public ResponseEntity<Void> unassignFromCustomer(@RequestParam Long chargeId, @RequestParam Long customerId) {
        service.unassignFromCustomer(chargeId, customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<List<ProductChargeResponse>> getProductCharges(@PathVariable String productCode) {
        List<ProductChargeResponse> response = service.getProductCharges(productCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerChargeOverrideResponse>> getCustomerOverrides(@PathVariable Long customerId) {
        List<CustomerChargeOverrideResponse> response = service.getCustomerOverrides(customerId);
        return ResponseEntity.ok(response);
    }
}
