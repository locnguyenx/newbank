package com.banking.product.service;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductCustomerSegmentResponse;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.repository.ProductCustomerSegmentRepository;
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
class ProductSegmentServiceTest {

    @Mock
    private ProductCustomerSegmentRepository productCustomerSegmentRepository;

    @Mock
    private ProductVersionRepository productVersionRepository;

    private ProductSegmentService productSegmentService;

    private Product testProduct;
    private ProductVersion testVersion;

    @BeforeEach
    void setUp() {
        productSegmentService = new ProductSegmentService(productCustomerSegmentRepository, productVersionRepository);
        testProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        testVersion = new ProductVersion(testProduct, 1, ProductStatus.DRAFT);
    }

    @Test
    void assignSegments_replacesAllSegmentsForVersion() {
        List<CustomerType> newSegments = List.of(CustomerType.CORPORATE, CustomerType.SME);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productVersionRepository.save(any(ProductVersion.class))).thenReturn(testVersion);

        List<ProductCustomerSegmentResponse> result = productSegmentService.assignSegments(1L, newSegments, "alice");

        assertEquals(2, result.size());
        verify(productVersionRepository).save(testVersion);
    }

    @Test
    void assignSegments_nonDraftVersion_throwsNotEditableException() {
        testVersion.setStatus(ProductStatus.ACTIVE);
        List<CustomerType> newSegments = List.of(CustomerType.CORPORATE);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        assertThrows(
            ProductVersionNotEditableException.class,
            () -> productSegmentService.assignSegments(1L, newSegments, "alice")
        );

        verify(productCustomerSegmentRepository, never()).save(any());
    }

    @Test
    void getSegments_returnsAllSegmentsForVersion() {
        ProductCustomerSegment segment1 = new ProductCustomerSegment(testVersion, CustomerType.CORPORATE);
        ProductCustomerSegment segment2 = new ProductCustomerSegment(testVersion, CustomerType.SME);
        testVersion.setCustomerSegments(new ArrayList<>(List.of(segment1, segment2)));

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));

        List<ProductCustomerSegmentResponse> segments = productSegmentService.getSegments(1L);

        assertEquals(2, segments.size());
    }

    @Test
    void assignSegments_clearsExistingSegments() {
        ProductCustomerSegment existingSegment = new ProductCustomerSegment(testVersion, CustomerType.INDIVIDUAL);
        testVersion.setCustomerSegments(new ArrayList<>(List.of(existingSegment)));

        List<CustomerType> newSegments = List.of(CustomerType.CORPORATE);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(testVersion));
        when(productVersionRepository.save(any(ProductVersion.class))).thenReturn(testVersion);

        productSegmentService.assignSegments(1L, newSegments, "alice");

        assertEquals(1, testVersion.getCustomerSegments().size());
        assertEquals(CustomerType.CORPORATE, testVersion.getCustomerSegments().get(0).getCustomerType());
    }
}
