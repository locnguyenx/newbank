package com.banking.customer.service;

import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.entity.SanctionsScreeningResult;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCStatus;
import com.banking.customer.domain.enums.KYCDocumentType;
import com.banking.customer.exception.InvalidKYCStateException;
import com.banking.customer.exception.KYCNotFoundException;
import com.banking.customer.repository.KYCCheckRepository;
import com.banking.customer.repository.KYCDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class KYCService {

    private final KYCCheckRepository kycCheckRepository;
    private final KYCDocumentRepository kycDocumentRepository;
    private final KYCOfficerAssignmentService officerAssignmentService;
    private final SanctionsScreeningService sanctionsScreeningService;
    private final com.banking.customer.event.KYCEventPublisher eventPublisher;

    public KYCService(KYCCheckRepository kycCheckRepository,
                      KYCDocumentRepository kycDocumentRepository,
                      KYCOfficerAssignmentService officerAssignmentService,
                      SanctionsScreeningService sanctionsScreeningService,
                      com.banking.customer.event.KYCEventPublisher eventPublisher) {
        this.kycCheckRepository = kycCheckRepository;
        this.kycDocumentRepository = kycDocumentRepository;
        this.officerAssignmentService = officerAssignmentService;
        this.sanctionsScreeningService = sanctionsScreeningService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public KYCCheck initiateKYC(com.banking.customer.domain.entity.Customer customer, KYCLevel level) {
        List<KYCCheck> existingChecks = kycCheckRepository.findByCustomerAndStatus(customer, KYCStatus.APPROVED);
        if (!existingChecks.isEmpty()) {
            throw new InvalidKYCStateException("Customer already has an approved KYC check");
        }

        KYCCheck kycCheck = new KYCCheck(customer, level, KYCStatus.IN_PROGRESS);
        Instant dueDate = calculateDueDate(level);
        kycCheck.setDueDate(dueDate);

        String assignedOfficer = officerAssignmentService.assignOfficer();
        kycCheck.setAssignedOfficer(assignedOfficer);

        KYCCheck savedCheck = kycCheckRepository.save(kycCheck);

        eventPublisher.publishKYCInitiated(savedCheck);

        return savedCheck;
    }

    @Transactional
    public KYCDocument submitDocuments(Long kycCheckId, List<KYCDocumentType> documentTypes, List<String> documentReferences) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() != KYCStatus.IN_PROGRESS) {
            throw new InvalidKYCStateException("Can only submit documents for KYC checks in IN_PROGRESS status");
        }

        if (documentTypes.size() != documentReferences.size()) {
            throw new IllegalArgumentException("Document types and references must have the same size");
        }

        KYCDocument savedDocument = null;
        for (int i = 0; i < documentTypes.size(); i++) {
            KYCDocument document = new KYCDocument(kycCheck, documentTypes.get(i), documentReferences.get(i));
            savedDocument = kycDocumentRepository.save(document);
        }

        eventPublisher.publishDocumentsSubmitted(kycCheck);

        return savedDocument;
    }

    @Transactional
    public KYCCheck submitForReview(Long kycCheckId) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() != KYCStatus.IN_PROGRESS) {
            throw new InvalidKYCStateException("Can only submit for review from IN_PROGRESS status");
        }

        kycCheck.setStatus(KYCStatus.PENDING_REVIEW);

        SanctionsScreeningResult screeningResult = sanctionsScreeningService.screenCustomer(kycCheck);
        if (screeningResult.getScreeningResult() == com.banking.customer.domain.enums.SanctionsScreeningResult.MATCH) {
            kycCheck.setStatus(KYCStatus.REJECTED);
        }

        KYCCheck savedCheck = kycCheckRepository.save(kycCheck);

        eventPublisher.publishSubmittedForReview(savedCheck);

        return savedCheck;
    }

    @Transactional
    public KYCCheck approveKYC(Long kycCheckId, String officerId) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() != KYCStatus.PENDING_REVIEW) {
            throw new InvalidKYCStateException("Can only approve KYC checks in PENDING_REVIEW status");
        }

        kycCheck.setStatus(KYCStatus.APPROVED);
        kycCheck.setCompletedAt(Instant.now());

        Instant nextReviewDate = calculateNextReviewDate(kycCheck.getKycLevel());
        kycCheck.setNextReviewDate(nextReviewDate);

        KYCCheck savedCheck = kycCheckRepository.save(kycCheck);

        eventPublisher.publishKYCApproved(savedCheck, officerId);

        return savedCheck;
    }

    @Transactional
    public KYCCheck rejectKYC(Long kycCheckId, String officerId, String reason) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() == KYCStatus.APPROVED || kycCheck.getStatus() == KYCStatus.REJECTED) {
            throw new InvalidKYCStateException("Cannot reject an already completed KYC check");
        }

        kycCheck.setStatus(KYCStatus.REJECTED);
        kycCheck.setCompletedAt(Instant.now());

        KYCCheck savedCheck = kycCheckRepository.save(kycCheck);

        eventPublisher.publishKYCRejected(savedCheck, officerId, reason);

        return savedCheck;
    }

    @Transactional
    public KYCCheck scheduleReview(Long kycCheckId, Instant dueDate) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() != KYCStatus.PENDING_REVIEW && kycCheck.getStatus() != KYCStatus.IN_PROGRESS) {
            throw new InvalidKYCStateException("Cannot schedule review for KYC check in current status");
        }

        kycCheck.setDueDate(dueDate);
        KYCCheck savedCheck = kycCheckRepository.save(kycCheck);

        eventPublisher.publishReviewScheduled(savedCheck);

        return savedCheck;
    }

    @Transactional
    public KYCCheck updateKYC(Long kycCheckId, String assignedOfficer, Instant dueDate) {
        KYCCheck kycCheck = kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));

        if (kycCheck.getStatus() == KYCStatus.APPROVED || kycCheck.getStatus() == KYCStatus.REJECTED) {
            throw new InvalidKYCStateException("Cannot update an already completed KYC check");
        }

        if (assignedOfficer != null) {
            kycCheck.setAssignedOfficer(assignedOfficer);
        }
        if (dueDate != null) {
            kycCheck.setDueDate(dueDate);
        }

        return kycCheckRepository.save(kycCheck);
    }

    @Transactional(readOnly = true)
    public KYCCheck getKYCCheckById(Long kycCheckId) {
        return kycCheckRepository.findById(kycCheckId)
            .orElseThrow(() -> new KYCNotFoundException(kycCheckId));
    }

    private Instant calculateDueDate(KYCLevel level) {
        int daysToAdd = switch (level) {
            case STANDARD -> 30;
            case ENHANCED -> 60;
            case EXEMPT -> 0;
        };
        return Instant.now().plus(daysToAdd, ChronoUnit.DAYS);
    }

    private Instant calculateNextReviewDate(KYCLevel level) {
        int daysToAdd = switch (level) {
            case STANDARD -> 365;
            case ENHANCED -> 180;
            case EXEMPT -> 730;
        };
        return Instant.now().plus(daysToAdd, ChronoUnit.DAYS);
    }
}
