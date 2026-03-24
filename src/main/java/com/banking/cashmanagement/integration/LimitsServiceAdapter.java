package com.banking.cashmanagement.integration;

import com.banking.limits.api.LimitCheckService;
import com.banking.limits.api.dto.LimitCheckResult;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class LimitsServiceAdapter {
    
    private final LimitCheckService limitCheckService;
    
    public LimitsServiceAdapter(LimitCheckService limitCheckService) {
        this.limitCheckService = limitCheckService;
    }
    
    public LimitCheckResult checkLimit(Long accountId, String transactionType, BigDecimal amount, String currency) {
        return limitCheckService.checkLimit(accountId, transactionType, amount, currency);
    }
    
    public boolean hasApprovalAuthority(Long userId, BigDecimal amount) {
        return limitCheckService.hasApprovalAuthority(userId, amount);
    }
}
