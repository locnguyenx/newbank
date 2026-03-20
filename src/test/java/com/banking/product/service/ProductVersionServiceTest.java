package com.banking.product.service;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionDiffResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.exception.MakerCheckerViolationException;
import com.banking.product.exception.ProductHasActiveContractsException;
import com.banking.product.exception.ProductVersionNotEditableException;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductVersionServiceTest {

    @Mock
    private ProductVersionRepository productVersionRepository;

    @Mock
    private ProductAuditService productAuditService;

    @Mock
    private ProductMapper productMapper;

    private ProductVersionService productVersionService;

    private Product product;

    @BeforeEach
    void setUp() {
        productVersionService = new ProductVersionService(
                productVersionRepository, productAuditService, productMapper
        );
        product = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
    }

    private ProductVersion createVersion(Long id, Product product, Integer versionNumber, ProductStatus status) {
        ProductVersion version = new ProductVersion(product, versionNumber, status);
        return version;
    }

    private ProductVersionResponse createResponse(Long id, ProductStatus status) {
        ProductVersionResponse response = new ProductVersionResponse();
        response.setId(id);
        response.setStatus(status.name());
        return response;
    }

    @Test
    void submitForApproval_draftToPendingApproval() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.DRAFT);
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.PENDING_APPROVAL);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.submitForApproval(1L, "alice");

        assertEquals(ProductStatus.PENDING_APPROVAL.name(), result.getStatus());
        assertEquals("alice", version.getSubmittedBy());
        verify(productAuditService).log(eq(1L), eq(AuditAction.SUBMIT), eq("alice"),
                eq(ProductStatus.DRAFT), eq(ProductStatus.PENDING_APPROVAL), isNull(), isNull());
    }

    @Test
    void approve_pendingToApproved() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.PENDING_APPROVAL);
        version.setSubmittedBy("alice");
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.APPROVED);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.approve(1L, "bob");

        assertEquals(ProductStatus.APPROVED.name(), result.getStatus());
        assertEquals("bob", version.getApprovedBy());
        verify(productAuditService).log(eq(1L), eq(AuditAction.APPROVE), eq("bob"),
                eq(ProductStatus.PENDING_APPROVAL), eq(ProductStatus.APPROVED), isNull(), eq("alice"));
    }

    @Test
    void reject_pendingToDraft() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.PENDING_APPROVAL);
        version.setSubmittedBy("alice");
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.DRAFT);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.reject(1L, "bob", "Needs more details");

        assertEquals(ProductStatus.DRAFT.name(), result.getStatus());
        assertEquals("Needs more details", version.getRejectionComment());
        verify(productAuditService).log(eq(1L), eq(AuditAction.REJECT), eq("bob"),
                eq(ProductStatus.PENDING_APPROVAL), eq(ProductStatus.DRAFT), eq("Needs more details"), eq("alice"));
    }

    @Test
    void rejection_requiresComment() {
        assertThrows(IllegalArgumentException.class, () ->
                productVersionService.reject(1L, "bob", null));
    }

    @Test
    void activate_approvedToActive() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.APPROVED);
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.ACTIVE);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.activate(1L, "charlie");

        assertEquals(ProductStatus.ACTIVE.name(), result.getStatus());
        verify(productAuditService).log(eq(1L), eq(AuditAction.ACTIVATE), eq("charlie"),
                eq(ProductStatus.APPROVED), eq(ProductStatus.ACTIVE), isNull(), isNull());
    }

    @Test
    void activate_supersedesPreviousActive() {
        ProductVersion oldVersion = createVersion(1L, product, 1, ProductStatus.ACTIVE);
        ProductVersion newVersion = createVersion(2L, product, 2, ProductStatus.APPROVED);
        ProductVersionResponse expectedResponse = createResponse(2L, ProductStatus.ACTIVE);

        when(productVersionRepository.findById(2L)).thenReturn(Optional.of(newVersion));
        when(productVersionRepository.findByProductIdAndStatus(product.getId(), ProductStatus.ACTIVE))
                .thenReturn(Optional.of(oldVersion));
        when(productVersionRepository.save(any())).thenReturn(newVersion);
        when(productMapper.toVersionResponse(newVersion)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.activate(2L, "charlie");

        assertEquals(ProductStatus.ACTIVE.name(), result.getStatus());
        assertEquals(ProductStatus.SUPERSEDED, oldVersion.getStatus());
    }

    @Test
    void activate_nonApproved_throwsException() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.DRAFT);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));

        assertThrows(InvalidProductStatusException.class, () ->
                productVersionService.activate(1L, "charlie"));
    }

    @Test
    void retire_activeToRetired() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.ACTIVE);
        version.setContractCount(0);
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.RETIRED);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.retire(1L, "admin");

        assertEquals(ProductStatus.RETIRED.name(), result.getStatus());
        verify(productAuditService).log(eq(1L), eq(AuditAction.RETIRE), eq("admin"),
                eq(ProductStatus.ACTIVE), eq(ProductStatus.RETIRED), isNull(), isNull());
    }

    @Test
    void retire_withContracts_throwsException() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.ACTIVE);
        version.setContractCount(5);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));

        assertThrows(ProductHasActiveContractsException.class, () ->
                productVersionService.retire(1L, "admin"));
    }

    @Test
    void approve_sameUser_throwsException() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.PENDING_APPROVAL);
        version.setSubmittedBy("alice");

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));

        MakerCheckerViolationException exception = assertThrows(MakerCheckerViolationException.class, () ->
                productVersionService.approve(1L, "alice"));

        assertEquals(MakerCheckerViolationException.ERROR_CODE, exception.getErrorCode());
    }

    @Test
    void approve_differentUser_succeeds() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.PENDING_APPROVAL);
        version.setSubmittedBy("alice");
        ProductVersionResponse expectedResponse = createResponse(1L, ProductStatus.APPROVED);

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(productVersionRepository.save(any())).thenReturn(version);
        when(productMapper.toVersionResponse(version)).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.approve(1L, "bob");

        assertEquals(ProductStatus.APPROVED.name(), result.getStatus());
    }

    @Test
    void editActive_createsNewDraftVersion() {
        ProductVersion activeVersion = createVersion(1L, product, 1, ProductStatus.ACTIVE);
        activeVersion.setFeatures(new ArrayList<>());
        activeVersion.setFeeEntries(new ArrayList<>());
        activeVersion.setCustomerSegments(new ArrayList<>());
        ProductVersion newVersion = createVersion(null, product, 2, ProductStatus.DRAFT);
        ProductVersionResponse expectedResponse = createResponse(2L, ProductStatus.DRAFT);

        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(product.getId()))
                .thenReturn(Optional.of(activeVersion));
        when(productVersionRepository.save(any())).thenAnswer(invocation -> {
            ProductVersion v = invocation.getArgument(0);
            return v;
        });
        when(productMapper.toVersionResponse(any())).thenReturn(expectedResponse);

        ProductVersionResponse result = productVersionService.createNewVersionFromActive(product.getId());

        assertEquals(ProductStatus.DRAFT.name(), result.getStatus());
        verify(productAuditService).log(any(), eq(AuditAction.CREATE), eq("system"),
                isNull(), eq(ProductStatus.DRAFT), isNull(), isNull());
    }

    @Test
    void cannotEdit_pendingApprovalVersion() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.PENDING_APPROVAL);

        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(product.getId()))
                .thenReturn(Optional.of(version));

        assertThrows(ProductVersionNotEditableException.class, () ->
                productVersionService.createNewVersionFromActive(product.getId()));
    }

    @Test
    void cannotEdit_approvedVersion() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.APPROVED);

        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(product.getId()))
                .thenReturn(Optional.of(version));

        assertThrows(ProductVersionNotEditableException.class, () ->
                productVersionService.createNewVersionFromActive(product.getId()));
    }

    @Test
    void cannotEdit_retiredVersion() {
        ProductVersion version = createVersion(1L, product, 1, ProductStatus.RETIRED);

        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(product.getId()))
                .thenReturn(Optional.of(version));

        assertThrows(ProductVersionNotEditableException.class, () ->
                productVersionService.createNewVersionFromActive(product.getId()));
    }

    @Test
    void getVersionHistory_returnsChronological() {
        ProductVersion version1 = createVersion(1L, product, 1, ProductStatus.SUPERSEDED);
        ProductVersion version2 = createVersion(2L, product, 2, ProductStatus.ACTIVE);
        ProductVersion version3 = createVersion(3L, product, 3, ProductStatus.DRAFT);

        ProductVersionResponse response1 = createResponse(1L, ProductStatus.SUPERSEDED);
        ProductVersionResponse response2 = createResponse(2L, ProductStatus.ACTIVE);
        ProductVersionResponse response3 = createResponse(3L, ProductStatus.DRAFT);

        when(productVersionRepository.findByProductIdOrderByVersionNumberDesc(product.getId()))
                .thenReturn(Arrays.asList(version3, version2, version1));
        when(productMapper.toVersionResponse(version1)).thenReturn(response1);
        when(productMapper.toVersionResponse(version2)).thenReturn(response2);
        when(productMapper.toVersionResponse(version3)).thenReturn(response3);

        List<ProductVersionResponse> result = productVersionService.getVersionHistory(product.getId());

        assertEquals(3, result.size());
        assertEquals(3L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1L, result.get(2).getId());
    }

    @Test
    void compareVersions_returnsDiffs() {
        ProductVersion version1 = createVersion(1L, product, 1, ProductStatus.SUPERSEDED);
        ProductVersion version2 = createVersion(2L, product, 2, ProductStatus.DRAFT);

        ProductVersionDiffResponse diffResponse = new ProductVersionDiffResponse();
        diffResponse.setVersionId1(1L);
        diffResponse.setVersionId2(2L);
        diffResponse.setGeneralFields(new ArrayList<>());
        diffResponse.setFeatures(new ArrayList<>());
        diffResponse.setFees(new ArrayList<>());
        diffResponse.setSegments(new ArrayList<>());

        when(productVersionRepository.findById(1L)).thenReturn(Optional.of(version1));
        when(productVersionRepository.findById(2L)).thenReturn(Optional.of(version2));

        ProductVersionDiffResponse result = productVersionService.compareVersions(1L, 2L);

        assertNotNull(result);
        assertEquals(1L, result.getVersionId1());
        assertEquals(2L, result.getVersionId2());
    }
}
