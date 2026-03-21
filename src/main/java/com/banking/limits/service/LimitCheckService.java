package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.ApprovalThreshold;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.ProductLimit;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.request.LimitCheckRequest;
import com.banking.limits.dto.response.LimitCheckResponse;
import com.banking.limits.exception.InvalidLimitTypeException;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.ApprovalThresholdRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LimitCheckService {

    private final AccountLimitRepository accountLimitRepository;
    private final CustomerLimitRepository customerLimitRepository;
    private final ProductLimitRepository productLimitRepository;
    private final LimitDefinitionRepository limitDefinitionRepository;
    private final ApprovalThresholdRepository approvalThresholdRepository;
    private final LimitUsageService limitUsageService;

    public LimitCheckService(AccountLimitRepository accountLimitRepository,
                             CustomerLimitRepository customerLimitRepository,
                             ProductLimitRepository productLimitRepository,
                             LimitDefinitionRepository limitDefinitionRepository,
                             ApprovalThresholdRepository approvalThresholdRepository,
                             LimitUsageService limitUsageService) {
        this.accountLimitRepository = accountLimitRepository;
        this.customerLimitRepository = customerLimitRepository;
        this.productLimitRepository = productLimitRepository;
        this.limitDefinitionRepository = limitDefinitionRepository;
        this.approvalThresholdRepository = approvalThresholdRepository;
        this.limitUsageService = limitUsageService;
    }

    public LimitCheckResponse checkLimit(LimitCheckRequest request) {
        LimitType limitType = parseLimitType(request.getLimitType());

        Optional<LimitDefinition> effectiveLimitDef = findEffectiveLimit(
                request.getAccountNumber(),
                request.getCustomerId(),
                request.getProductCode(),
                limitType
        );

        if (effectiveLimitDef.isEmpty()) {
            return LimitCheckResponse.noLimit();
        }

        LimitDefinition limitDef = effectiveLimitDef.get();

        if (limitDef.getStatus() != LimitStatus.ACTIVE) {
            return LimitCheckResponse.noLimit();
        }

        BigDecimal effectiveAmount = calculateEffectiveAmount(limitDef, request.getAccountNumber());
        BigDecimal currentUsage = limitUsageService.getCumulativeUsage(
                limitDef.getId(),
                request.getAccountNumber(),
                LocalDate.now()
        );

        BigDecimal projectedUsage = currentUsage.add(request.getTransactionAmount());

        if (projectedUsage.compareTo(effectiveAmount) > 0) {
            return LimitCheckResponse.rejected(
                    effectiveAmount,
                    currentUsage,
                    "Transaction would exceed limit"
            );
        }

        Optional<ApprovalThreshold> threshold = approvalThresholdRepository
                .findByLimitDefinitionId(limitDef.getId());

        if (threshold.isPresent() && requiresApproval(request.getTransactionAmount(), effectiveAmount, threshold.get())) {
            BigDecimal remaining = effectiveAmount.subtract(currentUsage);
            return LimitCheckResponse.requiresApproval(effectiveAmount, currentUsage, remaining);
        }

        BigDecimal remaining = effectiveAmount.subtract(projectedUsage);
        return LimitCheckResponse.allowed(effectiveAmount, currentUsage, remaining);
    }

    private Optional<LimitDefinition> findEffectiveLimit(String accountNumber, Long customerId, 
                                                           String productCode, LimitType limitType) {
        List<AccountLimit> accountLimits = accountLimitRepository.findByAccountNumber(accountNumber);
        for (AccountLimit al : accountLimits) {
            if (al.getLimitDefinition().getLimitType() == limitType) {
                return Optional.of(al.getLimitDefinition());
            }
        }

        if (customerId != null) {
            List<CustomerLimit> customerLimits = customerLimitRepository.findByCustomerId(customerId);
            for (CustomerLimit cl : customerLimits) {
                if (cl.getLimitDefinition().getLimitType() == limitType) {
                    return Optional.of(cl.getLimitDefinition());
                }
            }
        }

        if (productCode != null) {
            List<ProductLimit> productLimits = productLimitRepository.findByProductCode(productCode);
            for (ProductLimit pl : productLimits) {
                if (pl.getLimitDefinition().getLimitType() == limitType) {
                    return Optional.of(pl.getLimitDefinition());
                }
            }
        }

        return Optional.empty();
    }

    private BigDecimal calculateEffectiveAmount(LimitDefinition limitDef, String accountNumber) {
        List<AccountLimit> accountLimits = accountLimitRepository.findByAccountNumber(accountNumber);
        for (AccountLimit al : accountLimits) {
            if (al.getLimitDefinition().getId().equals(limitDef.getId())) {
                if (al.getOverrideAmount() != null) {
                    return al.getOverrideAmount();
                }
            }
        }

        return limitDef.getAmount();
    }

    private boolean requiresApproval(BigDecimal transactionAmount, BigDecimal effectiveLimit, 
                                      ApprovalThreshold threshold) {
        if (threshold.getAbsoluteAmount() != null && transactionAmount.compareTo(threshold.getAbsoluteAmount()) > 0) {
            return true;
        }

        if (threshold.getPercentageOfLimit() != null) {
            BigDecimal percentageAmount = effectiveLimit
                    .multiply(new BigDecimal(threshold.getPercentageOfLimit()))
                    .divide(new BigDecimal(100));
            if (transactionAmount.compareTo(percentageAmount) > 0) {
                return true;
            }
        }

        return false;
    }

    private LimitType parseLimitType(String limitType) {
        try {
            return LimitType.valueOf(limitType);
        } catch (IllegalArgumentException e) {
            throw new InvalidLimitTypeException(limitType);
        }
    }
}