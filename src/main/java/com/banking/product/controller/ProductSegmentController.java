package com.banking.product.controller;

import com.banking.product.dto.request.ProductSegmentRequest;
import com.banking.product.dto.response.ProductCustomerSegmentResponse;
import com.banking.product.service.ProductSegmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/versions/{versionId}/segments")
public class ProductSegmentController {

    private final ProductSegmentService productSegmentService;

    public ProductSegmentController(ProductSegmentService productSegmentService) {
        this.productSegmentService = productSegmentService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCustomerSegmentResponse>> getSegments(
            @PathVariable Long productId,
            @PathVariable Long versionId) {
        List<ProductCustomerSegmentResponse> segments = productSegmentService.getSegments(versionId);
        return ResponseEntity.ok(segments);
    }

    @PutMapping
    public ResponseEntity<List<ProductCustomerSegmentResponse>> assignSegments(
            @PathVariable Long productId,
            @PathVariable Long versionId,
            @Valid @RequestBody ProductSegmentRequest request,
            @RequestHeader("X-Username") String username) {
        List<ProductCustomerSegmentResponse> response = productSegmentService.assignSegments(
                versionId, request.getCustomerTypes(), username);
        return ResponseEntity.ok(response);
    }
}