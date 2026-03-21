package com.banking.charges.service;

import com.banking.charges.domain.entity.*;
import com.banking.charges.domain.enums.CalculationMethod;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.ChargeCalculationRequest;
import com.banking.charges.dto.response.ChargeCalculationResponse;
import com.banking.charges.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChargeCalculationServiceTest {

    @Mock
    private CustomerChargeOverrideRepository customerOverrideRepository;

    @Mock
    private ProductChargeRepository productChargeRepository;

    @Mock
    private ChargeRuleRepository chargeRuleRepository;

    @Mock
    private ChargeTierRepository chargeTierRepository;

    @Mock
    private FeeWaiverRepository feeWaiverRepository;

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    @Mock
    private InterestRateService interestRateService;

    private ChargeCalculationService chargeCalculationService;

    @BeforeEach
    void setUp() {
        chargeCalculationService = new ChargeCalculationService(
                customerOverrideRepository,
                productChargeRepository,
                chargeRuleRepository,
                chargeTierRepository,
                feeWaiverRepository,
                chargeDefinitionRepository,
                interestRateService
        );
    }

    @Test
    void calculate_flatCharge() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");

        ChargeDefinition chargeDefinition = new ChargeDefinition("Transaction Fee", ChargeType.TRANSACTION_FEE, "USD");
        chargeDefinition.setId(1L);
        chargeDefinition.setStatus(ChargeStatus.ACTIVE);

        ChargeRule rule = new ChargeRule(chargeDefinition, CalculationMethod.FLAT, new BigDecimal("10.00"), null, null, null);
        rule.setId(1L);

        when(chargeDefinitionRepository.findByChargeType(ChargeType.TRANSACTION_FEE)).thenReturn(List.of(chargeDefinition));
        when(chargeRuleRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(rule));

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(new BigDecimal("10.0000"), response.getBaseAmount());
        assertEquals(new BigDecimal("10.0000"), response.getFinalAmount());
        assertFalse(response.isWaiverApplied());
    }

    @Test
    void calculate_percentageCharge() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");
        request.setTransactionAmount(new BigDecimal("1000.00"));

        ChargeDefinition chargeDefinition = new ChargeDefinition("Transaction Fee", ChargeType.TRANSACTION_FEE, "USD");
        chargeDefinition.setId(1L);
        chargeDefinition.setStatus(ChargeStatus.ACTIVE);

        ChargeRule rule = new ChargeRule(chargeDefinition, CalculationMethod.PERCENTAGE, null, new BigDecimal("1.00"), null, null);
        rule.setId(1L);

        when(chargeDefinitionRepository.findByChargeType(ChargeType.TRANSACTION_FEE)).thenReturn(List.of(chargeDefinition));
        when(chargeRuleRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(rule));

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(new BigDecimal("10.0000"), response.getBaseAmount());
        assertEquals(new BigDecimal("10.0000"), response.getFinalAmount());
    }

    @Test
    void calculate_withWaiver() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");
        request.setTransactionAmount(new BigDecimal("1000.00"));

        ChargeDefinition chargeDefinition = new ChargeDefinition("Transaction Fee", ChargeType.TRANSACTION_FEE, "USD");
        chargeDefinition.setId(1L);
        chargeDefinition.setStatus(ChargeStatus.ACTIVE);

        ChargeRule rule = new ChargeRule(chargeDefinition, CalculationMethod.PERCENTAGE, null, new BigDecimal("10.00"), null, null);
        rule.setId(1L);

        FeeWaiver waiver = new FeeWaiver(chargeDefinition, com.banking.charges.domain.enums.WaiverScope.CUSTOMER, "123",
                50, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        waiver.setId(1L);

        when(chargeDefinitionRepository.findByChargeType(ChargeType.TRANSACTION_FEE)).thenReturn(List.of(chargeDefinition));
        when(chargeRuleRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(rule));
        when(feeWaiverRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(waiver));

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(new BigDecimal("100.0000"), response.getBaseAmount());
        assertEquals(new BigDecimal("50.0000"), response.getWaiverAmount());
        assertEquals(new BigDecimal("50.0000"), response.getFinalAmount());
        assertTrue(response.isWaiverApplied());
        assertNotNull(response.getWaiverId());
    }

    @Test
    void calculate_noChargeFound() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("UNKNOWN_CHARGE");
        request.setCurrency("USD");

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(BigDecimal.ZERO, response.getBaseAmount());
        assertEquals(BigDecimal.ZERO, response.getFinalAmount());
    }

    @Test
    void calculate_inactiveCharge() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");

        ChargeDefinition chargeDefinition = new ChargeDefinition("Transaction Fee", ChargeType.TRANSACTION_FEE, "USD");
        chargeDefinition.setStatus(ChargeStatus.INACTIVE);

        when(chargeDefinitionRepository.findByChargeType(ChargeType.TRANSACTION_FEE)).thenReturn(List.of(chargeDefinition));

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(BigDecimal.ZERO, response.getBaseAmount());
        assertEquals(BigDecimal.ZERO, response.getFinalAmount());
    }

    @Test
    void calculate_tieredVolumeCharge() {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setProductCode("SAVINGS");
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");
        request.setTransactionCount(150L);

        ChargeDefinition chargeDefinition = new ChargeDefinition("Transaction Fee", ChargeType.TRANSACTION_FEE, "USD");
        chargeDefinition.setId(1L);
        chargeDefinition.setStatus(ChargeStatus.ACTIVE);

        ChargeRule rule = new ChargeRule(chargeDefinition, CalculationMethod.TIERED_VOLUME, null, null, null, null);
        rule.setId(1L);

        ChargeTier tier1 = new ChargeTier(rule, 0L, 100L, new BigDecimal("0.10"));
        ChargeTier tier2 = new ChargeTier(rule, 100L, null, new BigDecimal("0.05"));

        when(chargeDefinitionRepository.findByChargeType(ChargeType.TRANSACTION_FEE)).thenReturn(List.of(chargeDefinition));
        when(chargeRuleRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(rule));
        when(chargeTierRepository.findByChargeRuleId(1L)).thenReturn(List.of(tier1, tier2));

        ChargeCalculationResponse response = chargeCalculationService.calculate(request);

        assertEquals(new BigDecimal("7.5000"), response.getBaseAmount());
    }
}
