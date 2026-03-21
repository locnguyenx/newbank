package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.ProductLimit;
import com.banking.limits.dto.response.AccountLimitResponse;
import com.banking.limits.dto.response.CustomerLimitResponse;
import com.banking.limits.dto.response.ProductLimitResponse;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class LimitAssignmentService {

    private final LimitDefinitionRepository limitDefinitionRepository;
    private final ProductLimitRepository productLimitRepository;
    private final CustomerLimitRepository customerLimitRepository;
    private final AccountLimitRepository accountLimitRepository;

    public LimitAssignmentService(LimitDefinitionRepository limitDefinitionRepository,
                                  ProductLimitRepository productLimitRepository,
                                  CustomerLimitRepository customerLimitRepository,
                                  AccountLimitRepository accountLimitRepository) {
        this.limitDefinitionRepository = limitDefinitionRepository;
        this.productLimitRepository = productLimitRepository;
        this.customerLimitRepository = customerLimitRepository;
        this.accountLimitRepository = accountLimitRepository;
    }

    public ProductLimitResponse assignToProduct(Long limitId, String productCode, BigDecimal overrideAmount) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));
        ProductLimit productLimit = new ProductLimit(limitDefinition, productCode, overrideAmount);
        ProductLimit saved = productLimitRepository.save(productLimit);
        return ProductLimitResponse.fromEntity(saved);
    }

    public CustomerLimitResponse assignToCustomer(Long limitId, Long customerId, BigDecimal overrideAmount) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));
        CustomerLimit customerLimit = new CustomerLimit(limitDefinition, customerId, overrideAmount);
        CustomerLimit saved = customerLimitRepository.save(customerLimit);
        return CustomerLimitResponse.fromEntity(saved);
    }

    public AccountLimitResponse assignToAccount(Long limitId, String accountNumber, BigDecimal overrideAmount) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));
        AccountLimit accountLimit = new AccountLimit(limitDefinition, accountNumber, overrideAmount);
        AccountLimit saved = accountLimitRepository.save(accountLimit);
        return AccountLimitResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductLimitResponse> getProductLimits(String productCode) {
        return productLimitRepository.findByProductCode(productCode).stream()
                .map(ProductLimitResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerLimitResponse> getCustomerLimits(Long customerId) {
        return customerLimitRepository.findByCustomerId(customerId).stream()
                .map(CustomerLimitResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountLimitResponse> getAccountLimits(String accountNumber) {
        return accountLimitRepository.findByAccountNumber(accountNumber).stream()
                .map(AccountLimitResponse::fromEntity)
                .toList();
    }
}
