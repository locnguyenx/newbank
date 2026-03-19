package com.banking.customer.service;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCStatus;
import com.banking.customer.domain.enums.KYCDocumentType;
import com.banking.customer.exception.InvalidKYCStateException;
import com.banking.customer.exception.KYCNotFoundException;
import com.banking.customer.repository.KYCCheckRepository;
import com.banking.customer.repository.KYCDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KYCServiceTest {

    @Mock
    private KYCCheckRepository kycCheckRepository;

    @Mock
    private KYCDocumentRepository kycDocumentRepository;

    @Mock
    private KYCOfficerAssignmentService officerAssignmentService;

    @Mock
    private SanctionsScreeningService sanctionsScreeningService;

    @Mock
    private com.banking.customer.event.KYCEventPublisher eventPublisher;

    private KYCService kycService;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        kycService = new KYCService(kycCheckRepository, kycDocumentRepository, officerAssignmentService, 
            sanctionsScreeningService, eventPublisher);
        testCustomer = new com.banking.customer.domain.entity.CorporateCustomer(
            "CUST-001", "Test Corp", com.banking.customer.domain.enums.CustomerStatus.ACTIVE);
    }

    @Test
    void shouldInitiateKYCForNewCustomer() {
        when(kycCheckRepository.findByCustomerAndStatus(any(), eq(KYCStatus.APPROVED)))
            .thenReturn(List.of());
        when(officerAssignmentService.assignOfficer()).thenReturn("OFFICER-001");
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> {
            KYCCheck kyc = invocation.getArgument(0);
            kyc.setId(1L);
            return kyc;
        });

        KYCCheck result = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);

        assertNotNull(result);
        assertEquals(KYCStatus.IN_PROGRESS, result.getStatus());
        assertEquals(KYCLevel.STANDARD, result.getKycLevel());
        assertEquals("OFFICER-001", result.getAssignedOfficer());
        verify(eventPublisher).publishKYCInitiated(any());
    }

    @Test
    void shouldThrowExceptionWhenInitiatingKYCForCustomerWithApprovedKYC() {
        KYCCheck existingKYC = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.APPROVED);
        when(kycCheckRepository.findByCustomerAndStatus(any(), eq(KYCStatus.APPROVED)))
            .thenReturn(List.of(existingKYC));

        assertThrows(InvalidKYCStateException.class, () -> 
            kycService.initiateKYC(testCustomer, KYCLevel.STANDARD));
    }

    @Test
    void shouldSubmitDocuments() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(kycDocumentRepository.save(any())).thenAnswer(invocation -> {
            KYCDocument doc = invocation.getArgument(0);
            doc.setId(1L);
            return doc;
        });

        List<KYCDocumentType> documentTypes = Arrays.asList(KYCDocumentType.PASSPORT, KYCDocumentType.UTILITY_BILL);
        List<String> references = Arrays.asList("DOC-001", "DOC-002");

        KYCDocument result = kycService.submitDocuments(kycCheckId, documentTypes, references);

        assertNotNull(result);
        assertEquals(KYCDocumentType.UTILITY_BILL, result.getDocumentType());
        assertEquals("DOC-002", result.getDocumentReference());
        verify(eventPublisher).publishDocumentsSubmitted(any());
    }

    @Test
    void shouldThrowExceptionWhenSubmittingDocumentsToNonInProgressKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.APPROVED);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));

        assertThrows(InvalidKYCStateException.class, () ->
            kycService.submitDocuments(kycCheckId, Arrays.asList(KYCDocumentType.PASSPORT), 
                Arrays.asList("DOC-001")));
    }

    @Test
    void shouldThrowExceptionWhenKYCNotFound() {
        when(kycCheckRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(KYCNotFoundException.class, () ->
            kycService.submitDocuments(999L, Arrays.asList(KYCDocumentType.PASSPORT), 
                Arrays.asList("DOC-001")));
    }

    @Test
    void shouldSubmitForReview() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(kycCheckId);

        com.banking.customer.domain.entity.SanctionsScreeningResult screeningResult = 
            new com.banking.customer.domain.entity.SanctionsScreeningResult(
                kycCheck, Instant.now(), com.banking.customer.domain.enums.SanctionsScreeningResult.CLEAR);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(sanctionsScreeningService.screenCustomer(any())).thenReturn(screeningResult);
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        KYCCheck result = kycService.submitForReview(kycCheckId);

        assertEquals(KYCStatus.PENDING_REVIEW, result.getStatus());
        verify(eventPublisher).publishSubmittedForReview(any());
    }

    @Test
    void shouldRejectWhenSanctionsMatch() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(kycCheckId);

        com.banking.customer.domain.entity.SanctionsScreeningResult screeningResult = 
            new com.banking.customer.domain.entity.SanctionsScreeningResult(
                kycCheck, Instant.now(), com.banking.customer.domain.enums.SanctionsScreeningResult.MATCH);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(sanctionsScreeningService.screenCustomer(any())).thenReturn(screeningResult);
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        KYCCheck result = kycService.submitForReview(kycCheckId);

        assertEquals(KYCStatus.REJECTED, result.getStatus());
    }

    @Test
    void shouldApproveKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.PENDING_REVIEW);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        KYCCheck result = kycService.approveKYC(kycCheckId, "OFFICER-001");

        assertEquals(KYCStatus.APPROVED, result.getStatus());
        assertNotNull(result.getCompletedAt());
        assertNotNull(result.getNextReviewDate());
        verify(eventPublisher).publishKYCApproved(any(), eq("OFFICER-001"));
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonPendingKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));

        assertThrows(InvalidKYCStateException.class, () ->
            kycService.approveKYC(kycCheckId, "OFFICER-001"));
    }

    @Test
    void shouldRejectKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.PENDING_REVIEW);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        KYCCheck result = kycService.rejectKYC(kycCheckId, "OFFICER-001", "Invalid documents");

        assertEquals(KYCStatus.REJECTED, result.getStatus());
        assertNotNull(result.getCompletedAt());
        verify(eventPublisher).publishKYCRejected(any(), eq("OFFICER-001"), eq("Invalid documents"));
    }

    @Test
    void shouldThrowExceptionWhenRejectingAlreadyCompletedKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.APPROVED);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));

        assertThrows(InvalidKYCStateException.class, () ->
            kycService.rejectKYC(kycCheckId, "OFFICER-001", "Some reason"));
    }

    @Test
    void shouldScheduleReview() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.PENDING_REVIEW);
        kycCheck.setId(kycCheckId);
        Instant dueDate = Instant.now().plusSeconds(86400 * 30);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));
        when(kycCheckRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        KYCCheck result = kycService.scheduleReview(kycCheckId, dueDate);

        assertEquals(dueDate, result.getDueDate());
        verify(eventPublisher).publishReviewScheduled(any());
    }

    @Test
    void shouldThrowExceptionWhenSchedulingReviewForCompletedKYC() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.APPROVED);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));

        assertThrows(InvalidKYCStateException.class, () ->
            kycService.scheduleReview(kycCheckId, Instant.now()));
    }

    @Test
    void shouldThrowExceptionForMismatchedDocumentCounts() {
        Long kycCheckId = 1L;
        KYCCheck kycCheck = new KYCCheck(testCustomer, KYCLevel.STANDARD, KYCStatus.IN_PROGRESS);
        kycCheck.setId(kycCheckId);

        when(kycCheckRepository.findById(kycCheckId)).thenReturn(Optional.of(kycCheck));

        assertThrows(IllegalArgumentException.class, () ->
            kycService.submitDocuments(kycCheckId, Arrays.asList(KYCDocumentType.PASSPORT), 
                Arrays.asList("DOC-001", "DOC-002")));
    }
}
