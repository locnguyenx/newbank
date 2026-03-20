package com.banking.product.service;

import com.banking.product.domain.entity.Product;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductFeatureServiceTest {

    @Mock
    private ProductFeatureRepository productFeatureRepository;

    @Mock
    private ProductVersionRepository productVersionRepository;

    private ProductFeatureService productFeatureService;

    private Product testProduct;
    private ProductVersion testVersion;

    @BeforeEach
    void setUp() {
        productFeatureService = new ProductFeatureService(productFeatureRepository, productVersionRepository);
        testProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        testVersion = new ProductVersion(testProduct, 1, ProductStatus.DRAFT);
    }

    @Test
    void addFeature_validPrefix_addsFeatureToDraftVersion() {
        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("account.maxTransactions");
        request.setFeatureValue("100");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productFeatureRepository.save(any(ProductFeature.class))).thenAnswer(inv -> {
            ProductFeature f = inv.getArgument(0);
            return f;
        });

        ProductFeatureResponse response = productFeatureService.addFeature(1L, request, "alice");

        assertEquals("account.maxTransactions", response.getFeatureKey());
        assertEquals("100", response.getFeatureValue());
        verify(productFeatureRepository).save(any(ProductFeature.class));
    }

    @Test
    void updateFeature_updatesFeatureValue() {
        ProductFeature existingFeature = new ProductFeature(testVersion, "account.maxTransactions", "100");
        existingFeature.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("account.maxTransactions");
        request.setFeatureValue("200");

        when(productFeatureRepository.findById(1L)).thenReturn(Optional.of(existingFeature));
        when(productFeatureRepository.save(any(ProductFeature.class))).thenReturn(existingFeature);

        ProductFeatureResponse response = productFeatureService.updateFeature(1L, request, "alice");

        assertEquals("200", response.getFeatureValue());
        verify(productFeatureRepository).save(existingFeature);
    }

    @Test
    void addFeature_invalidPrefix_throwsInvalidFeaturePrefixException() {
        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("payment.invalidKey");
        request.setFeatureValue("value");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        InvalidFeaturePrefixException exception = assertThrows(
            InvalidFeaturePrefixException.class,
            () -> productFeatureService.addFeature(1L, request, "alice")
        );

        assertEquals(InvalidFeaturePrefixException.ERROR_CODE, exception.getErrorCode());
        verify(productFeatureRepository, never()).save(any());
    }

    @Test
    void addFeature_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.ACTIVE);
        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("account.maxTransactions");
        request.setFeatureValue("100");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productFeatureService.addFeature(1L, request, "alice")
        );

        verify(productFeatureRepository, never()).save(any());
    }

    @Test
    void updateFeature_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.APPROVED);
        ProductFeature existingFeature = new ProductFeature(testVersion, "account.maxTransactions", "100");
        existingFeature.setAudit(new com.banking.product.domain.embeddable.AuditFields());

        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("account.maxTransactions");
        request.setFeatureValue("200");

        when(productFeatureRepository.findById(1L)).thenReturn(Optional.of(existingFeature));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productFeatureService.updateFeature(1L, request, "alice")
        );

        verify(productFeatureRepository, never()).save(any());
    }

    @Test
    void removeFeature_deletesFeature() {
        when(productFeatureRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productFeatureRepository).deleteById(1L);

        productFeatureService.removeFeature(1L);

        verify(productFeatureRepository).deleteById(1L);
    }

    @Test
    void getFeatures_returnsAllFeaturesForVersion() {
        ProductFeature feature1 = new ProductFeature(testVersion, "account.maxTransactions", "100");
        ProductFeature feature2 = new ProductFeature(testVersion, "account.minBalance", "500");
        testVersion.setFeatures(new ArrayList<>(List.of(feature1, feature2)));

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        List<ProductFeatureResponse> features = productFeatureService.getFeatures(1L);

        assertEquals(2, features.size());
    }

    @Test
    void addFeature_tradeFinanceFamily_acceptsTradeFinancePrefix() {
        Product tradeFinanceProduct = new Product("TF-LETTER", "Letter of Credit", ProductFamily.TRADE_FINANCE, "Trade finance product");
        ProductVersion tradeFinanceVersion = new ProductVersion(tradeFinanceProduct, 1, ProductStatus.DRAFT);

        ProductFeatureRequest request = new ProductFeatureRequest();
        request.setFeatureKey("trade_finance.maxAmount");
        request.setFeatureValue("1000000");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(tradeFinanceVersion));
        when(productFeatureRepository.save(any(ProductFeature.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductFeatureResponse response = productFeatureService.addFeature(1L, request, "alice");

        assertEquals("trade_finance.maxAmount", response.getFeatureKey());
        verify(productFeatureRepository).save(any(ProductFeature.class));
    }
}
