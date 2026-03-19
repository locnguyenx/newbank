package com.banking.customer.service;

import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.SanctionsScreeningResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class SanctionsScreeningService {

    private static final Logger log = LoggerFactory.getLogger(SanctionsScreeningService.class);

    @Transactional
    public SanctionsScreeningResult screenCustomer(KYCCheck kycCheck) {
        log.warn("STUB: SanctionsScreeningService always returns CLEAR. "
            + "This MUST be replaced with a real sanctions screening provider integration "
            + "(e.g., Dow Jones, World-Check, ComplyAdvantage) before production use.");
        SanctionsScreeningResult result = new SanctionsScreeningResult(
            kycCheck,
            Instant.now(),
            com.banking.customer.domain.enums.SanctionsScreeningResult.CLEAR
        );
        result.setIsCleared(true);
        return result;
    }

    public boolean isCustomerCleared(KYCCheck kycCheck) {
        log.warn("STUB: isCustomerCleared always returns true. Requires real integration.");
        return true;
    }
}
