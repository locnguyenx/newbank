package com.banking.customer.service;

import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.SanctionsScreeningResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class SanctionsScreeningService {

    @Transactional
    public SanctionsScreeningResult screenCustomer(KYCCheck kycCheck) {
        SanctionsScreeningResult result = new SanctionsScreeningResult(
            kycCheck,
            Instant.now(),
            com.banking.customer.domain.enums.SanctionsScreeningResult.CLEAR
        );
        result.setIsCleared(true);
        return result;
    }

    public boolean isCustomerCleared(KYCCheck kycCheck) {
        return true;
    }
}
