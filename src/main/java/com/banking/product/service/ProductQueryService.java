package com.banking.product.service;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductCustomerSegmentRepository;
import com.banking.product.repository.ProductRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ProductCustomerSegmentRepository productCustomerSegmentRepository;
    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository,
                               ProductVersionRepository productVersionRepository,
                               ProductCustomerSegmentRepository productCustomerSegmentRepository,
                               ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productVersionRepository = productVersionRepository;
        this.productCustomerSegmentRepository = productCustomerSegmentRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public Optional<ProductVersionResponse> getActiveProductByCode(String code) {
        List<ProductVersion> activeVersions = productVersionRepository.findByProductCodeAndStatus(code, ProductStatus.ACTIVE);
        if (activeVersions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(productMapper.toVersionResponse(activeVersions.get(0)));
    }

    @Transactional(readOnly = true)
    public Optional<ProductVersionResponse> getProductVersionById(Long versionId) {
        return productVersionRepository.findById(versionId)
                .map(productMapper::toVersionResponse);
    }

    @Transactional(readOnly = true)
    public List<ProductVersionResponse> getActiveProductsByFamily(ProductFamily family) {
        return productVersionRepository.findByStatus(ProductStatus.ACTIVE).stream()
                .filter(v -> v.getProduct().getFamily() == family)
                .map(productMapper::toVersionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductVersionResponse> getActiveProductsByCustomerType(CustomerType customerType) {
        List<ProductCustomerSegment> segments = productCustomerSegmentRepository.findByCustomerType(customerType);
        
        return segments.stream()
                .map(ProductCustomerSegment::getProductVersion)
                .filter(v -> v.getStatus() == ProductStatus.ACTIVE)
                .distinct()
                .map(productMapper::toVersionResponse)
                .collect(Collectors.toList());
    }
}
