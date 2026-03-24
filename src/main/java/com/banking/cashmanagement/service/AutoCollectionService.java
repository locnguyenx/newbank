package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.AutoCollectionRule;
import com.banking.cashmanagement.dto.AutoCollectionRuleRequest;
import com.banking.cashmanagement.dto.AutoCollectionRuleResponse;
import com.banking.cashmanagement.repository.AutoCollectionRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AutoCollectionService {
    
    private final AutoCollectionRuleRepository autoCollectionRuleRepository;
    
    public AutoCollectionService(AutoCollectionRuleRepository autoCollectionRuleRepository) {
        this.autoCollectionRuleRepository = autoCollectionRuleRepository;
    }
    
    public AutoCollectionRuleResponse createRule(AutoCollectionRuleRequest request) {
        AutoCollectionRule rule = new AutoCollectionRule();
        rule.setCustomerId(request.getCustomerId());
        rule.setRuleName(request.getRuleName());
        rule.setDescription(request.getDescription());
        rule.setTriggerCondition(request.getTriggerCondition());
        rule.setCollectionAmountType(request.getCollectionAmountType());
        rule.setCollectionAmountValue(request.getCollectionAmountValue());
        rule.setCurrency(request.getCurrency());
        rule.setFundingAccountId(request.getFundingAccountId());
        rule.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        rule.setPreNotificationDays(request.getPreNotificationDays());
        rule.setRetryAttempts(request.getRetryAttempts());
        rule.setRetryIntervalHours(request.getRetryIntervalHours());
        
        AutoCollectionRule saved = autoCollectionRuleRepository.save(rule);
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public AutoCollectionRuleResponse getRule(Long id) {
        AutoCollectionRule rule = autoCollectionRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto collection rule not found: " + id));
        return mapToResponse(rule);
    }
    
    @Transactional(readOnly = true)
    public List<AutoCollectionRuleResponse> listRules(Long customerId) {
        List<AutoCollectionRule> rules;
        if (customerId != null) {
            rules = autoCollectionRuleRepository.findByCustomerId(customerId);
        } else {
            rules = autoCollectionRuleRepository.findAll();
        }
        return rules.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public AutoCollectionRuleResponse activateRule(Long id) {
        AutoCollectionRule rule = autoCollectionRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto collection rule not found: " + id));
        rule.setIsActive(true);
        AutoCollectionRule saved = autoCollectionRuleRepository.save(rule);
        return mapToResponse(saved);
    }
    
    public AutoCollectionRuleResponse deactivateRule(Long id) {
        AutoCollectionRule rule = autoCollectionRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto collection rule not found: " + id));
        rule.setIsActive(false);
        AutoCollectionRule saved = autoCollectionRuleRepository.save(rule);
        return mapToResponse(saved);
    }
    
    private AutoCollectionRuleResponse mapToResponse(AutoCollectionRule rule) {
        AutoCollectionRuleResponse response = new AutoCollectionRuleResponse();
        response.setId(rule.getId());
        response.setCustomerId(rule.getCustomerId());
        response.setRuleName(rule.getRuleName());
        response.setDescription(rule.getDescription());
        response.setTriggerCondition(rule.getTriggerCondition());
        response.setCollectionAmountType(rule.getCollectionAmountType());
        response.setCollectionAmountValue(rule.getCollectionAmountValue());
        response.setCurrency(rule.getCurrency());
        response.setFundingAccountId(rule.getFundingAccountId());
        response.setIsActive(rule.getIsActive());
        response.setPreNotificationDays(rule.getPreNotificationDays());
        response.setRetryAttempts(rule.getRetryAttempts());
        response.setRetryIntervalHours(rule.getRetryIntervalHours());
        return response;
    }
}
