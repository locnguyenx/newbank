package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.CustomerChargeOverride;
import com.banking.charges.domain.entity.ProductCharge;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.response.CustomerChargeOverrideResponse;
import com.banking.charges.dto.response.ProductChargeResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.CustomerChargeOverrideRepository;
import com.banking.charges.repository.ProductChargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargeAssignmentServiceTest {

    @Mock
    private ProductChargeRepository productChargeRepository;

    @Mock
    private CustomerChargeOverrideRepository customerChargeOverrideRepository;

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    private ChargeAssignmentService chargeAssignmentService;

    @BeforeEach
    void setUp() {
        chargeAssignmentService = new ChargeAssignmentService(
                productChargeRepository, customerChargeOverrideRepository, chargeDefinitionRepository);
    }

    @Test
    void assignToProduct_new_success() {
        ChargeDefinition charge = new ChargeDefinition("Account Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setId(1L);

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(productChargeRepository.findByChargeDefinitionIdAndProductCode(1L, "PROD-001"))
                .thenReturn(Optional.empty());
        when(productChargeRepository.save(any())).thenAnswer(inv -> {
            ProductCharge pc = inv.getArgument(0);
            pc.setId(1L);
            return pc;
        });

        ProductChargeResponse response = chargeAssignmentService.assignToProduct(
                1L, "PROD-001", new BigDecimal("50.00"));

        assertEquals("PROD-001", response.getProductCode());
        assertEquals(0, new BigDecimal("50.00").compareTo(response.getOverrideAmount()));
    }

    @Test
    void assignToProduct_update_success() {
        ChargeDefinition charge = new ChargeDefinition("Account Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setId(1L);

        ProductCharge existing = new ProductCharge(charge, "PROD-001", new BigDecimal("30.00"));
        existing.setId(1L);

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(productChargeRepository.findByChargeDefinitionIdAndProductCode(1L, "PROD-001"))
                .thenReturn(Optional.of(existing));
        when(productChargeRepository.save(any())).thenReturn(existing);

        ProductChargeResponse response = chargeAssignmentService.assignToProduct(
                1L, "PROD-001", new BigDecimal("45.00"));

        assertEquals(0, new BigDecimal("45.00").compareTo(response.getOverrideAmount()));
    }

    @Test
    void assignToCustomer_new_success() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(2L);

        when(chargeDefinitionRepository.findById(2L)).thenReturn(Optional.of(charge));
        when(customerChargeOverrideRepository.findByChargeDefinitionIdAndCustomerId(2L, 100L))
                .thenReturn(Optional.empty());
        when(customerChargeOverrideRepository.save(any())).thenAnswer(inv -> {
            CustomerChargeOverride override = inv.getArgument(0);
            override.setId(1L);
            return override;
        });

        CustomerChargeOverrideResponse response = chargeAssignmentService.assignToCustomer(
                2L, 100L, new BigDecimal("15.00"));

        assertEquals(100L, response.getCustomerId());
        assertEquals(0, new BigDecimal("15.00").compareTo(response.getOverrideAmount()));
    }

    @Test
    void getProductCharges_success() {
        ChargeDefinition charge = new ChargeDefinition("Account Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setId(1L);

        ProductCharge pc = new ProductCharge(charge, "PROD-001", new BigDecimal("50.00"));
        pc.setId(1L);

        when(productChargeRepository.findByProductCode("PROD-001")).thenReturn(List.of(pc));

        List<ProductChargeResponse> result = chargeAssignmentService.getProductCharges("PROD-001");

        assertEquals(1, result.size());
        assertEquals("PROD-001", result.get(0).getProductCode());
    }

    @Test
    void getCustomerOverrides_success() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(2L);

        CustomerChargeOverride override = new CustomerChargeOverride(charge, 100L, new BigDecimal("15.00"));
        override.setId(1L);

        when(customerChargeOverrideRepository.findByCustomerId(100L)).thenReturn(List.of(override));

        List<CustomerChargeOverrideResponse> result = chargeAssignmentService.getCustomerOverrides(100L);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getCustomerId());
    }

    @Test
    void assignToProduct_chargeNotFound_throws() {
        when(chargeDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ChargeNotFoundException.class, () ->
                chargeAssignmentService.assignToProduct(999L, "PROD-001", new BigDecimal("50.00")));
    }
}
