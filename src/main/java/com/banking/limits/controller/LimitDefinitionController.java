package com.banking.limits.controller;

import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.dto.request.CreateLimitDefinitionRequest;
import com.banking.limits.dto.response.LimitDefinitionResponse;
import com.banking.limits.service.LimitDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits/definitions")
public class LimitDefinitionController {

    private final LimitDefinitionService limitDefinitionService;

    public LimitDefinitionController(LimitDefinitionService limitDefinitionService) {
        this.limitDefinitionService = limitDefinitionService;
    }

    @PostMapping
    public ResponseEntity<LimitDefinitionResponse> createLimit(@Valid @RequestBody CreateLimitDefinitionRequest request) {
        LimitDefinitionResponse response = limitDefinitionService.createLimit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LimitDefinitionResponse>> getAllLimits(
            @RequestParam(required = false) LimitStatus status) {
        List<LimitDefinitionResponse> response = limitDefinitionService.getAllLimits(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LimitDefinitionResponse> getLimit(@PathVariable Long id) {
        LimitDefinitionResponse response = limitDefinitionService.getLimit(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LimitDefinitionResponse> updateLimit(
            @PathVariable Long id,
            @Valid @RequestBody CreateLimitDefinitionRequest request) {
        LimitDefinitionResponse response = limitDefinitionService.updateLimit(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<LimitDefinitionResponse> activateLimit(@PathVariable Long id) {
        LimitDefinitionResponse response = limitDefinitionService.activateLimit(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<LimitDefinitionResponse> deactivateLimit(@PathVariable Long id) {
        LimitDefinitionResponse response = limitDefinitionService.deactivateLimit(id);
        return ResponseEntity.ok(response);
    }
}
