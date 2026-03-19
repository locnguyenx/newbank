package com.banking.customer.event;

import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.context.ApplicationEvent;

public class KYCInitiatedEvent extends ApplicationEvent {

    private final KYCCheck kycCheck;

    public KYCInitiatedEvent(Object source, KYCCheck kycCheck) {
        super(source);
        this.kycCheck = kycCheck;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }
}
