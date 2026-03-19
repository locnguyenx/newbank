package com.banking.customer.controller;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCStatus;
import com.banking.customer.domain.enums.KYCDocumentType;
import com.banking.customer.exception.InvalidKYCStateException;
import com.banking.customer.exception.KYCNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.service.KYCService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KYCController.class)
class KYCControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KYCService kycService;

    @MockBean
    private CustomerRepository customerRepository;

    private CorporateCustomer customer;
    private KYCCheck kycCheck;

    @BeforeEach
    void setUp() {
        customer = new CorporateCustomer("CUST-20240101-100000", "Test Corp", CustomerStatus.ACTIVE);
        customer.setId(1L);

        kycCheck = new KYCCheck(customer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(1L);
        kycCheck.setAssignedOfficer("OFFICER001");
        kycCheck.setDueDate(Instant.now().plus(30, ChronoUnit.DAYS));
    }

    @Test
    void shouldInitiateKYC() throws Exception {
        KYCController.InitiateKYCRequest request = new KYCController.InitiateKYCRequest();
        request.setCustomerId(1L);
        request.setLevel(KYCLevel.STANDARD);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(kycService.initiateKYC(any(), eq(KYCLevel.STANDARD))).thenReturn(kycCheck);

        mockMvc.perform(post("/api/kyc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.kycLevel").value("STANDARD"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn400WhenInitiateKYCWithMissingCustomerId() throws Exception {
        KYCController.InitiateKYCRequest request = new KYCController.InitiateKYCRequest();
        request.setCustomerId(null);
        request.setLevel(KYCLevel.STANDARD);

        mockMvc.perform(post("/api/kyc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldSubmitDocuments() throws Exception {
        KYCController.SubmitDocumentsRequest request = new KYCController.SubmitDocumentsRequest();
        request.setDocumentTypes(List.of(KYCDocumentType.PASSPORT, KYCDocumentType.PROOF_OF_ADDRESS));
        request.setDocumentReferences(List.of("DOC001", "DOC002"));

        KYCDocument document = new KYCDocument(kycCheck, KYCDocumentType.PASSPORT, "DOC001");
        document.setId(1L);

        when(kycService.submitDocuments(eq(1L), any(), any())).thenReturn(document);
        when(kycService.getKYCCheckById(1L)).thenReturn(kycCheck);

        mockMvc.perform(post("/api/kyc/1/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldSubmitForReview() throws Exception {
        KYCCheck pendingCheck = new KYCCheck(customer, KYCLevel.STANDARD, KYCStatus.PENDING_REVIEW);
        pendingCheck.setId(1L);

        when(kycService.submitForReview(1L)).thenReturn(pendingCheck);

        mockMvc.perform(post("/api/kyc/1/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING_REVIEW"));
    }

    @Test
    void shouldApproveKYC() throws Exception {
        KYCCheck approvedCheck = new KYCCheck(customer, KYCLevel.STANDARD, KYCStatus.APPROVED);
        approvedCheck.setId(1L);
        approvedCheck.setCompletedAt(Instant.now());

        KYCController.ApproveKYCRequest request = new KYCController.ApproveKYCRequest();
        request.setOfficerId("OFFICER001");

        when(kycService.approveKYC(1L, "OFFICER001")).thenReturn(approvedCheck);

        mockMvc.perform(post("/api/kyc/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldRejectKYC() throws Exception {
        KYCCheck rejectedCheck = new KYCCheck(customer, KYCLevel.STANDARD, KYCStatus.REJECTED);
        rejectedCheck.setId(1L);
        rejectedCheck.setCompletedAt(Instant.now());

        KYCController.RejectKYCRequest request = new KYCController.RejectKYCRequest();
        request.setOfficerId("OFFICER001");
        request.setReason("Invalid documents");

        when(kycService.rejectKYC(1L, "OFFICER001", "Invalid documents")).thenReturn(rejectedCheck);

        mockMvc.perform(post("/api/kyc/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void shouldReturn404WhenKYCCheckNotFound() throws Exception {
        when(kycService.getKYCCheckById(999L)).thenThrow(new KYCNotFoundException(999L));

        mockMvc.perform(get("/api/kyc/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("KYC-001"));
    }

    @Test
    void shouldUpdateKYC() throws Exception {
        KYCController.UpdateKYCRequest request = new KYCController.UpdateKYCRequest();
        request.setAssignedOfficer("OFFICER002");
        request.setDueDate(Instant.now().plus(45, ChronoUnit.DAYS));

        KYCCheck updatedCheck = new KYCCheck(customer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        updatedCheck.setId(1L);
        updatedCheck.setAssignedOfficer("OFFICER002");
        updatedCheck.setDueDate(Instant.now().plus(45, ChronoUnit.DAYS));

        when(kycService.updateKYC(1L, "OFFICER002", request.getDueDate())).thenReturn(updatedCheck);

        mockMvc.perform(put("/api/kyc/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedOfficer").value("OFFICER002"));
    }

    @Test
    void shouldReturn400WhenInvalidKYCState() throws Exception {
        when(kycService.submitForReview(1L)).thenThrow(new InvalidKYCStateException("Can only submit for review from IN_PROGRESS status"));

        mockMvc.perform(post("/api/kyc/1/submit"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("KYC-002"));
    }
}
