package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.ChargeRule;
import com.banking.charges.domain.entity.ChargeTier;
import com.banking.charges.domain.enums.CalculationMethod;
import com.banking.charges.dto.request.CreateChargeRuleRequest;
import com.banking.charges.dto.request.TierRequest;
import com.banking.charges.dto.response.ChargeRuleResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InvalidCalculationMethodException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.ChargeRuleRepository;
import com.banking.charges.repository.ChargeTierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChargeRuleService {

    private final ChargeRuleRepository chargeRuleRepository;
    private final ChargeTierRepository chargeTierRepository;
    private final ChargeDefinitionRepository chargeDefinitionRepository;

    public ChargeRuleService(ChargeRuleRepository chargeRuleRepository,
                             ChargeTierRepository chargeTierRepository,
                             ChargeDefinitionRepository chargeDefinitionRepository) {
        this.chargeRuleRepository = chargeRuleRepository;
        this.chargeTierRepository = chargeTierRepository;
        this.chargeDefinitionRepository = chargeDefinitionRepository;
    }

    public ChargeRuleResponse addRule(Long chargeId, CreateChargeRuleRequest request) {
        ChargeDefinition chargeDefinition = chargeDefinitionRepository.findById(chargeId)
                .orElseThrow(() -> new ChargeNotFoundException(chargeId));

        CalculationMethod method = parseCalculationMethod(request.getCalculationMethod());

        ChargeRule rule = new ChargeRule(
                chargeDefinition,
                method,
                request.getFlatAmount(),
                request.getPercentageRate(),
                request.getMinAmount(),
                request.getMaxAmount()
        );

        if (request.getTiers() != null && !request.getTiers().isEmpty()) {
            List<TierRequest> sortedTiers = request.getTiers().stream()
                    .sorted((a, b) -> a.getTierFrom().compareTo(b.getTierFrom()))
                    .toList();
            for (TierRequest tierRequest : sortedTiers) {
                ChargeTier tier = new ChargeTier(rule, tierRequest.getTierFrom(), tierRequest.getTierTo(), tierRequest.getRate());
                rule.addTier(tier);
            }
        }

        ChargeRule saved = chargeRuleRepository.save(rule);
        return ChargeRuleResponse.fromEntity(saved);
    }

    public ChargeRuleResponse updateRule(Long ruleId, CreateChargeRuleRequest request) {
        ChargeRule rule = chargeRuleRepository.findById(ruleId)
                .orElseThrow(() -> new RuleNotFoundException(ruleId));

        CalculationMethod method = parseCalculationMethod(request.getCalculationMethod());
        rule.setMethod(method);
        rule.setFlatAmount(request.getFlatAmount());
        rule.setPercentageRate(request.getPercentageRate());
        rule.setMinAmount(request.getMinAmount());
        rule.setMaxAmount(request.getMaxAmount());

        rule.getTiers().clear();

        if (request.getTiers() != null && !request.getTiers().isEmpty()) {
            List<TierRequest> sortedTiers = request.getTiers().stream()
                    .sorted((a, b) -> a.getTierFrom().compareTo(b.getTierFrom()))
                    .toList();
            for (TierRequest tierRequest : sortedTiers) {
                ChargeTier tier = new ChargeTier(rule, tierRequest.getTierFrom(), tierRequest.getTierTo(), tierRequest.getRate());
                rule.addTier(tier);
            }
        }

        ChargeRule saved = chargeRuleRepository.save(rule);
        return ChargeRuleResponse.fromEntity(saved);
    }

    public void removeRule(Long ruleId) {
        if (!chargeRuleRepository.existsById(ruleId)) {
            throw new RuleNotFoundException(ruleId);
        }
        chargeRuleRepository.deleteById(ruleId);
    }

    @Transactional(readOnly = true)
    public List<ChargeRuleResponse> getRules(Long chargeId) {
        return chargeRuleRepository.findByChargeDefinitionId(chargeId).stream()
                .map(ChargeRuleResponse::fromEntity)
                .toList();
    }

    private CalculationMethod parseCalculationMethod(String methodStr) {
        try {
            return CalculationMethod.valueOf(methodStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCalculationMethodException(methodStr);
        }
    }

    public static class RuleNotFoundException extends RuntimeException {
        public static final String ERROR_CODE = "CHRG-002";

        public RuleNotFoundException(Long id) {
            super("Rule not found: " + id);
        }

        public String getErrorCode() {
            return ERROR_CODE;
        }
    }
}
