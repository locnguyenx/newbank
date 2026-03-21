package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.ProductLimit;
import com.banking.limits.dto.response.EffectiveLimitResponse;
import com.banking.limits.exception.InvalidLimitTypeException;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LimitQueryService {

    private final AccountLimitRepository accountLimitRepository;
    private final CustomerLimitRepository customerLimitRepository;
    private final ProductLimitRepository productLimitRepository;

    public LimitQueryService(AccountLimitRepository accountLimitRepository,
                             CustomerLimitRepository customerLimitRepository,
                             ProductLimitRepository productLimitRepository) {
        this.accountLimitRepository = accountLimitRepository;
        this.customerLimitRepository = customerLimitRepository;
        this.productLimitRepository = productLimitRepository;
    }

    public EffectiveLimitResponse getEffectiveLimit(String accountNumber, Long customerId, 
                                                      String productCode, String currency, String limitType) {
        List<AccountLimit> accountLimits = accountLimitRepository.findByAccountNumber(accountNumber);
        
        for (AccountLimit al : accountLimits) {
            LimitDefinition ld = al.getLimitDefinition();
            if (matchesLimitType(ld, limitType) && matchesCurrency(ld, currency)) {
                BigDecimal effectiveAmount = al.getOverrideAmount() != null ? al.getOverrideAmount() : ld.getAmount();
                return EffectiveLimitResponse.fromAccount(
                        ld.getId(),
                        ld.getName(),
                        ld.getLimitType().name(),
                        effectiveAmount,
                        ld.getCurrency(),
                        ld.getStatus().name()
                );
            }
        }

        if (customerId != null) {
            List<CustomerLimit> customerLimits = customerLimitRepository.findByCustomerId(customerId);
            for (CustomerLimit cl : customerLimits) {
                LimitDefinition ld = cl.getLimitDefinition();
                if (matchesLimitType(ld, limitType) && matchesCurrency(ld, currency)) {
                    BigDecimal effectiveAmount = cl.getOverrideAmount() != null ? cl.getOverrideAmount() : ld.getAmount();
                    return EffectiveLimitResponse.fromCustomer(
                            ld.getId(),
                            ld.getName(),
                            ld.getLimitType().name(),
                            effectiveAmount,
                            ld.getCurrency(),
                            ld.getStatus().name()
                    );
                }
            }
        }

        if (productCode != null) {
            List<ProductLimit> productLimits = productLimitRepository.findByProductCode(productCode);
            for (ProductLimit pl : productLimits) {
                LimitDefinition ld = pl.getLimitDefinition();
                if (matchesLimitType(ld, limitType) && matchesCurrency(ld, currency)) {
                    BigDecimal effectiveAmount = pl.getOverrideAmount() != null ? pl.getOverrideAmount() : ld.getAmount();
                    return EffectiveLimitResponse.fromProduct(
                            ld.getId(),
                            ld.getName(),
                            ld.getLimitType().name(),
                            effectiveAmount,
                            ld.getCurrency(),
                            ld.getStatus().name()
                    );
                }
            }
        }

        return null;
    }

    public List<EffectiveLimitResponse> getAllEffectiveLimits(String accountNumber, Long customerId, 
                                                               String productCode, String currency) {
        List<EffectiveLimitResponse> results = new ArrayList<>();

        List<AccountLimit> accountLimits = accountLimitRepository.findByAccountNumber(accountNumber);
        for (AccountLimit al : accountLimits) {
            LimitDefinition ld = al.getLimitDefinition();
            if (matchesCurrency(ld, currency)) {
                BigDecimal effectiveAmount = al.getOverrideAmount() != null ? al.getOverrideAmount() : ld.getAmount();
                results.add(EffectiveLimitResponse.fromAccount(
                        ld.getId(),
                        ld.getName(),
                        ld.getLimitType().name(),
                        effectiveAmount,
                        ld.getCurrency(),
                        ld.getStatus().name()
                ));
            }
        }

        if (customerId != null) {
            List<CustomerLimit> customerLimits = customerLimitRepository.findByCustomerId(customerId);
            for (CustomerLimit cl : customerLimits) {
                LimitDefinition ld = cl.getLimitDefinition();
                if (matchesCurrency(ld, currency) && !hasAccountLevelOverride(accountLimits, ld.getId())) {
                    BigDecimal effectiveAmount = cl.getOverrideAmount() != null ? cl.getOverrideAmount() : ld.getAmount();
                    results.add(EffectiveLimitResponse.fromCustomer(
                            ld.getId(),
                            ld.getName(),
                            ld.getLimitType().name(),
                            effectiveAmount,
                            ld.getCurrency(),
                            ld.getStatus().name()
                    ));
                }
            }
        }

        if (productCode != null) {
            List<ProductLimit> productLimits = productLimitRepository.findByProductCode(productCode);
            for (ProductLimit pl : productLimits) {
                LimitDefinition ld = pl.getLimitDefinition();
                if (matchesCurrency(ld, currency) && 
                    !hasAccountLevelOverride(accountLimits, ld.getId()) &&
                    !hasCustomerLevelOverride(customerId, ld.getId())) {
                    BigDecimal effectiveAmount = pl.getOverrideAmount() != null ? pl.getOverrideAmount() : ld.getAmount();
                    results.add(EffectiveLimitResponse.fromProduct(
                            ld.getId(),
                            ld.getName(),
                            ld.getLimitType().name(),
                            effectiveAmount,
                            ld.getCurrency(),
                            ld.getStatus().name()
                    ));
                }
            }
        }

        return results;
    }

    private boolean matchesLimitType(LimitDefinition ld, String limitType) {
        if (limitType == null) {
            return true;
        }
        try {
            return ld.getLimitType().name().equalsIgnoreCase(limitType);
        } catch (IllegalArgumentException e) {
            throw new InvalidLimitTypeException(limitType);
        }
    }

    private boolean matchesCurrency(LimitDefinition ld, String currency) {
        if (currency == null) {
            return true;
        }
        return ld.getCurrency().equalsIgnoreCase(currency);
    }

    private boolean hasAccountLevelOverride(List<AccountLimit> accountLimits, Long limitDefId) {
        for (AccountLimit al : accountLimits) {
            if (al.getLimitDefinition().getId().equals(limitDefId) && al.getOverrideAmount() != null) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCustomerLevelOverride(Long customerId, Long limitDefId) {
        if (customerId == null) {
            return false;
        }
        List<CustomerLimit> customerLimits = customerLimitRepository.findByCustomerId(customerId);
        for (CustomerLimit cl : customerLimits) {
            if (cl.getLimitDefinition().getId().equals(limitDefId) && cl.getOverrideAmount() != null) {
                return true;
            }
        }
        return false;
    }
}