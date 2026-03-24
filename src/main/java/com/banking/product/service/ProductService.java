package com.banking.product.service;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.CreateProductRequest;
import com.banking.product.dto.request.UpdateProductRequest;
import com.banking.product.dto.response.ProductDetailResponse;
import com.banking.product.dto.response.ProductResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.exception.DuplicateProductCodeException;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.exception.ProductNotFoundException;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductVersionRepository productVersionRepository;
    private final ProductSegmentService productSegmentService;

    public ProductService(ProductRepository productRepository, 
                         ProductMapper productMapper, 
                         ProductVersionRepository productVersionRepository,
                         ProductSegmentService productSegmentService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productVersionRepository = productVersionRepository;
        this.productSegmentService = productSegmentService;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, String createdBy) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (createdBy == null) {
            throw new IllegalArgumentException("CreatedBy cannot be null");
        }
        if (productRepository.existsByCode(request.getCode())) {
            throw new DuplicateProductCodeException(request.getCode());
        }

        Product product = productMapper.toEntity(request);
        product.getAudit().setCreatedBy(createdBy);
        product.getAudit().setUpdatedBy(createdBy);
        
        Product savedProduct = productRepository.save(product);

        ProductVersion version = new ProductVersion(savedProduct, 1, ProductStatus.DRAFT);
        version.getAudit().setCreatedBy(createdBy);
        version.getAudit().setUpdatedBy(createdBy);
        ProductVersion savedVersion = productVersionRepository.save(version);

        if (request.getCustomerTypes() != null && !request.getCustomerTypes().isEmpty()) {
            productSegmentService.assignSegments(savedVersion.getId(), request.getCustomerTypes(), createdBy);
        }

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request, String updatedBy) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (updatedBy == null) {
            throw new IllegalArgumentException("UpdatedBy cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        if (request.getCode() != null && !request.getCode().equals(product.getCode())) {
            throw new InvalidProductStatusException("Product code cannot be changed");
        }

        ProductVersion currentVersion = productVersionRepository
                .findTopByProductIdOrderByVersionNumberDesc(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        if (currentVersion.getStatus() != ProductStatus.DRAFT) {
            throw new InvalidProductStatusException("Only DRAFT version can be updated");
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        product.getAudit().setUpdatedBy(updatedBy);

        if (request.getCustomerTypes() != null && !request.getCustomerTypes().isEmpty()) {
            productSegmentService.assignSegments(currentVersion.getId(), request.getCustomerTypes(), updatedBy);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductVersionResponse createProductVersion(Long productId, int versionNumber, ProductStatus status, String createdBy) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        
        ProductVersion version = new ProductVersion(product, versionNumber, status);
        version.getAudit().setCreatedBy(createdBy);
        version.getAudit().setUpdatedBy(createdBy);
        ProductVersion savedVersion = productVersionRepository.save(version);
        return productMapper.toVersionResponse(savedVersion);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id.toString()));
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long productId) {
        ProductVersion currentVersion = productVersionRepository
                .findTopByProductIdOrderByVersionNumberDesc(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        return ProductDetailResponse.fromEntity(currentVersion);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException(code));
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(Specification<Product> spec, Pageable pageable) {
        Page<Product> products;
        if (spec == null) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findAll(spec, pageable);
        }
        return products.map(productMapper::toResponse);
    }
}