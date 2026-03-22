package com.banking.product.service;

import com.banking.product.api.dto.ProductDTO;
import com.banking.product.api.dto.ProductVersionDTO;
import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.CustomerType;
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
public class ProductQueryServiceImpl implements com.banking.product.api.ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ProductCustomerSegmentRepository productCustomerSegmentRepository;
    private final ProductMapper productMapper;

    public ProductQueryServiceImpl(ProductRepository productRepository,
                                   ProductVersionRepository productVersionRepository,
                                   ProductCustomerSegmentRepository productCustomerSegmentRepository,
                                   ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productVersionRepository = productVersionRepository;
        this.productCustomerSegmentRepository = productCustomerSegmentRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findByCode(String code) {
        return productRepository.findByCode(code)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findByFamily(String family) {
        ProductFamily productFamily = ProductFamily.valueOf(family);
        return productRepository.findByFamily(productFamily).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findActiveProducts() {
        return productVersionRepository.findByStatus(ProductStatus.ACTIVE).stream()
                .map(ProductVersion::getProduct)
                .distinct()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVersionDTO> findActiveVersionByCode(String code) {
        List<ProductVersion> activeVersions = productVersionRepository.findByProductCodeAndStatus(code, ProductStatus.ACTIVE);
        if (activeVersions.isEmpty()) {
            return Optional.empty();
        }
        ProductVersion v = activeVersions.get(0);
        ProductVersionDTO dto = new ProductVersionDTO();
        dto.setId(v.getId());
        dto.setProductId(v.getProduct() != null ? v.getProduct().getId() : null);
        dto.setProductName(v.getProduct() != null ? v.getProduct().getName() : null);
        dto.setVersionNumber(v.getVersionNumber());
        dto.setStatus(v.getStatus() != null ? v.getStatus().name() : null);
        return Optional.of(dto);
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

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setFamily(product.getFamily().name());
        dto.setVersion(product.getVersion());
        return dto;
    }
}
