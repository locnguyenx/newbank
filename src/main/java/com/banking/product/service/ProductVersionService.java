package com.banking.product.service;

import com.banking.product.domain.entity.*;
import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionDiffResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.exception.MakerCheckerViolationException;
import com.banking.product.exception.ProductHasActiveContractsException;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductVersionService {

    private final ProductVersionRepository productVersionRepository;
    private final ProductAuditService productAuditService;
    private final ProductMapper productMapper;

    public ProductVersionService(ProductVersionRepository productVersionRepository,
                                  ProductAuditService productAuditService,
                                  ProductMapper productMapper) {
        this.productVersionRepository = productVersionRepository;
        this.productAuditService = productAuditService;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductVersionResponse submitForApproval(Long versionId, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (version.getStatus() != ProductStatus.DRAFT) {
            throw new InvalidProductStatusException("Only DRAFT version can be submitted for approval");
        }

        version.setStatus(ProductStatus.PENDING_APPROVAL);
        version.setSubmittedBy(username);
        ProductVersion saved = productVersionRepository.save(version);

        productAuditService.log(versionId, AuditAction.SUBMIT, username,
                ProductStatus.DRAFT, ProductStatus.PENDING_APPROVAL, null, null);

        return productMapper.toVersionResponse(saved);
    }

    @Transactional
    public ProductVersionResponse approve(Long versionId, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (version.getStatus() != ProductStatus.PENDING_APPROVAL) {
            throw new InvalidProductStatusException("Only PENDING_APPROVAL version can be approved");
        }

        if (username.equals(version.getSubmittedBy())) {
            throw new MakerCheckerViolationException("Approver cannot be the same as submitter");
        }

        version.setStatus(ProductStatus.APPROVED);
        version.setApprovedBy(username);
        ProductVersion saved = productVersionRepository.save(version);

        productAuditService.log(versionId, AuditAction.APPROVE, username,
                ProductStatus.PENDING_APPROVAL, ProductStatus.APPROVED, null, version.getSubmittedBy());

        return productMapper.toVersionResponse(saved);
    }

    @Transactional
    public ProductVersionResponse reject(Long versionId, String username, String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection comment is required");
        }

        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (version.getStatus() != ProductStatus.PENDING_APPROVAL) {
            throw new InvalidProductStatusException("Only PENDING_APPROVAL version can be rejected");
        }

        version.setStatus(ProductStatus.DRAFT);
        version.setRejectionComment(comment);
        ProductVersion saved = productVersionRepository.save(version);

        productAuditService.log(versionId, AuditAction.REJECT, username,
                ProductStatus.PENDING_APPROVAL, ProductStatus.DRAFT, comment, version.getSubmittedBy());

        return productMapper.toVersionResponse(saved);
    }

    @Transactional
    public ProductVersionResponse activate(Long versionId, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (version.getStatus() != ProductStatus.APPROVED) {
            throw new InvalidProductStatusException("Only APPROVED version can be activated");
        }

        productVersionRepository.findByProductIdAndStatus(version.getProduct().getId(), ProductStatus.ACTIVE)
                .ifPresent(activeVersion -> {
                    activeVersion.setStatus(ProductStatus.SUPERSEDED);
                    productVersionRepository.save(activeVersion);
                });

        version.setStatus(ProductStatus.ACTIVE);
        ProductVersion saved = productVersionRepository.save(version);

        productAuditService.log(versionId, AuditAction.ACTIVATE, username,
                ProductStatus.APPROVED, ProductStatus.ACTIVE, null, null);

        return productMapper.toVersionResponse(saved);
    }

    @Transactional
    public ProductVersionResponse retire(Long versionId, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (version.getStatus() != ProductStatus.ACTIVE) {
            throw new InvalidProductStatusException("Only ACTIVE version can be retired");
        }

        if (version.getContractCount() != null && version.getContractCount() > 0) {
            throw new ProductHasActiveContractsException(version.getProduct().getCode());
        }

        version.setStatus(ProductStatus.RETIRED);
        ProductVersion saved = productVersionRepository.save(version);

        productAuditService.log(versionId, AuditAction.RETIRE, username,
                ProductStatus.ACTIVE, ProductStatus.RETIRED, null, null);

        return productMapper.toVersionResponse(saved);
    }

    @Transactional
    public ProductVersionResponse createNewVersionFromActive(Long productId) {
        ProductVersion activeVersion = productVersionRepository
                .findTopByProductIdOrderByVersionNumberDesc(productId)
                .orElseThrow(() -> new IllegalArgumentException("No version found for product: " + productId));

        if (activeVersion.getStatus() != ProductStatus.ACTIVE) {
            throw new ProductVersionNotEditableException(activeVersion.getId());
        }

        ProductVersion newVersion = new ProductVersion(
                activeVersion.getProduct(),
                activeVersion.getVersionNumber() + 1,
                ProductStatus.DRAFT
        );
        newVersion.getAudit().setCreatedBy("system");
        newVersion.getAudit().setUpdatedBy("system");

        copyFeatures(activeVersion, newVersion);
        copyFeeEntries(activeVersion, newVersion);
        copySegments(activeVersion, newVersion);

        ProductVersion saved = productVersionRepository.save(newVersion);

        productAuditService.log(saved.getId(), AuditAction.CREATE, "system",
                null, ProductStatus.DRAFT, null, null);

        return productMapper.toVersionResponse(saved);
    }

    private void copyFeatures(ProductVersion from, ProductVersion to) {
        for (ProductFeature feature : from.getFeatures()) {
            ProductFeature newFeature = new ProductFeature(to, feature.getFeatureKey(), feature.getFeatureValue());
            newFeature.getAudit().setCreatedBy("system");
            newFeature.getAudit().setUpdatedBy("system");
            to.getFeatures().add(newFeature);
        }
    }

    private void copyFeeEntries(ProductVersion from, ProductVersion to) {
        for (ProductFeeEntry feeEntry : from.getFeeEntries()) {
            ProductFeeEntry newFeeEntry = new ProductFeeEntry(
                    to,
                    feeEntry.getFeeType(),
                    feeEntry.getCalculationMethod(),
                    feeEntry.getAmount(),
                    feeEntry.getRate(),
                    feeEntry.getCurrency()
            );
            newFeeEntry.getAudit().setCreatedBy("system");
            newFeeEntry.getAudit().setUpdatedBy("system");

            for (ProductFeeTier tier : feeEntry.getTiers()) {
                ProductFeeTier newTier = new ProductFeeTier(newFeeEntry, tier.getTierFrom(), tier.getTierTo(), tier.getRate());
                newFeeEntry.getTiers().add(newTier);
            }

            to.getFeeEntries().add(newFeeEntry);
        }
    }

    private void copySegments(ProductVersion from, ProductVersion to) {
        for (ProductCustomerSegment segment : from.getCustomerSegments()) {
            ProductCustomerSegment newSegment = new ProductCustomerSegment(to, segment.getCustomerType());
            newSegment.getAudit().setCreatedBy("system");
            newSegment.getAudit().setUpdatedBy("system");
            to.getCustomerSegments().add(newSegment);
        }
    }

    @Transactional(readOnly = true)
    public ProductVersionResponse getVersion(Long versionId) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));
        return productMapper.toVersionResponse(version);
    }

    @Transactional(readOnly = true)
    public List<ProductVersionResponse> getVersionHistory(Long productId) {
        return productVersionRepository.findByProductIdOrderByVersionNumberDesc(productId)
                .stream()
                .map(productMapper::toVersionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductVersionDiffResponse compareVersions(Long versionId1, Long versionId2) {
        ProductVersion version1 = productVersionRepository.findById(versionId1)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId1));
        ProductVersion version2 = productVersionRepository.findById(versionId2)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId2));

        ProductVersionDiffResponse diffResponse = new ProductVersionDiffResponse();
        diffResponse.setVersionId1(versionId1);
        diffResponse.setVersionId2(versionId2);
        diffResponse.setGeneralFields(new ArrayList<>());
        diffResponse.setFeatures(new ArrayList<>());
        diffResponse.setFees(new ArrayList<>());
        diffResponse.setSegments(new ArrayList<>());

        return diffResponse;
    }
}
