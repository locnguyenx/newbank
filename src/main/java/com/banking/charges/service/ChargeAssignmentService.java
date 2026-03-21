package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.CustomerChargeOverride;
import com.banking.charges.domain.entity.ProductCharge;
import com.banking.charges.dto.response.CustomerChargeOverrideResponse;
import com.banking.charges.dto.response.ProductChargeResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.CustomerChargeOverrideRepository;
import com.banking.charges.repository.ProductChargeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ChargeAssignmentService {

    private final ProductChargeRepository productChargeRepository;
    private final CustomerChargeOverrideRepository customerChargeOverrideRepository;
    private final ChargeDefinitionRepository chargeDefinitionRepository;

    public ChargeAssignmentService(ProductChargeRepository productChargeRepository,
                                   CustomerChargeOverrideRepository customerChargeOverrideRepository,
                                   ChargeDefinitionRepository chargeDefinitionRepository) {
        this.productChargeRepository = productChargeRepository;
        this.customerChargeOverrideRepository = customerChargeOverrideRepository;
        this.chargeDefinitionRepository = chargeDefinitionRepository;
    }

    public ProductChargeResponse assignToProduct(Long chargeId, String productCode, BigDecimal overrideAmount) {
        ChargeDefinition chargeDefinition = chargeDefinitionRepository.findById(chargeId)
                .orElseThrow(() -> new ChargeNotFoundException(chargeId));

        ProductCharge productCharge = productChargeRepository
                .findByChargeDefinitionIdAndProductCode(chargeId, productCode)
                .orElse(new ProductCharge(chargeDefinition, productCode, overrideAmount));

        productCharge.setOverrideAmount(overrideAmount);

        ProductCharge saved = productChargeRepository.save(productCharge);
        return ProductChargeResponse.fromEntity(saved);
    }

    public void unassignFromProduct(Long chargeId, String productCode) {
        productChargeRepository.deleteByChargeDefinitionIdAndProductCode(chargeId, productCode);
    }

    @Transactional(readOnly = true)
    public List<ProductChargeResponse> getProductCharges(String productCode) {
        return productChargeRepository.findByProductCode(productCode).stream()
                .map(ProductChargeResponse::fromEntity)
                .toList();
    }

    public CustomerChargeOverrideResponse assignToCustomer(Long chargeId, Long customerId, BigDecimal overrideAmount) {
        ChargeDefinition chargeDefinition = chargeDefinitionRepository.findById(chargeId)
                .orElseThrow(() -> new ChargeNotFoundException(chargeId));

        CustomerChargeOverride override = customerChargeOverrideRepository
                .findByChargeDefinitionIdAndCustomerId(chargeId, customerId)
                .orElse(new CustomerChargeOverride(chargeDefinition, customerId, overrideAmount));

        override.setOverrideAmount(overrideAmount);

        CustomerChargeOverride saved = customerChargeOverrideRepository.save(override);
        return CustomerChargeOverrideResponse.fromEntity(saved);
    }

    public void unassignFromCustomer(Long chargeId, Long customerId) {
        customerChargeOverrideRepository.deleteByChargeDefinitionIdAndCustomerId(chargeId, customerId);
    }

    @Transactional(readOnly = true)
    public List<CustomerChargeOverrideResponse> getCustomerOverrides(Long customerId) {
        return customerChargeOverrideRepository.findByCustomerId(customerId).stream()
                .map(CustomerChargeOverrideResponse::fromEntity)
                .toList();
    }
}
