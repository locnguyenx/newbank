package com.banking.product.controller;

import com.banking.product.dto.request.ProductFeatureRequest;
import com.banking.product.dto.response.ProductFeatureResponse;
import com.banking.product.service.ProductFeatureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/versions/{versionId}/features")
public class ProductFeatureController {

    private final ProductFeatureService productFeatureService;

    public ProductFeatureController(ProductFeatureService productFeatureService) {
        this.productFeatureService = productFeatureService;
    }

    @GetMapping
    public ResponseEntity<List<ProductFeatureResponse>> listFeatures(
            @PathVariable Long productId,
            @PathVariable Long versionId) {
        List<ProductFeatureResponse> features = productFeatureService.getFeatures(versionId);
        return ResponseEntity.ok(features);
    }

    @PostMapping
    public ResponseEntity<ProductFeatureResponse> addFeature(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @Valid @RequestBody ProductFeatureRequest request,
            @RequestHeader("X-Username") String username) {
        ProductFeatureResponse response = productFeatureService.addFeature(versionId, request, username);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{featureId}")
    public ResponseEntity<ProductFeatureResponse> updateFeature(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @PathVariable Long featureId,
            @Valid @RequestBody ProductFeatureRequest request,
            @RequestHeader("X-Username") String username) {
        ProductFeatureResponse response = productFeatureService.updateFeature(featureId, request, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<Void> deleteFeature(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @PathVariable Long featureId) {
        productFeatureService.removeFeature(featureId);
        return ResponseEntity.noContent().build();
    }
}