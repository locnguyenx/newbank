package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.InterestAccrual;
import com.banking.charges.domain.entity.InterestRate;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.domain.enums.InterestSchedule;
import com.banking.charges.dto.response.InterestAccrualResponse;
import com.banking.charges.exception.InterestRateNotFoundException;
import com.banking.charges.repository.InterestAccrualRepository;
import com.banking.charges.repository.InterestRateRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestAccrualServiceTest {

    @Mock
    private InterestAccrualRepository accrualRepository;

    @Mock
    private InterestRateRepository interestRateRepository;

    @Mock
    private InterestRateService interestRateService;

    private InterestAccrualService interestAccrualService;

    @BeforeEach
    void setUp() {
        interestAccrualService = new InterestAccrualService(
                accrualRepository,
                interestRateRepository,
                interestRateService
        );
    }

    @Test
    void accrueDaily_success() {
        String accountNumber = "ACC123";
        BigDecimal balance = new BigDecimal("10000");
        LocalDate date = LocalDate.of(2024, 1, 15);
        String productCode = "SAVINGS";

        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, productCode, new BigDecimal("0.05"),
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        when(interestRateRepository.findByProductCode(productCode)).thenReturn(List.of(rate));
        when(interestRateService.calculateRateForBalance(rate, balance)).thenReturn(new BigDecimal("0.05"));
        when(accrualRepository.save(any())).thenAnswer(inv -> {
            InterestAccrual accrual = inv.getArgument(0);
            accrual.setId(1L);
            return accrual;
        });

        InterestAccrualResponse response = interestAccrualService.accrueDaily(accountNumber, balance, date, productCode);

        assertNotNull(response);
        assertEquals("ACC123", response.getAccountNumber());
        assertEquals(date, response.getAccrualDate());
        verify(accrualRepository).save(any());
    }

    @Test
    void accrueDaily_noRateFound_throws() {
        when(interestRateRepository.findByProductCode("SAVINGS")).thenReturn(Collections.emptyList());

        assertThrows(InterestRateNotFoundException.class, () ->
                interestAccrualService.accrueDaily("ACC123", new BigDecimal("10000"), LocalDate.now(), "SAVINGS"));
    }

    @Test
    void applyInterest_success() {
        String accountNumber = "ACC123";
        LocalDate date = LocalDate.of(2024, 1, 15);

        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, "SAVINGS", new BigDecimal("0.05"),
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        InterestAccrual accrual1 = new InterestAccrual(rate, accountNumber, date, new BigDecimal("10000"), new BigDecimal("1.37"));
        InterestAccrual accrual2 = new InterestAccrual(rate, accountNumber, date.plusDays(1), new BigDecimal("10000"), new BigDecimal("1.37"));

        when(accrualRepository.findByAccountNumberAndApplied(accountNumber, false)).thenReturn(List.of(accrual1, accrual2));
        when(accrualRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BigDecimal total = interestAccrualService.applyInterest(accountNumber, date);

        assertEquals(new BigDecimal("2.74"), total);
        assertTrue(accrual1.getApplied());
        assertTrue(accrual2.getApplied());
    }

    @Test
    void getAccruals_success() {
        String accountNumber = "ACC123";

        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, "SAVINGS", new BigDecimal("0.05"),
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        InterestAccrual accrual = new InterestAccrual(rate, accountNumber, LocalDate.now(), new BigDecimal("10000"), new BigDecimal("1.37"));

        when(accrualRepository.findByAccountNumberAndApplied(accountNumber, false)).thenReturn(List.of(accrual));

        List<InterestAccrualResponse> responses = interestAccrualService.getAccruals(accountNumber, false);

        assertEquals(1, responses.size());
        assertEquals("ACC123", responses.get(0).getAccountNumber());
    }

    @Test
    void getTotalUnappliedAccrual_success() {
        String accountNumber = "ACC123";

        ChargeDefinition chargeDefinition = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        InterestRate rate = new InterestRate(chargeDefinition, "SAVINGS", new BigDecimal("0.05"),
                InterestSchedule.DAILY, InterestSchedule.MONTHLY);
        rate.setId(1L);

        InterestAccrual accrual1 = new InterestAccrual(rate, accountNumber, LocalDate.now(), new BigDecimal("10000"), new BigDecimal("1.37"));
        InterestAccrual accrual2 = new InterestAccrual(rate, accountNumber, LocalDate.now().plusDays(1), new BigDecimal("10000"), new BigDecimal("1.37"));

        when(accrualRepository.findByAccountNumberAndApplied(accountNumber, false)).thenReturn(List.of(accrual1, accrual2));

        BigDecimal total = interestAccrualService.getTotalUnappliedAccrual(accountNumber);

        assertEquals(new BigDecimal("2.74"), total);
    }
}
