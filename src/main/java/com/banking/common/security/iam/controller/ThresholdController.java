package com.banking.common.security.iam.controller;

import com.banking.common.message.MessageCatalog;
import com.banking.common.security.iam.dto.ThresholdRequest;
import com.banking.common.security.rbac.AmountThreshold;
import com.banking.common.security.iam.service.ThresholdManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/iam/thresholds")
@PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'COMPANY_ADMIN')")
public class ThresholdController {

    private final ThresholdManagementService thresholdManagementService;

    public ThresholdController(ThresholdManagementService thresholdManagementService) {
        this.thresholdManagementService = thresholdManagementService;
    }

    @PostMapping
    public ResponseEntity<AmountThreshold> setThreshold(@Valid @RequestBody ThresholdRequest request) {
        return ResponseEntity.ok(thresholdManagementService.setThreshold(request));
    }

    @GetMapping
    public ResponseEntity<List<AmountThreshold>> listThresholds() {
        return ResponseEntity.ok(thresholdManagementService.listThresholds());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AmountThreshold>> getUserThresholds(@PathVariable Long userId) {
        return ResponseEntity.ok(thresholdManagementService.getUserThresholds(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmountThreshold> getThreshold(@PathVariable Long id) {
        return ResponseEntity.ok(thresholdManagementService.getThreshold(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmountThreshold> updateThreshold(@PathVariable Long id, @RequestParam BigDecimal threshold) {
        return ResponseEntity.ok(thresholdManagementService.updateThreshold(id, threshold));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> deleteThreshold(@PathVariable Long id) {
        thresholdManagementService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
}