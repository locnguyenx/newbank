package com.banking.charges.controller;

import com.banking.charges.dto.request.CreateInterestRateRequest;
import com.banking.charges.dto.response.InterestRateResponse;
import com.banking.charges.service.InterestRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges/interest")
public class InterestRateController {

    private final InterestRateService interestRateService;

    public InterestRateController(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    @PostMapping
    public ResponseEntity<InterestRateResponse> createInterestRate(@RequestBody CreateInterestRateRequest request) {
        InterestRateResponse response = interestRateService.createInterestRate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterestRateResponse> updateInterestRate(@PathVariable Long id, @RequestBody CreateInterestRateRequest request) {
        InterestRateResponse response = interestRateService.updateInterestRate(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterestRateResponse> getInterestRate(@PathVariable Long id) {
        InterestRateResponse response = interestRateService.getInterestRate(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<List<InterestRateResponse>> getInterestRatesByProduct(@PathVariable String productCode) {
        List<InterestRateResponse> response = interestRateService.getInterestRatesByProduct(productCode);
        return ResponseEntity.ok(response);
    }
}
