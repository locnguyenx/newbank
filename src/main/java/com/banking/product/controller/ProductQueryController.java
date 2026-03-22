package com.banking.product.controller;

import com.banking.product.domain.enums.CustomerType;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.service.ProductQueryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-query")
public class ProductQueryController {

    private final ProductQueryServiceImpl productQueryService;

    public ProductQueryController(ProductQueryServiceImpl productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping("/active")
    public ResponseEntity<ProductVersionResponse> getActiveProductByCode(@RequestParam String code) {
        return productQueryService.getActiveProductByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/version/{versionId}")
    public ResponseEntity<ProductVersionResponse> getProductVersionById(@PathVariable Long versionId) {
        return productQueryService.getProductVersionById(versionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/family/{family}")
    public ResponseEntity<List<ProductVersionResponse>> getProductsByFamily(
            @PathVariable ProductFamily family,
            @RequestParam(required = false) ProductStatus status) {
        List<ProductVersionResponse> products = productQueryService.getActiveProductsByFamily(family);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/customer-type/{type}")
    public ResponseEntity<List<ProductVersionResponse>> getProductsByCustomerType(
            @PathVariable CustomerType type,
            @RequestParam(required = false) ProductStatus status) {
        List<ProductVersionResponse> products = productQueryService.getActiveProductsByCustomerType(type);
        return ResponseEntity.ok(products);
    }
}