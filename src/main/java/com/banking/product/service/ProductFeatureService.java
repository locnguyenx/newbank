package com.banking.product.service;

import com.banking.product.domain.entity.ProductFeature;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.ProductFeatureRequest;
import com.banking.product.dto.response.ProductFeatureResponse;
import com.banking.product.exception.InvalidFeaturePrefixException;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.repository.ProductFeatureRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductFeatureService {

    private final ProductFeatureRepository productFeatureRepository;
    private final ProductVersionRepository productVersionRepository;

    public ProductFeatureService(ProductFeatureRepository productFeatureRepository,
                                  ProductVersionRepository productVersionRepository) {
        this.productFeatureRepository = productFeatureRepository;
        this.productVersionRepository = productVersionRepository;
    }

    @Transactional
    public ProductFeatureResponse addFeature(Long versionId, ProductFeatureRequest request, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        validateVersionIsDraft(version);
        validateFeaturePrefix(version, request.getFeatureKey());

        ProductFeature feature = new ProductFeature(version, request.getFeatureKey(), request.getFeatureValue());
        feature.getAudit().setCreatedBy(username);
        feature.getAudit().setUpdatedBy(username);

        ProductFeature saved = productFeatureRepository.save(feature);
        return ProductFeatureResponse.fromEntity(saved);
    }

    @Transactional
    public ProductFeatureResponse updateFeature(Long featureId, ProductFeatureRequest request, String username) {
        ProductFeature feature = productFeatureRepository.findById(featureId)
                .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + featureId));

        validateVersionIsDraft(feature.getProductVersion());

        feature.setFeatureKey(request.getFeatureKey());
        feature.setFeatureValue(request.getFeatureValue());
        feature.getAudit().setUpdatedBy(username);

        ProductFeature saved = productFeatureRepository.save(feature);
        return ProductFeatureResponse.fromEntity(saved);
    }

    @Transactional
    public void removeFeature(Long featureId) {
        ProductFeature feature = productFeatureRepository.findById(featureId)
                .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + featureId));

        validateVersionIsDraft(feature.getProductVersion());
        productFeatureRepository.deleteById(featureId);
    }

    @Transactional(readOnly = true)
    public List<ProductFeatureResponse> getFeatures(Long versionId) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        return version.getFeatures().stream()
                .map(ProductFeatureResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateVersionIsDraft(ProductVersion version) {
        if (version.getStatus() != ProductStatus.DRAFT) {
            throw new ProductVersionNotEditableException(version.getId());
        }
    }

    private void validateFeaturePrefix(ProductVersion version, String featureKey) {
        ProductFamily family = version.getProduct().getFamily();
        String expectedPrefix = family.name().toLowerCase() + ".";
        if (!featureKey.startsWith(expectedPrefix)) {
            throw new InvalidFeaturePrefixException(featureKey);
        }
    }
}
