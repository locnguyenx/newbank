package com.banking.charges.service;

import com.banking.charges.domain.entity.InterestAccrual;
import com.banking.charges.domain.entity.InterestRate;
import com.banking.charges.dto.response.InterestAccrualResponse;
import com.banking.charges.exception.InterestRateNotFoundException;
import com.banking.charges.repository.InterestAccrualRepository;
import com.banking.charges.repository.InterestRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InterestAccrualService {

    private final InterestAccrualRepository accrualRepository;
    private final InterestRateRepository interestRateRepository;
    private final InterestRateService interestRateService;

    public InterestAccrualService(InterestAccrualRepository accrualRepository,
                                  InterestRateRepository interestRateRepository,
                                  InterestRateService interestRateService) {
        this.accrualRepository = accrualRepository;
        this.interestRateRepository = interestRateRepository;
        this.interestRateService = interestRateService;
    }

    public InterestAccrualResponse accrueDaily(String accountNumber, BigDecimal balance, LocalDate date, String productCode) {
        List<InterestRate> rates = interestRateRepository.findByProductCode(productCode);
        if (rates.isEmpty()) {
            throw new InterestRateNotFoundException(null);
        }
        InterestRate rate = rates.get(0);

        BigDecimal dailyRate = interestRateService.calculateRateForBalance(rate, balance)
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);

        BigDecimal amount = balance.multiply(dailyRate)
                .setScale(6, RoundingMode.HALF_UP);

        InterestAccrual accrual = new InterestAccrual(rate, accountNumber, date, balance, amount);
        InterestAccrual saved = accrualRepository.save(accrual);

        return InterestAccrualResponse.fromEntity(saved);
    }

    public BigDecimal applyInterest(String accountNumber, LocalDate date) {
        List<InterestAccrual> accruals = accrualRepository.findByAccountNumberAndApplied(accountNumber, false);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InterestAccrual accrual : accruals) {
            totalAmount = totalAmount.add(accrual.getAmount());
            accrual.setApplied(true);
            accrualRepository.save(accrual);
        }

        return totalAmount;
    }

    @Transactional(readOnly = true)
    public List<InterestAccrualResponse> getAccruals(String accountNumber, Boolean applied) {
        List<InterestAccrual> accruals = accrualRepository.findByAccountNumberAndApplied(accountNumber, applied);
        return accruals.stream()
                .map(InterestAccrualResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalUnappliedAccrual(String accountNumber) {
        List<InterestAccrual> accruals = accrualRepository.findByAccountNumberAndApplied(accountNumber, false);
        BigDecimal total = BigDecimal.ZERO;
        for (InterestAccrual accrual : accruals) {
            total = total.add(accrual.getAmount());
        }
        return total;
    }
}
