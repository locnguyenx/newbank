package com.banking.product.service;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductAuditLog;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductAuditLogResponse;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductAuditLogRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAuditServiceTest {

    @Mock
    private ProductAuditLogRepository productAuditLogRepository;

    @Mock
    private ProductVersionRepository productVersionRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductAuditService productAuditService;

    @BeforeEach
    void setUp() {
        productAuditService = new ProductAuditService(productAuditLogRepository, productVersionRepository, productMapper);
    }

    private ProductVersion createTestVersion(Long id, ProductStatus status) {
        Product product = new Product("BIZ-CURRENT", "Business Current", ProductFamily.ACCOUNT, "Test");
        ProductVersion version = new ProductVersion(product, 1, status);
        return version;
    }

    @Test
    void log_createsAuditLogEntry() {
        ProductVersion version = createTestVersion(1L, ProductStatus.DRAFT);
        ProductAuditLog savedLog = new ProductAuditLog(version, AuditAction.SUBMIT, "alice",
                ProductStatus.DRAFT, ProductStatus.PENDING_APPROVAL, null, "alice");

        when(productAuditLogRepository.save(any())).thenReturn(savedLog);

        productAuditService.log(1L, AuditAction.SUBMIT, "alice",
                ProductStatus.DRAFT, ProductStatus.PENDING_APPROVAL, null, "alice");

        ArgumentCaptor<ProductAuditLog> captor = ArgumentCaptor.forClass(ProductAuditLog.class);
        verify(productAuditLogRepository).save(captor.capture());

        ProductAuditLog captured = captor.getValue();
        assertEquals(AuditAction.SUBMIT, captured.getAction());
        assertEquals("alice", captured.getActor());
        assertEquals(ProductStatus.DRAFT, captured.getFromStatus());
        assertEquals(ProductStatus.PENDING_APPROVAL, captured.getToStatus());
    }

    @Test
    void log_withComment_savesComment() {
        ProductVersion version = createTestVersion(1L, ProductStatus.PENDING_APPROVAL);
        ProductAuditLog savedLog = new ProductAuditLog(version, AuditAction.REJECT, "bob",
                ProductStatus.PENDING_APPROVAL, ProductStatus.DRAFT, "Needs more details", null);

        when(productAuditLogRepository.save(any())).thenReturn(savedLog);

        productAuditService.log(1L, AuditAction.REJECT, "bob",
                ProductStatus.PENDING_APPROVAL, ProductStatus.DRAFT, "Needs more details", null);

        ArgumentCaptor<ProductAuditLog> captor = ArgumentCaptor.forClass(ProductAuditLog.class);
        verify(productAuditLogRepository).save(captor.capture());

        assertEquals("Needs more details", captor.getValue().getComment());
    }

    @Test
    void getAuditTrail_returnsLogsInReverseOrder() {
        ProductVersion version = createTestVersion(1L, ProductStatus.DRAFT);
        ProductAuditLog log1 = new ProductAuditLog(version, AuditAction.CREATE, "alice",
                null, ProductStatus.DRAFT, null, null);
        ProductAuditLog log2 = new ProductAuditLog(version, AuditAction.SUBMIT, "alice",
                ProductStatus.DRAFT, ProductStatus.PENDING_APPROVAL, null, "alice");
        ProductAuditLog log3 = new ProductAuditLog(version, AuditAction.APPROVE, "bob",
                ProductStatus.PENDING_APPROVAL, ProductStatus.APPROVED, null, "alice");

        ProductAuditLogResponse response1 = new ProductAuditLogResponse();
        response1.setId(1L);
        ProductAuditLogResponse response2 = new ProductAuditLogResponse();
        response2.setId(2L);
        ProductAuditLogResponse response3 = new ProductAuditLogResponse();
        response3.setId(3L);

        when(productAuditLogRepository.findByProductVersionIdOrderByTimestampDesc(1L))
                .thenReturn(Arrays.asList(log3, log2, log1));
        when(productMapper.toAuditLogResponse(log1)).thenReturn(response1);
        when(productMapper.toAuditLogResponse(log2)).thenReturn(response2);
        when(productMapper.toAuditLogResponse(log3)).thenReturn(response3);

        List<ProductAuditLogResponse> result = productAuditService.getAuditTrail(1L);

        assertEquals(3, result.size());
        assertEquals(3L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1L, result.get(2).getId());
    }

    @Test
    void getAuditTrail_emptyList_returnsEmpty() {
        when(productAuditLogRepository.findByProductVersionIdOrderByTimestampDesc(999L))
                .thenReturn(Arrays.asList());

        List<ProductAuditLogResponse> result = productAuditService.getAuditTrail(999L);

        assertTrue(result.isEmpty());
    }
}
