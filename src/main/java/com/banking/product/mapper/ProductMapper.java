package com.banking.product.mapper;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductFeature;
import com.banking.product.domain.entity.ProductFeeEntry;
import com.banking.product.domain.entity.ProductFeeTier;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.entity.ProductAuditLog;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.CreateProductRequest;
import com.banking.product.dto.response.ProductResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.dto.response.ProductDetailResponse;
import com.banking.product.dto.response.ProductSummaryResponse;
import com.banking.product.dto.response.ProductFeatureResponse;
import com.banking.product.dto.response.ProductFeeEntryResponse;
import com.banking.product.dto.response.ProductFeeTierResponse;
import com.banking.product.dto.response.ProductAuditLogResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request) {
        Product product = new Product(
            request.getCode(),
            request.getName(),
            request.getFamily(),
            request.getDescription()
        );
        return product;
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = ProductResponse.fromEntity(product);
        response.setStatus(ProductStatus.DRAFT.name());
        if (response.getVersionNumber() == null) {
            response.setVersionNumber(1L);
        }
        return response;
    }

    public ProductVersionResponse toVersionResponse(ProductVersion version) {
        return ProductVersionResponse.fromEntity(version);
    }

    public ProductDetailResponse toDetailResponse(ProductVersion version) {
        return ProductDetailResponse.fromEntity(version);
    }

    public ProductSummaryResponse toSummaryResponse(Product product) {
        return ProductSummaryResponse.fromEntity(product);
    }

    public ProductFeatureResponse toFeatureResponse(ProductFeature feature) {
        return ProductFeatureResponse.fromEntity(feature);
    }

    public ProductFeeEntryResponse toFeeEntryResponse(ProductFeeEntry feeEntry) {
        return ProductFeeEntryResponse.fromEntity(feeEntry);
    }

    public ProductFeeTierResponse toTierResponse(ProductFeeTier tier) {
        return ProductFeeTierResponse.fromEntity(tier);
    }

    public ProductAuditLogResponse toAuditLogResponse(ProductAuditLog auditLog) {
        return ProductAuditLogResponse.fromEntity(auditLog);
    }
}