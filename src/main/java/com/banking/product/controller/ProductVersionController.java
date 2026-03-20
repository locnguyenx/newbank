package com.banking.product.controller;

import com.banking.product.dto.request.RejectProductRequest;
import com.banking.product.dto.response.ProductVersionDiffResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.service.ProductVersionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/versions")
public class ProductVersionController {

    private final ProductVersionService productVersionService;

    public ProductVersionController(ProductVersionService productVersionService) {
        this.productVersionService = productVersionService;
    }

    @GetMapping
    public ResponseEntity<List<ProductVersionResponse>> listVersions(@PathVariable Long productId) {
        List<ProductVersionResponse> versions = productVersionService.getVersionHistory(productId);
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<ProductVersionResponse> getVersionDetail(
            @PathVariable Long productId,
            @PathVariable Long versionId) {
        ProductVersionResponse response = productVersionService.getVersion(versionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{versionId}/submit")
    public ResponseEntity<ProductVersionResponse> submitForApproval(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @RequestHeader("X-Username") String username) {
        ProductVersionResponse response = productVersionService.submitForApproval(versionId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{versionId}/approve")
    public ResponseEntity<ProductVersionResponse> approve(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @RequestHeader("X-Username") String username) {
        ProductVersionResponse response = productVersionService.approve(versionId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{versionId}/reject")
    public ResponseEntity<ProductVersionResponse> reject(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @Valid @RequestBody RejectProductRequest request,
            @RequestHeader("X-Username") String username) {
        ProductVersionResponse response = productVersionService.reject(versionId, username, request.getComment());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{versionId}/activate")
    public ResponseEntity<ProductVersionResponse> activate(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @RequestHeader("X-Username") String username) {
        ProductVersionResponse response = productVersionService.activate(versionId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{versionId}/retire")
    public ResponseEntity<ProductVersionResponse> retire(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @RequestHeader("X-Username") String username) {
        ProductVersionResponse response = productVersionService.retire(versionId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/compare")
    public ResponseEntity<ProductVersionDiffResponse> compareVersions(
            @PathVariable Long productId,
            @RequestParam Long v1,
            @RequestParam Long v2) {
        ProductVersionDiffResponse response = productVersionService.compareVersions(v1, v2);
        return ResponseEntity.ok(response);
    }
}