package com.banking.limits.api;

import com.banking.limits.api.dto.LimitCheckResult;

import java.math.BigDecimal;

public interface LimitCheckService {

    LimitCheckResult checkLimit(Long accountId, String transactionType, BigDecimal amount, String currency);

    boolean hasApprovalAuthority(Long userId, BigDecimal amount);
}
