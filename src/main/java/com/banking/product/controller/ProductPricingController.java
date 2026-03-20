package com.banking.product.controller;

import com.banking.product.dto.request.ProductFeeEntryRequest;
import com.banking.product.dto.response.ProductFeeEntryResponse;
import com.banking.product.service.ProductPricingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/versions/{versionId}/fees")
public class ProductPricingController {

    private final ProductPricingService productPricingService;

    public ProductPricingController(ProductPricingService productPricingService) {
        this.productPricingService = productPricingService;
    }

    @GetMapping
    public ResponseEntity<List<ProductFeeEntryResponse>> listFeeEntries(
            @PathVariable Long productId,
            @PathVariable Long versionId) {
        List<ProductFeeEntryResponse> fees = productPricingService.getFeeEntries(versionId);
        return ResponseEntity.ok(fees);
    }

    @PostMapping
    public ResponseEntity<ProductFeeEntryResponse> addFeeEntry(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @Valid @RequestBody ProductFeeEntryRequest request,
            @RequestHeader("X-Username") String username) {
        ProductFeeEntryResponse response = productPricingService.addFeeEntry(versionId, request, username);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{feeEntryId}")
    public ResponseEntity<ProductFeeEntryResponse> updateFeeEntry(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @PathVariable Long feeEntryId,
            @Valid @RequestBody ProductFeeEntryRequest request,
            @RequestHeader("X-Username") String username) {
        ProductFeeEntryResponse response = productPricingService.updateFeeEntry(feeEntryId, request, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{feeEntryId}")
    public ResponseEntity<Void> deleteFeeEntry(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @PathVariable Long feeEntryId) {
        productPricingService.removeFeeEntry(feeEntryId);
        return ResponseEntity.noContent().build();
    }
}