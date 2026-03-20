package com.banking.product.service;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductCustomerSegmentRepository;
import com.banking.product.repository.ProductRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVersionRepository productVersionRepository;

    @Mock
    private ProductCustomerSegmentRepository productCustomerSegmentRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductQueryService productQueryService;

    private Product testProduct;
    private ProductVersion testVersion;

    @BeforeEach
    void setUp() {
        productQueryService = new ProductQueryService(productRepository, productVersionRepository, productCustomerSegmentRepository, productMapper);
        testProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        testVersion = new ProductVersion(testProduct, 1, ProductStatus.ACTIVE);
    }

    @Test
    void getActiveProductByCode_returnsActiveVersion() {
        ProductVersionResponse mockResponse = new ProductVersionResponse();
        when(productVersionRepository.findByProductCodeAndStatus("BIZ-CURRENT", ProductStatus.ACTIVE))
                .thenReturn(List.of(testVersion));
        when(productMapper.toVersionResponse(testVersion)).thenReturn(mockResponse);

        Optional<ProductVersionResponse> result = productQueryService.getActiveProductByCode("BIZ-CURRENT");

        assertTrue(result.isPresent());
        assertEquals(mockResponse, result.get());
    }

    @Test
    void getActiveProductByCode_noActiveVersion_returnsEmpty() {
        when(productVersionRepository.findByProductCodeAndStatus("BIZ-CURRENT", ProductStatus.ACTIVE))
                .thenReturn(List.of());

        Optional<ProductVersionResponse> result = productQueryService.getActiveProductByCode("BIZ-CURRENT");

        assertTrue(result.isEmpty());
    }

    @Test
    void getProductVersionById_returnsAnyVersion() {
        testVersion.setStatus(ProductStatus.SUPERSEDED);
        ProductVersionResponse mockResponse = new ProductVersionResponse();
        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productMapper.toVersionResponse(testVersion)).thenReturn(mockResponse);

        Optional<ProductVersionResponse> result = productQueryService.getProductVersionById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void getProductVersionById_versionNotFound_returnsEmpty() {
        when(productVersionRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ProductVersionResponse> result = productQueryService.getProductVersionById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getActiveProductsByFamily_returnsAllActiveProductsForFamily() {
        Product product1 = new Product("BIZ-CURRENT", "Business Current", ProductFamily.ACCOUNT, "Test 1");
        Product product2 = new Product("SAVINGS", "Savings Account", ProductFamily.ACCOUNT, "Test 2");
        
        ProductVersion version1 = new ProductVersion(product1, 1, ProductStatus.ACTIVE);
        ProductVersion version2 = new ProductVersion(product2, 1, ProductStatus.ACTIVE);

        ProductVersionResponse mockResponse1 = new ProductVersionResponse();
        ProductVersionResponse mockResponse2 = new ProductVersionResponse();

        when(productVersionRepository.findByStatus(ProductStatus.ACTIVE))
                .thenReturn(List.of(version1, version2));
        when(productMapper.toVersionResponse(version1)).thenReturn(mockResponse1);
        when(productMapper.toVersionResponse(version2)).thenReturn(mockResponse2);

        List<ProductVersionResponse> result = productQueryService.getActiveProductsByFamily(ProductFamily.ACCOUNT);

        assertEquals(2, result.size());
    }

    @Test
    void getActiveProductsByCustomerType_returnsProductsForCustomerType() {
        Product product1 = new Product("BIZ-CURRENT", "Business Current", ProductFamily.ACCOUNT, "Test 1");
        ProductVersion version1 = new ProductVersion(product1, 1, ProductStatus.ACTIVE);
        
        ProductCustomerSegment segment = new ProductCustomerSegment(version1, CustomerType.CORPORATE);
        version1.setCustomerSegments(new ArrayList<>(List.of(segment)));

        ProductVersionResponse mockResponse = new ProductVersionResponse();

        when(productCustomerSegmentRepository.findByCustomerType(CustomerType.CORPORATE))
                .thenReturn(List.of(segment));
        when(productMapper.toVersionResponse(version1)).thenReturn(mockResponse);

        List<ProductVersionResponse> result = productQueryService.getActiveProductsByCustomerType(CustomerType.CORPORATE);

        assertEquals(1, result.size());
    }

    @Test
    void getActiveProductsByCustomerType_noMatchingProducts_returnsEmptyList() {
        when(productCustomerSegmentRepository.findByCustomerType(CustomerType.INDIVIDUAL))
                .thenReturn(List.of());

        List<ProductVersionResponse> result = productQueryService.getActiveProductsByCustomerType(CustomerType.INDIVIDUAL);

        assertTrue(result.isEmpty());
    }
}
