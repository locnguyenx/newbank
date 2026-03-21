package com.banking.limits.controller;

import com.banking.limits.dto.request.LimitCheckRequest;
import com.banking.limits.dto.response.EffectiveLimitResponse;
import com.banking.limits.dto.response.LimitCheckResponse;
import com.banking.limits.service.LimitCheckService;
import com.banking.limits.service.LimitQueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits/check")
public class LimitCheckController {

    private final LimitCheckService limitCheckService;
    private final LimitQueryService limitQueryService;

    public LimitCheckController(LimitCheckService limitCheckService, LimitQueryService limitQueryService) {
        this.limitCheckService = limitCheckService;
        this.limitQueryService = limitQueryService;
    }

    @PostMapping
    public ResponseEntity<LimitCheckResponse> checkLimit(@Valid @RequestBody LimitCheckRequest request) {
        LimitCheckResponse response = limitCheckService.checkLimit(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/effective")
    public ResponseEntity<List<EffectiveLimitResponse>> getEffectiveLimits(
            @RequestParam String accountNumber,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String productCode,
            @RequestParam String currency) {
        List<EffectiveLimitResponse> response = limitQueryService.getAllEffectiveLimits(
                accountNumber, customerId, productCode, currency);
        return ResponseEntity.ok(response);
    }
}