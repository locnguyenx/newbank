package com.banking.product.controller;

import com.banking.product.domain.entity.Product;
import com.banking.product.dto.request.CreateProductRequest;
import com.banking.product.dto.request.UpdateProductRequest;
import com.banking.product.dto.response.ProductDetailResponse;
import com.banking.product.dto.response.ProductResponse;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.service.ProductService;
import com.banking.product.service.ProductVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductVersionService productVersionService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, 
                            ProductVersionService productVersionService, 
                            ProductMapper productMapper) {
        this.productService = productService;
        this.productVersionService = productVersionService;
        this.productMapper = productMapper;
    }

     @Operation(summary = "Create a new product", description = "Create product with basic info. Requires X-Username header for audit.")
     @PostMapping
     public ResponseEntity<ProductResponse> createProduct(
             @Parameter(in = ParameterIn.HEADER, description = "Username of the user performing the action", required = true) @RequestHeader("X-Username") String username,
             @Valid @RequestBody CreateProductRequest request) {
         ProductResponse response = productService.createProduct(request, username);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
     }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        ProductDetailResponse response = productService.getProductDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponse> getProductByCode(@PathVariable String code) {
        ProductResponse response = productService.getProductByCode(code);
        return ResponseEntity.ok(response);
    }

     @Operation(summary = "Update an existing product")
     @PutMapping("/{id}")
     public ResponseEntity<ProductResponse> updateProduct(
             @Parameter(in = ParameterIn.PATH, description = "Product ID", required = true) @PathVariable Long id,
             @Valid @RequestBody UpdateProductRequest request,
             @Parameter(in = ParameterIn.HEADER, description = "Username of the user performing the action", required = true) @RequestHeader("X-Username") String username) {
         ProductResponse response = productService.updateProduct(id, request, username);
         return ResponseEntity.ok(response);
     }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Specification<Product> spec = null;
        Page<ProductResponse> products = productService.searchProducts(spec, PageRequest.of(page, size));
        return ResponseEntity.ok(products);
    }
}