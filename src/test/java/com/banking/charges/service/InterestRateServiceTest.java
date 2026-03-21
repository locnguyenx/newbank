package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.InterestRate;
import com.banking.charges.domain.entity.InterestTier;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.domain.enums.InterestSchedule;
import com.banking.charges.dto.request.CreateInterestRateRequest;
import com.banking.charges.dto.request.InterestTierRequest;
import com.banking.charges.dto.response.InterestRateResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InterestRateNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.InterestRateRepository;
import com.banking.charges.repository.InterestTierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestRateServiceTest {

    @Mock
    private InterestRateRepository interestRateRepository;

    @Mock
    private InterestTierRepository interestTierRepository;

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    private InterestRateService interestRateService;

    @BeforeEach
    void setUp() {
        interestRateService = new InterestRateService(
                interestRateRepository,
                interestTierRepository,
                chargeDefinitionRepository
        );
    }

    private CreateInterestRateRequest createRequest(Long chargeId, String productCode, BigDecimal fixedRate) {
        CreateInterestRateRequest request = new CreateInterestRateRequest();
        request.setChargeId(chargeId);
        request.setProductCode(productCode);
        request.setFixedRate(fixedRate);
        request.setAccrualSchedule("DAILY");
        request.setApplicationSchedule("MONTHLY");
        return request;
    }

    @Test
    void createInterestRate_success() {
        CreateInterestRateRequest request = createRequest(1L, "SAVINGS", new BigDecimal("0.05"));
        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        chargeDefinition.setId(1L);

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(chargeDefinition));
        when(interestRateRepository.save(any())).thenAnswer(inv -> {
            InterestRate rate = inv.getArgument(0);
            rate.setId(1L);
            return rate;
        });

        InterestRateResponse response = interestRateService.createInterestRate(request);

        assertNotNull(response);
        assertEquals("SAVINGS", response.getProductCode());
        assertEquals(new BigDecimal("0.05"), response.getFixedRate());
        verify(interestRateRepository).save(any());
    }

    @Test
    void createInterestRate_chargeNotFound_throws() {
        CreateInterestRateRequest request = createRequest(999L, "SAVINGS", new BigDecimal("0.05"));

        when(chargeDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ChargeNotFoundException.class, () ->
                interestRateService.createInterestRate(request));
    }

    @Test
    void getInterestRate_notFound_throws() {
        when(interestRateRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(InterestRateNotFoundException.class, () ->
                interestRateService.getInterestRate(999L));
    }

    @Test
    void calculateRateForBalance_fixedRate() {
        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, "SAVINGS", new BigDecimal("0.05"),
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        BigDecimal calculatedRate = interestRateService.calculateRateForBalance(rate, new BigDecimal("10000"));

        assertEquals(new BigDecimal("0.05"), calculatedRate);
    }

    @Test
    void calculateRateForBalance_tieredRate() {
        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, "SAVINGS", null,
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        InterestTier tier1 = new InterestTier(rate, BigDecimal.ZERO, new BigDecimal("5000"), new BigDecimal("0.02"));
        InterestTier tier2 = new InterestTier(rate, new BigDecimal("5000"), null, new BigDecimal("0.05"));
        rate.setTiers(List.of(tier1, tier2));

        when(interestTierRepository.findByInterestRateIdOrderByBalanceFrom(1L)).thenReturn(List.of(tier1, tier2));

        BigDecimal calculatedRate = interestRateService.calculateRateForBalance(rate, new BigDecimal("10000"));

        assertEquals(new BigDecimal("0.05"), calculatedRate);
    }
}
