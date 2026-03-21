package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.ChargeRule;
import com.banking.charges.domain.enums.CalculationMethod;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.CreateChargeRuleRequest;
import com.banking.charges.dto.request.TierRequest;
import com.banking.charges.dto.response.ChargeRuleResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InvalidCalculationMethodException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.ChargeRuleRepository;
import com.banking.charges.repository.ChargeTierRepository;
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
class ChargeRuleServiceTest {

    @Mock
    private ChargeRuleRepository chargeRuleRepository;

    @Mock
    private ChargeTierRepository chargeTierRepository;

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    private ChargeRuleService chargeRuleService;

    @BeforeEach
    void setUp() {
        chargeRuleService = new ChargeRuleService(chargeRuleRepository, chargeTierRepository, chargeDefinitionRepository);
    }

    @Test
    void addRule_flat_success() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        CreateChargeRuleRequest request = new CreateChargeRuleRequest();
        request.setCalculationMethod("FLAT");
        request.setFlatAmount(new BigDecimal("25.00"));

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(chargeRuleRepository.save(any())).thenAnswer(inv -> {
            ChargeRule rule = inv.getArgument(0);
            rule.setId(1L);
            return rule;
        });

        ChargeRuleResponse response = chargeRuleService.addRule(1L, request);

        assertEquals("FLAT", response.getCalculationMethod());
        assertEquals(1L, response.getChargeDefinitionId());
        verify(chargeRuleRepository).save(any());
    }

    @Test
    void addRule_tiered_success() {
        ChargeDefinition charge = new ChargeDefinition("Volume Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(2L);

        TierRequest tier1 = new TierRequest(0L, 1000L, new BigDecimal("0.01"));
        TierRequest tier2 = new TierRequest(1001L, null, new BigDecimal("0.005"));

        CreateChargeRuleRequest request = new CreateChargeRuleRequest();
        request.setCalculationMethod("TIERED_VOLUME");
        request.setTiers(List.of(tier2, tier1));

        when(chargeDefinitionRepository.findById(2L)).thenReturn(Optional.of(charge));
        when(chargeRuleRepository.save(any())).thenAnswer(inv -> {
            ChargeRule rule = inv.getArgument(0);
            rule.setId(1L);
            return rule;
        });

        ChargeRuleResponse response = chargeRuleService.addRule(2L, request);

        assertEquals("TIERED_VOLUME", response.getCalculationMethod());
        assertEquals(2, response.getTiers().size());
        assertEquals(0L, response.getTiers().get(0).getTierFrom());
    }

    @Test
    void addRule_chargeNotFound_throws() {
        CreateChargeRuleRequest request = new CreateChargeRuleRequest();
        request.setCalculationMethod("FLAT");

        when(chargeDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ChargeNotFoundException.class, () -> chargeRuleService.addRule(999L, request));
    }

    @Test
    void addRule_invalidMethod_throws() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        CreateChargeRuleRequest request = new CreateChargeRuleRequest();
        request.setCalculationMethod("INVALID_METHOD");

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));

        assertThrows(InvalidCalculationMethodException.class, () -> chargeRuleService.addRule(1L, request));
    }

    @Test
    void removeRule_success() {
        when(chargeRuleRepository.existsById(1L)).thenReturn(true);

        chargeRuleService.removeRule(1L);

        verify(chargeRuleRepository).deleteById(1L);
    }

    @Test
    void removeRule_notFound_throws() {
        when(chargeRuleRepository.existsById(999L)).thenReturn(false);

        assertThrows(ChargeRuleService.RuleNotFoundException.class, () -> chargeRuleService.removeRule(999L));
    }
}
