package com.banking.customer.event;

import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.context.ApplicationEvent;

public class KYCApprovedEvent extends ApplicationEvent {

    private final KYCCheck kycCheck;
    private final String officerId;

    public KYCApprovedEvent(Object source, KYCCheck kycCheck, String officerId) {
        super(source);
        this.kycCheck = kycCheck;
        this.officerId = officerId;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }

    public String getOfficerId() {
        return officerId;
    }
}
