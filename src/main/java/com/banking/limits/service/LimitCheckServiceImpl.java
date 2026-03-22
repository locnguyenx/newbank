package com.banking.limits.service;

import com.banking.limits.api.dto.LimitCheckResult;
import com.banking.limits.dto.request.LimitCheckRequest;
import com.banking.limits.dto.response.LimitCheckResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LimitCheckServiceImpl implements com.banking.limits.api.LimitCheckService {

    private final LimitCheckService limitCheckService;

    public LimitCheckServiceImpl(LimitCheckService limitCheckService) {
        this.limitCheckService = limitCheckService;
    }

    @Override
    public LimitCheckResult checkLimit(Long accountId, String transactionType, BigDecimal amount, String currency) {
        LimitCheckRequest request = new LimitCheckRequest();
        request.setAccountNumber(String.valueOf(accountId));
        request.setLimitType(transactionType);
        request.setTransactionAmount(amount);
        request.setCurrency(currency);

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        boolean approved = response.getResult() != com.banking.limits.domain.enums.LimitCheckResult.REJECTED;
        String reason = response.getRejectionReason();
        BigDecimal remaining = response.getRemainingAmount();

        return new LimitCheckResult(approved, reason, remaining);
    }

    @Override
    public boolean hasApprovalAuthority(Long userId, BigDecimal amount) {
        return false;
    }
}
