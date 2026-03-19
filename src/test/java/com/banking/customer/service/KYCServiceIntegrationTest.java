package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCStatus;
import com.banking.customer.domain.enums.KYCDocumentType;
import com.banking.customer.exception.InvalidKYCStateException;
import com.banking.customer.exception.KYCNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.repository.KYCCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class KYCServiceIntegrationTest {

    @Autowired
    private KYCService kycService;

    @Autowired
    private KYCCheckRepository kycCheckRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private CorporateCustomer testCustomer;

    @BeforeEach
    void setUp() {
        kycCheckRepository.deleteAll();
        customerRepository.deleteAll();

        testCustomer = new CorporateCustomer("CUST-KYC-001", "Test Corp", CustomerStatus.ACTIVE);
        testCustomer.setTaxId("TAX-001");
        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    void shouldInitiateKYC() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);

        assertNotNull(kyc);
        assertNotNull(kyc.getId());
        assertEquals(KYCStatus.IN_PROGRESS, kyc.getStatus());
        assertEquals(KYCLevel.STANDARD, kyc.getKycLevel());
        assertNotNull(kyc.getAssignedOfficer());
        assertNotNull(kyc.getDueDate());
    }

    @Test
    void shouldSubmitDocuments() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);

        List<KYCDocumentType> docTypes = Arrays.asList(KYCDocumentType.PASSPORT, KYCDocumentType.UTILITY_BILL);
        List<String> refs = Arrays.asList("DOC-001", "DOC-002");

        KYCDocument doc = kycService.submitDocuments(kyc.getId(), docTypes, refs);

        assertNotNull(doc);
        assertEquals(KYCDocumentType.UTILITY_BILL, doc.getDocumentType());
        assertEquals("DOC-002", doc.getDocumentReference());
    }

    @Test
    void shouldSubmitForReview() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);

        KYCCheck reviewed = kycService.submitForReview(kyc.getId());

        assertEquals(KYCStatus.PENDING_REVIEW, reviewed.getStatus());
    }

    @Test
    void shouldApproveKYC() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);
        kycService.submitDocuments(kyc.getId(), 
            Arrays.asList(KYCDocumentType.PASSPORT), 
            Arrays.asList("DOC-001"));
        kycService.submitForReview(kyc.getId());

        KYCCheck approved = kycService.approveKYC(kyc.getId(), "OFFICER-001");

        assertEquals(KYCStatus.APPROVED, approved.getStatus());
        assertNotNull(approved.getCompletedAt());
        assertNotNull(approved.getNextReviewDate());
    }

    @Test
    void shouldRejectKYC() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);
        kycService.submitDocuments(kyc.getId(), 
            Arrays.asList(KYCDocumentType.PASSPORT), 
            Arrays.asList("DOC-001"));
        kycService.submitForReview(kyc.getId());

        KYCCheck rejected = kycService.rejectKYC(kyc.getId(), "OFFICER-001", "Invalid documents");

        assertEquals(KYCStatus.REJECTED, rejected.getStatus());
        assertNotNull(rejected.getCompletedAt());
    }

    @Test
    void shouldScheduleReview() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);
        Instant newDueDate = Instant.now().plusSeconds(86400 * 45);

        KYCCheck scheduled = kycService.scheduleReview(kyc.getId(), newDueDate);

        assertEquals(newDueDate, scheduled.getDueDate());
    }

    @Test
    void shouldThrowExceptionForDuplicateKYCApproval() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);
        kycService.submitDocuments(kyc.getId(), 
            Arrays.asList(KYCDocumentType.PASSPORT), 
            Arrays.asList("DOC-001"));
        kycService.submitForReview(kyc.getId());
        kycService.approveKYC(kyc.getId(), "OFFICER-001");

        assertThrows(InvalidKYCStateException.class, () ->
            kycService.approveKYC(kyc.getId(), "OFFICER-001"));
    }

    @Test
    void shouldThrowExceptionForNonExistentKYC() {
        assertThrows(KYCNotFoundException.class, () ->
            kycService.getKYCCheckById(99999L));
    }

    @Test
    void shouldEnforceStandardKYCLevelDueDate() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.STANDARD);

        Instant expectedDueDate = Instant.now().plusSeconds(86400 * 30);
        long diff = Math.abs(kyc.getDueDate().toEpochMilli() - expectedDueDate.toEpochMilli());
        assertTrue(diff < 60000);
    }

    @Test
    void shouldEnforceEnhancedKYCLevelDueDate() {
        KYCCheck kyc = kycService.initiateKYC(testCustomer, KYCLevel.ENHANCED);

        Instant expectedDueDate = Instant.now().plusSeconds(86400 * 60);
        long diff = Math.abs(kyc.getDueDate().toEpochMilli() - expectedDueDate.toEpochMilli());
        assertTrue(diff < 60000);
    }
}
