package com.banking.customer.event;

import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class KYCEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public KYCEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishKYCInitiated(KYCCheck kycCheck) {
        eventPublisher.publishEvent(new KYCInitiatedEvent(this, kycCheck));
    }

    public void publishDocumentsSubmitted(KYCCheck kycCheck) {
        eventPublisher.publishEvent(new KYCDocumentsSubmittedEvent(this, kycCheck));
    }

    public void publishSubmittedForReview(KYCCheck kycCheck) {
        eventPublisher.publishEvent(new KYCSubmittedForReviewEvent(this, kycCheck));
    }

    public void publishKYCApproved(KYCCheck kycCheck, String officerId) {
        eventPublisher.publishEvent(new KYCApprovedEvent(this, kycCheck, officerId));
    }

    public void publishKYCRejected(KYCCheck kycCheck, String officerId, String reason) {
        eventPublisher.publishEvent(new KYCRejectedEvent(this, kycCheck, officerId, reason));
    }

    public void publishReviewScheduled(KYCCheck kycCheck) {
        eventPublisher.publishEvent(new KYCReviewScheduledEvent(this, kycCheck));
    }
}
