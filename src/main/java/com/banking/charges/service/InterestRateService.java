package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.InterestRate;
import com.banking.charges.domain.entity.InterestTier;
import com.banking.charges.domain.enums.InterestSchedule;
import com.banking.charges.dto.request.CreateInterestRateRequest;
import com.banking.charges.dto.request.InterestTierRequest;
import com.banking.charges.dto.response.InterestRateResponse;
import com.banking.charges.dto.response.InterestTierResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InterestRateNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.InterestRateRepository;
import com.banking.charges.repository.InterestTierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InterestRateService {

    private final InterestRateRepository interestRateRepository;
    private final InterestTierRepository interestTierRepository;
    private final ChargeDefinitionRepository chargeDefinitionRepository;

    public InterestRateService(InterestRateRepository interestRateRepository,
                              InterestTierRepository interestTierRepository,
                              ChargeDefinitionRepository chargeDefinitionRepository) {
        this.interestRateRepository = interestRateRepository;
        this.interestTierRepository = interestTierRepository;
        this.chargeDefinitionRepository = chargeDefinitionRepository;
    }

    public InterestRateResponse createInterestRate(CreateInterestRateRequest request) {
        ChargeDefinition chargeDefinition = chargeDefinitionRepository.findById(request.getChargeId())
                .orElseThrow(() -> new ChargeNotFoundException(request.getChargeId()));

        InterestSchedule accrualSchedule = InterestSchedule.valueOf(request.getAccrualSchedule());
        InterestSchedule applicationSchedule = InterestSchedule.valueOf(request.getApplicationSchedule());

        InterestRate interestRate = new InterestRate(
                chargeDefinition,
                request.getProductCode(),
                request.getFixedRate(),
                accrualSchedule,
                applicationSchedule
        );

        InterestRate saved = interestRateRepository.save(interestRate);

        if (request.getTiers() != null && !request.getTiers().isEmpty()) {
            for (InterestTierRequest tierRequest : request.getTiers()) {
                InterestTier tier = new InterestTier(
                        saved,
                        tierRequest.getBalanceFrom(),
                        tierRequest.getBalanceTo(),
                        tierRequest.getRate()
                );
                saved.addTier(tier);
            }
            saved = interestRateRepository.save(saved);
        }

        return buildResponse(saved);
    }

    public InterestRateResponse updateInterestRate(Long id, CreateInterestRateRequest request) {
        InterestRate interestRate = interestRateRepository.findById(id)
                .orElseThrow(() -> new InterestRateNotFoundException(id));

        if (request.getProductCode() != null) {
            interestRate.setProductCode(request.getProductCode());
        }
        if (request.getFixedRate() != null) {
            interestRate.setFixedRate(request.getFixedRate());
        }
        if (request.getAccrualSchedule() != null) {
            interestRate.setAccrualSchedule(InterestSchedule.valueOf(request.getAccrualSchedule()));
        }
        if (request.getApplicationSchedule() != null) {
            interestRate.setApplicationSchedule(InterestSchedule.valueOf(request.getApplicationSchedule()));
        }

        if (request.getTiers() != null) {
            interestTierRepository.deleteByInterestRateId(id);
            interestRate.getTiers().clear();

            for (InterestTierRequest tierRequest : request.getTiers()) {
                InterestTier tier = new InterestTier(
                        interestRate,
                        tierRequest.getBalanceFrom(),
                        tierRequest.getBalanceTo(),
                        tierRequest.getRate()
                );
                interestRate.addTier(tier);
            }
        }

        InterestRate saved = interestRateRepository.save(interestRate);
        return buildResponse(saved);
    }

    @Transactional(readOnly = true)
    public InterestRateResponse getInterestRate(Long id) {
        InterestRate interestRate = interestRateRepository.findById(id)
                .orElseThrow(() -> new InterestRateNotFoundException(id));
        return buildResponse(interestRate);
    }

    @Transactional(readOnly = true)
    public List<InterestRateResponse> getInterestRatesByProduct(String productCode) {
        List<InterestRate> rates = interestRateRepository.findByProductCode(productCode);
        return rates.stream()
                .map(this::buildResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateRateForBalance(InterestRate rate, BigDecimal balance) {
        if (rate.getFixedRate() != null) {
            return rate.getFixedRate();
        }

        List<InterestTier> tiers = interestTierRepository.findByInterestRateIdOrderByBalanceFrom(rate.getId());
        for (InterestTier tier : tiers) {
            if (balance.compareTo(tier.getBalanceFrom()) >= 0) {
                if (tier.getBalanceTo() == null || balance.compareTo(tier.getBalanceTo()) < 0) {
                    return tier.getRate();
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private InterestRateResponse buildResponse(InterestRate entity) {
        InterestRateResponse response = InterestRateResponse.fromEntity(entity);

        List<InterestTierResponse> tierResponses = new ArrayList<>();
        for (InterestTier tier : entity.getTiers()) {
            tierResponses.add(InterestTierResponse.fromEntity(tier));
        }
        response.setTiers(tierResponses);

        return response;
    }
}
