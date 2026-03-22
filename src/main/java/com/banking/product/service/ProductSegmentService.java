package com.banking.product.service;

import com.banking.product.domain.enums.CustomerType;
import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductCustomerSegmentResponse;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.repository.ProductCustomerSegmentRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSegmentService {

    private final ProductCustomerSegmentRepository productCustomerSegmentRepository;
    private final ProductVersionRepository productVersionRepository;

    public ProductSegmentService(ProductCustomerSegmentRepository productCustomerSegmentRepository,
                                  ProductVersionRepository productVersionRepository) {
        this.productCustomerSegmentRepository = productCustomerSegmentRepository;
        this.productVersionRepository = productVersionRepository;
    }

    @Transactional
    public List<ProductCustomerSegmentResponse> assignSegments(Long versionId, List<CustomerType> customerTypes, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        validateVersionIsDraft(version);

        version.getCustomerSegments().clear();

        for (CustomerType customerType : customerTypes) {
            ProductCustomerSegment segment = new ProductCustomerSegment(version, customerType);
            segment.getAudit().setCreatedBy(username);
            segment.getAudit().setUpdatedBy(username);
            version.getCustomerSegments().add(segment);
        }

        productVersionRepository.save(version);

        return version.getCustomerSegments().stream()
                .map(ProductCustomerSegmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCustomerSegmentResponse> getSegments(Long versionId) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        return version.getCustomerSegments().stream()
                .map(ProductCustomerSegmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateVersionIsDraft(ProductVersion version) {
        if (version.getStatus() != ProductStatus.DRAFT) {
            throw new ProductVersionNotEditableException(version.getId());
        }
    }
}
