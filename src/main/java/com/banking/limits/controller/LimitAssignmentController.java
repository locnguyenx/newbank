package com.banking.limits.controller;

import com.banking.limits.dto.request.AssignLimitRequest;
import com.banking.limits.dto.response.AccountLimitResponse;
import com.banking.limits.dto.response.CustomerLimitResponse;
import com.banking.limits.dto.response.ProductLimitResponse;
import com.banking.limits.service.LimitAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits/assignments")
public class LimitAssignmentController {

    private final LimitAssignmentService limitAssignmentService;

    public LimitAssignmentController(LimitAssignmentService limitAssignmentService) {
        this.limitAssignmentService = limitAssignmentService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductLimitResponse> assignToProduct(@Valid @RequestBody AssignLimitRequest request) {
        ProductLimitResponse response = limitAssignmentService.assignToProduct(
                request.getLimitId(), request.getReferenceId(), request.getOverrideAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerLimitResponse> assignToCustomer(@Valid @RequestBody AssignLimitRequest request) {
        CustomerLimitResponse response = limitAssignmentService.assignToCustomer(
                request.getLimitId(), Long.parseLong(request.getReferenceId()), request.getOverrideAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/account")
    public ResponseEntity<AccountLimitResponse> assignToAccount(@Valid @RequestBody AssignLimitRequest request) {
        AccountLimitResponse response = limitAssignmentService.assignToAccount(
                request.getLimitId(), request.getReferenceId(), request.getOverrideAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/product/{code}")
    public ResponseEntity<List<ProductLimitResponse>> getProductLimits(@PathVariable String code) {
        List<ProductLimitResponse> response = limitAssignmentService.getProductLimits(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<CustomerLimitResponse>> getCustomerLimits(@PathVariable Long id) {
        List<CustomerLimitResponse> response = limitAssignmentService.getCustomerLimits(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{num}")
    public ResponseEntity<List<AccountLimitResponse>> getAccountLimits(@PathVariable String num) {
        List<AccountLimitResponse> response = limitAssignmentService.getAccountLimits(num);
        return ResponseEntity.ok(response);
    }
}
