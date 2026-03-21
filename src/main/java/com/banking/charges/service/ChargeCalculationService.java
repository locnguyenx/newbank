package com.banking.charges.service;

import com.banking.charges.domain.entity.*;
import com.banking.charges.domain.enums.CalculationMethod;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.ChargeCalculationRequest;
import com.banking.charges.dto.response.ChargeCalculationResponse;
import com.banking.charges.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChargeCalculationService {

    private final CustomerChargeOverrideRepository customerOverrideRepository;
    private final ProductChargeRepository productChargeRepository;
    private final ChargeRuleRepository chargeRuleRepository;
    private final ChargeTierRepository chargeTierRepository;
    private final FeeWaiverRepository feeWaiverRepository;
    private final ChargeDefinitionRepository chargeDefinitionRepository;
    private final InterestRateService interestRateService;

    public ChargeCalculationService(CustomerChargeOverrideRepository customerOverrideRepository,
                                   ProductChargeRepository productChargeRepository,
                                   ChargeRuleRepository chargeRuleRepository,
                                   ChargeTierRepository chargeTierRepository,
                                   FeeWaiverRepository feeWaiverRepository,
                                   ChargeDefinitionRepository chargeDefinitionRepository,
                                   InterestRateService interestRateService) {
        this.customerOverrideRepository = customerOverrideRepository;
        this.productChargeRepository = productChargeRepository;
        this.chargeRuleRepository = chargeRuleRepository;
        this.chargeTierRepository = chargeTierRepository;
        this.feeWaiverRepository = feeWaiverRepository;
        this.chargeDefinitionRepository = chargeDefinitionRepository;
        this.interestRateService = interestRateService;
    }

    public ChargeCalculationResponse calculate(ChargeCalculationRequest request) {
        try {
            ChargeType chargeType;
            try {
                chargeType = ChargeType.valueOf(request.getChargeType());
            } catch (IllegalArgumentException e) {
                return ChargeCalculationResponse.noCharge();
            }

            ChargeDefinition chargeDefinition = chargeDefinitionRepository.findByChargeType(chargeType)
                    .stream()
                    .filter(cd -> cd.getStatus() == ChargeStatus.ACTIVE)
                    .findFirst()
                    .orElse(null);

            if (chargeDefinition == null) {
                return ChargeCalculationResponse.noCharge();
            }

            BigDecimal baseAmount = calculateBaseAmount(chargeDefinition, request);
            if (baseAmount == null) {
                return ChargeCalculationResponse.noCharge();
            }

            baseAmount = baseAmount.setScale(4, RoundingMode.HALF_UP);

            LocalDate today = LocalDate.now();
            FeeWaiver applicableWaiver = feeWaiverRepository.findByChargeDefinitionId(chargeDefinition.getId())
                    .stream()
                    .filter(w -> w.isActiveOn(today))
                    .findFirst()
                    .orElse(null);

            BigDecimal waiverAmount = BigDecimal.ZERO;
            String waiverId = null;
            boolean waiverApplied = false;

            if (applicableWaiver != null) {
                waiverAmount = baseAmount.multiply(BigDecimal.valueOf(applicableWaiver.getWaiverPercentage()))
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                waiverAmount = waiverAmount.setScale(4, RoundingMode.HALF_UP);
                waiverId = applicableWaiver.getId().toString();
                waiverApplied = true;
            }

            BigDecimal finalAmount = baseAmount.subtract(waiverAmount).setScale(4, RoundingMode.HALF_UP);

            ChargeCalculationResponse response = new ChargeCalculationResponse();
            response.setBaseAmount(baseAmount);
            response.setWaiverAmount(waiverAmount);
            response.setFinalAmount(finalAmount);
            response.setWaiverApplied(waiverApplied);
            response.setWaiverId(waiverId);
            response.setRuleApplied(chargeDefinition.getName());

            return response;
        } catch (IllegalArgumentException e) {
            return ChargeCalculationResponse.noCharge();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during charge calculation", e);
        }
    }

    private BigDecimal calculateBaseAmount(ChargeDefinition chargeDefinition, ChargeCalculationRequest request) {
        if (request.getCustomerId() != null) {
            var override = customerOverrideRepository.findByChargeDefinitionIdAndCustomerId(chargeDefinition.getId(), request.getCustomerId());
            if (override.isPresent()) {
                return override.get().getOverrideAmount();
            }
        }

        if (request.getProductCode() != null) {
            var pc = productChargeRepository.findByChargeDefinitionIdAndProductCode(chargeDefinition.getId(), request.getProductCode());
            if (pc.isPresent()) {
                return pc.get().getOverrideAmount();
            }
        }

        List<ChargeRule> rules = chargeRuleRepository.findByChargeDefinitionId(chargeDefinition.getId());
        if (rules.isEmpty()) {
            return null;
        }

        ChargeRule rule = rules.get(0);
        CalculationMethod method = rule.getMethod();

        return switch (method) {
            case FLAT -> rule.getFlatAmount();
            case PERCENTAGE -> {
                if (request.getTransactionAmount() == null) {
                    yield BigDecimal.ZERO;
                }
                BigDecimal amount = request.getTransactionAmount()
                        .multiply(rule.getPercentageRate())
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

                BigDecimal minAmount = rule.getMinAmount();
                BigDecimal maxAmount = rule.getMaxAmount();

                if (minAmount != null && amount.compareTo(minAmount) < 0) {
                    amount = minAmount;
                }
                if (maxAmount != null && amount.compareTo(maxAmount) > 0) {
                    amount = maxAmount;
                }
                yield amount;
            }
            case TIERED_VOLUME -> {
                if (request.getTransactionCount() == null) {
                    yield BigDecimal.ZERO;
                }
                List<ChargeTier> tiers = chargeTierRepository.findByChargeRuleId(rule.getId());
                BigDecimal applicableRate = BigDecimal.ZERO;
                for (ChargeTier tier : tiers) {
                    if (request.getTransactionCount() >= tier.getTierFrom() &&
                        (tier.getTierTo() == null || request.getTransactionCount() < tier.getTierTo())) {
                        applicableRate = tier.getRate();
                        break;
                    }
                }
                yield applicableRate.multiply(BigDecimal.valueOf(request.getTransactionCount()));
            }
        };
    }
}
