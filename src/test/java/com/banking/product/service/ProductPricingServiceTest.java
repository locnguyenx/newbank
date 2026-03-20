package com.banking.product.service;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductFeeEntry;
import com.banking.product.domain.entity.ProductFeeTier;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.FeeCalculationMethod;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.ProductFeeEntryRequest;
import com.banking.product.dto.request.ProductFeeTierRequest;
import com.banking.product.dto.response.ProductFeeEntryResponse;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.repository.ProductFeeEntryRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPricingServiceTest {

    @Mock
    private ProductFeeEntryRepository productFeeEntryRepository;

    @Mock
    private ProductVersionRepository productVersionRepository;

    private ProductPricingService productPricingService;

    private Product testProduct;
    private ProductVersion testVersion;

    @BeforeEach
    void setUp() {
        productPricingService = new ProductPricingService(productFeeEntryRepository, productVersionRepository);
        testProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        testVersion = new ProductVersion(testProduct, 1, ProductStatus.DRAFT);
    }

    @Test
    void addFeeEntry_flatFee_createsFlatFeeEntry() {
        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("MONTHLY_FEE");
        request.setCalculationMethod(FeeCalculationMethod.FLAT);
        request.setAmount(new BigDecimal("25.00"));
        request.setCurrency("USD");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productFeeEntryRepository.save(any(ProductFeeEntry.class))).thenAnswer(inv -> {
            ProductFeeEntry entry = inv.getArgument(0);
            return entry;
        });

        ProductFeeEntryResponse response = productPricingService.addFeeEntry(1L, request, "alice");

        assertEquals("MONTHLY_FEE", response.getFeeType());
        assertEquals("FLAT", response.getCalculationMethod());
        assertEquals(new BigDecimal("25.00"), response.getAmount());
        assertEquals("USD", response.getCurrency());
        verify(productFeeEntryRepository).save(any(ProductFeeEntry.class));
    }

    @Test
    void addFeeEntry_percentageFee_createsPercentageFeeEntry() {
        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("TRANSACTION_FEE");
        request.setCalculationMethod(FeeCalculationMethod.PERCENTAGE);
        request.setRate(new BigDecimal("0.0015"));
        request.setCurrency("USD");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productFeeEntryRepository.save(any(ProductFeeEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductFeeEntryResponse response = productPricingService.addFeeEntry(1L, request, "alice");

        assertEquals("TRANSACTION_FEE", response.getFeeType());
        assertEquals("PERCENTAGE", response.getCalculationMethod());
        assertEquals(new BigDecimal("0.0015"), response.getRate());
        verify(productFeeEntryRepository).save(any(ProductFeeEntry.class));
    }

    @Test
    void addFeeEntry_tieredVolumePricing_createsTieredFeeEntry() {
        ProductFeeTierRequest tier1 = new ProductFeeTierRequest();
        tier1.setTierFrom(0L);
        tier1.setTierTo(10000L);
        tier1.setRate(new BigDecimal("0.01"));

        ProductFeeTierRequest tier2 = new ProductFeeTierRequest();
        tier2.setTierFrom(10001L);
        tier2.setTierTo(null);
        tier2.setRate(new BigDecimal("0.005"));

        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("VOLUME_FEE");
        request.setCalculationMethod(FeeCalculationMethod.TIERED_VOLUME);
        request.setCurrency("USD");
        request.setTiers(List.of(tier1, tier2));

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productFeeEntryRepository.save(any(ProductFeeEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductFeeEntryResponse response = productPricingService.addFeeEntry(1L, request, "alice");

        assertEquals("VOLUME_FEE", response.getFeeType());
        assertEquals("TIERED_VOLUME", response.getCalculationMethod());
        assertNotNull(response.getTiers());
        assertEquals(2, response.getTiers().size());
        verify(productFeeEntryRepository).save(any(ProductFeeEntry.class));
    }

    @Test
    void addFeeEntry_multipleFeeEntries_createsAllEntries() {
        ProductFeeEntryRequest request1 = new ProductFeeEntryRequest();
        request1.setFeeType("MONTHLY_FEE");
        request1.setCalculationMethod(FeeCalculationMethod.FLAT);
        request1.setAmount(new BigDecimal("25.00"));
        request1.setCurrency("USD");

        ProductFeeEntryRequest request2 = new ProductFeeEntryRequest();
        request2.setFeeType("TRANSACTION_FEE");
        request2.setCalculationMethod(FeeCalculationMethod.PERCENTAGE);
        request2.setRate(new BigDecimal("0.0015"));
        request2.setCurrency("USD");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productFeeEntryRepository.save(any(ProductFeeEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductFeeEntryResponse response1 = productPricingService.addFeeEntry(1L, request1, "alice");
        ProductFeeEntryResponse response2 = productPricingService.addFeeEntry(1L, request2, "alice");

        assertEquals("MONTHLY_FEE", response1.getFeeType());
        assertEquals("TRANSACTION_FEE", response2.getFeeType());
        verify(productFeeEntryRepository, times(2)).save(any(ProductFeeEntry.class));
    }

    @Test
    void addFeeEntry_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.ACTIVE);
        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("MONTHLY_FEE");
        request.setCalculationMethod(FeeCalculationMethod.FLAT);
        request.setAmount(new BigDecimal("25.00"));
        request.setCurrency("USD");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productPricingService.addFeeEntry(1L, request, "alice")
        );

        verify(productFeeEntryRepository, never()).save(any());
    }

    @Test
    void updateFeeEntry_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.APPROVED);
        ProductFeeEntry existingEntry = new ProductFeeEntry(testVersion, "MONTHLY_FEE", 
            FeeCalculationMethod.FLAT, new BigDecimal("25.00"), null, "USD");
        existingEntry.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("MONTHLY_FEE");
        request.setCalculationMethod(FeeCalculationMethod.FLAT);
        request.setAmount(new BigDecimal("30.00"));
        request.setCurrency("USD");

        when(productFeeEntryRepository.findById(1L)).thenReturn(Optional.of(existingEntry));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productPricingService.updateFeeEntry(1L, request, "alice")
        );

        verify(productFeeEntryRepository, never()).save(any());
    }

    @Test
    void removeFeeEntry_deletesFeeEntry() {
        ProductFeeEntry existingEntry = new ProductFeeEntry(testVersion, "MONTHLY_FEE", 
            FeeCalculationMethod.FLAT, new BigDecimal("25.00"), null, "USD");
        existingEntry.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        when(productFeeEntryRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        doNothing().when(productFeeEntryRepository).deleteById(1L);

        productPricingService.removeFeeEntry(1L);

        verify(productFeeEntryRepository).deleteById(1L);
    }

    @Test
    void removeFeeEntry_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.ACTIVE);
        ProductFeeEntry existingEntry = new ProductFeeEntry(testVersion, "MONTHLY_FEE", 
            FeeCalculationMethod.FLAT, new BigDecimal("25.00"), null, "USD");
        existingEntry.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        when(productFeeEntryRepository.findById(1L)).thenReturn(Optional.of(existingEntry));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productPricingService.removeFeeEntry(1L)
        );

        verify(productFeeEntryRepository, never()).deleteById(any());
    }

    @Test
    void getFeeEntries_returnsAllFeeEntriesWithTiers() {
        ProductFeeEntry entry1 = new ProductFeeEntry(testVersion, "MONTHLY_FEE", 
            FeeCalculationMethod.FLAT, new BigDecimal("25.00"), null, "USD");
        entry1.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        ProductFeeTier tier = new ProductFeeTier(entry1, 0L, 10000L, new BigDecimal("0.01"));
        entry1.getTiers().add(tier);

        testVersion.setFeeEntries(new ArrayList<>(List.of(entry1)));

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        List<ProductFeeEntryResponse> entries = productPricingService.getFeeEntries(1L);

        assertEquals(1, entries.size());
        assertEquals("MONTHLY_FEE", entries.get(0).getFeeType());
        assertEquals(1, entries.get(0).getTiers().size());
    }

    @Test
    void updateFeeEntry_updatesFeeEntry() {
        ProductFeeEntry existingEntry = new ProductFeeEntry(testVersion, "MONTHLY_FEE", 
            FeeCalculationMethod.FLAT, new BigDecimal("25.00"), null, "USD");
        existingEntry.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        ProductFeeEntryRequest request = new ProductFeeEntryRequest();
        request.setFeeType("MONTHLY_FEE");
        request.setCalculationMethod(FeeCalculationMethod.FLAT);
        request.setAmount(new BigDecimal("30.00"));
        request.setCurrency("USD");

        when(productFeeEntryRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        when(productFeeEntryRepository.save(any(ProductFeeEntry.class))).thenReturn(existingEntry);

        ProductFeeEntryResponse response = productPricingService.updateFeeEntry(1L, request, "alice");

        assertEquals(new BigDecimal("30.00"), response.getAmount());
        verify(productFeeEntryRepository).save(existingEntry);
    }
}
