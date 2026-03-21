package com.banking.product.service;

import com.banking.product.domain.entity.ProductFeeEntry;
import com.banking.product.domain.entity.ProductFeeTier;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.ProductFeeEntryRequest;
import com.banking.product.dto.request.ProductFeeTierRequest;
import com.banking.product.dto.response.ProductFeeEntryResponse;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.repository.ProductFeeEntryRepository;
import com.banking.product.repository.ProductVersionRepository;
import com.banking.masterdata.repository.CurrencyRepository;
import com.banking.masterdata.domain.entity.Currency;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Deprecated
public class ProductPricingService {

    private final ProductFeeEntryRepository productFeeEntryRepository;
    private final ProductVersionRepository productVersionRepository;
    private final CurrencyRepository currencyRepository;

    public ProductPricingService(ProductFeeEntryRepository productFeeEntryRepository,
                                  ProductVersionRepository productVersionRepository,
                                  CurrencyRepository currencyRepository) {
        this.productFeeEntryRepository = productFeeEntryRepository;
        this.productVersionRepository = productVersionRepository;
        this.currencyRepository = currencyRepository;
    }

    private void validateCurrency(String code) {
        if (code == null || code.isBlank()) {
            return;
        }
        Currency currency = currencyRepository.findById(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid currency code: " + code));
        if (!currency.isActive()) {
            throw new IllegalArgumentException("Currency is inactive: " + code);
        }
    }

    @Transactional
    public ProductFeeEntryResponse addFeeEntry(Long versionId, ProductFeeEntryRequest request, String username) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        validateVersionIsDraft(version);

        validateCurrency(request.getCurrency());
        ProductFeeEntry feeEntry = new ProductFeeEntry(
                version,
                request.getFeeType(),
                request.getCalculationMethod(),
                request.getAmount(),
                request.getRate(),
                request.getCurrency()
        );
        feeEntry.getAudit().setCreatedBy(username);
        feeEntry.getAudit().setUpdatedBy(username);

        if (request.getTiers() != null && !request.getTiers().isEmpty()) {
            for (ProductFeeTierRequest tierRequest : request.getTiers()) {
                ProductFeeTier tier = new ProductFeeTier(
                        feeEntry,
                        tierRequest.getTierFrom(),
                        tierRequest.getTierTo(),
                        tierRequest.getRate()
                );
                feeEntry.getTiers().add(tier);
            }
        }

        ProductFeeEntry saved = productFeeEntryRepository.save(feeEntry);
        return ProductFeeEntryResponse.fromEntity(saved);
    }

    @Transactional
    public ProductFeeEntryResponse updateFeeEntry(Long feeEntryId, ProductFeeEntryRequest request, String username) {
        ProductFeeEntry feeEntry = productFeeEntryRepository.findById(feeEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Fee entry not found: " + feeEntryId));

        validateVersionIsDraft(feeEntry.getProductVersion());

        validateCurrency(request.getCurrency());
        feeEntry.setFeeType(request.getFeeType());
        feeEntry.setCalculationMethod(request.getCalculationMethod());
        feeEntry.setAmount(request.getAmount());
        feeEntry.setRate(request.getRate());
        feeEntry.setCurrency(request.getCurrency());
        feeEntry.getAudit().setUpdatedBy(username);

        feeEntry.getTiers().clear();
        if (request.getTiers() != null && !request.getTiers().isEmpty()) {
            for (ProductFeeTierRequest tierRequest : request.getTiers()) {
                ProductFeeTier tier = new ProductFeeTier(
                        feeEntry,
                        tierRequest.getTierFrom(),
                        tierRequest.getTierTo(),
                        tierRequest.getRate()
                );
                feeEntry.getTiers().add(tier);
            }
        }

        ProductFeeEntry saved = productFeeEntryRepository.save(feeEntry);
        return ProductFeeEntryResponse.fromEntity(saved);
    }

    @Transactional
    public void removeFeeEntry(Long feeEntryId) {
        ProductFeeEntry feeEntry = productFeeEntryRepository.findById(feeEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Fee entry not found: " + feeEntryId));

        validateVersionIsDraft(feeEntry.getProductVersion());
        productFeeEntryRepository.deleteById(feeEntryId);
    }

    @Transactional(readOnly = true)
    public List<ProductFeeEntryResponse> getFeeEntries(Long versionId) {
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        return version.getFeeEntries().stream()
                .map(ProductFeeEntryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateVersionIsDraft(ProductVersion version) {
        if (version.getStatus() != ProductStatus.DRAFT) {
            throw new ProductVersionNotEditableException(version.getId());
        }
    }
}
