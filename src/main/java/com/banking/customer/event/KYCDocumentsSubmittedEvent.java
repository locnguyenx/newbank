package com.banking.customer.event;

import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.context.ApplicationEvent;

public class KYCDocumentsSubmittedEvent extends ApplicationEvent {

    private final KYCCheck kycCheck;

    public KYCDocumentsSubmittedEvent(Object source, KYCCheck kycCheck) {
        super(source);
        this.kycCheck = kycCheck;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }
}
