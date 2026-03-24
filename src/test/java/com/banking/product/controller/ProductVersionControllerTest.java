package com.banking.product.controller;

import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionDiffResponse;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.exception.MakerCheckerViolationException;
import com.banking.product.exception.ProductHasActiveContractsException;
import com.banking.product.service.ProductVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.common.security.config.TestSecurityConfig;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductVersionController.class)
@ContextConfiguration(classes = {ProductVersionController.class, ProductExceptionHandler.class})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ProductVersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductVersionService productVersionService;

    @Test
    void shouldListVersions() throws Exception {
        List<ProductVersionResponse> versions = List.of(createVersionResponse());

        when(productVersionService.getVersionHistory(1L)).thenReturn(versions);

        mockMvc.perform(get("/api/products/1/versions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].versionNumber").value(1))
                .andExpect(jsonPath("$[0].status").value("DRAFT"));
    }

    @Test
    void shouldGetVersionDetail() throws Exception {
        ProductVersionResponse response = createVersionResponse();

        when(productVersionService.getVersion(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1/versions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.versionNumber").value(1));
    }

    @Test
    void shouldSubmitForApproval() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("PENDING_APPROVAL");

        when(productVersionService.submitForApproval(1L, "testuser")).thenReturn(response);

        mockMvc.perform(post("/api/products/1/versions/1/submit")
                        .header("X-Username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING_APPROVAL"));
    }

    @Test
    void shouldReturn400WhenSubmittingNonDraftVersion() throws Exception {
        when(productVersionService.submitForApproval(1L, "testuser"))
                .thenThrow(new InvalidProductStatusException("Only DRAFT version can be submitted for approval"));

        mockMvc.perform(post("/api/products/1/versions/1/submit")
                        .header("X-Username", "testuser"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("PROD-004"));
    }

    @Test
    void shouldApproveVersion() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("APPROVED");

        when(productVersionService.approve(1L, "approver")).thenReturn(response);

        mockMvc.perform(post("/api/products/1/versions/1/approve")
                        .header("X-Username", "approver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldReturn403WhenApproverIsSameAsSubmitter() throws Exception {
        when(productVersionService.approve(1L, "submitter"))
                .thenThrow(new MakerCheckerViolationException("Approver cannot be the same as submitter"));

        mockMvc.perform(post("/api/products/1/versions/1/approve")
                        .header("X-Username", "submitter"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.messageCode").value("PROD-007"));
    }

    @Test
    void shouldRejectVersion() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("DRAFT");
        response.setRejectionComment("Needs more detail");

        when(productVersionService.reject(eq(1L), eq("rejector"), any())).thenReturn(response);

        mockMvc.perform(post("/api/products/1/versions/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "rejector")
                        .content("{\"comment\": \"Needs more detail\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.rejectionComment").value("Needs more detail"));
    }

    @Test
    void shouldReturn400WhenRejectingWithEmptyComment() throws Exception {
        mockMvc.perform(post("/api/products/1/versions/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "rejector")
                        .content("{\"comment\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldActivateVersion() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("ACTIVE");

        when(productVersionService.activate(1L, "activator")).thenReturn(response);

        mockMvc.perform(post("/api/products/1/versions/1/activate")
                        .header("X-Username", "activator"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400WhenActivatingNonApprovedVersion() throws Exception {
        when(productVersionService.activate(1L, "activator"))
                .thenThrow(new InvalidProductStatusException("Only APPROVED version can be activated"));

        mockMvc.perform(post("/api/products/1/versions/1/activate")
                        .header("X-Username", "activator"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("PROD-004"));
    }

    @Test
    void shouldRetireVersion() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("RETIRED");

        when(productVersionService.retire(1L, "retirer")).thenReturn(response);

        mockMvc.perform(post("/api/products/1/versions/1/retire")
                        .header("X-Username", "retirer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETIRED"));
    }

    @Test
    void shouldReturn409WhenRetiringVersionWithActiveContracts() throws Exception {
        when(productVersionService.retire(1L, "retirer"))
                .thenThrow(new ProductHasActiveContractsException("CUR-001"));

        mockMvc.perform(post("/api/products/1/versions/1/retire")
                        .header("X-Username", "retirer"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageCode").value("PROD-006"));
    }

    @Test
    void shouldCompareVersions() throws Exception {
        ProductVersionDiffResponse diffResponse = new ProductVersionDiffResponse();
        diffResponse.setVersionId1(1L);
        diffResponse.setVersionId2(2L);

        when(productVersionService.compareVersions(1L, 2L)).thenReturn(diffResponse);

        mockMvc.perform(get("/api/products/1/versions/compare")
                        .param("v1", "1")
                        .param("v2", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versionId1").value(1))
                .andExpect(jsonPath("$.versionId2").value(2));
    }

    private ProductVersionResponse createVersionResponse() {
        ProductVersionResponse response = new ProductVersionResponse();
        response.setId(1L);
        response.setVersionNumber(1);
        response.setStatus("DRAFT");
        return response;
    }
}