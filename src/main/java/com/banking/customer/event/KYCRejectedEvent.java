package com.banking.customer.event;

import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.context.ApplicationEvent;

public class KYCRejectedEvent extends ApplicationEvent {

    private final KYCCheck kycCheck;
    private final String officerId;
    private final String reason;

    public KYCRejectedEvent(Object source, KYCCheck kycCheck, String officerId, String reason) {
        super(source);
        this.kycCheck = kycCheck;
        this.officerId = officerId;
        this.reason = reason;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }

    public String getOfficerId() {
        return officerId;
    }

    public String getReason() {
        return reason;
    }
}
