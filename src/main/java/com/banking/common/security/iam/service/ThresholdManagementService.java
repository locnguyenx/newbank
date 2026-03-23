package com.banking.common.security.iam.service;

import com.banking.common.message.MessageCatalog;
import com.banking.common.security.rbac.AmountThreshold;
import com.banking.common.security.rbac.AmountThresholdRepository;
import com.banking.common.security.rbac.Role;
import com.banking.common.security.iam.dto.ThresholdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThresholdManagementService {

    private final AmountThresholdRepository thresholdRepository;

    public ThresholdManagementService(AmountThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    public AmountThreshold setThreshold(ThresholdRequest request) {
        Optional<AmountThreshold> existing = thresholdRepository.findAll().stream()
                .filter(t -> t.getUserId().equals(request.getUserId()) && t.getRole() == request.getRole())
                .findFirst();
        
        if (existing.isPresent()) {
            AmountThreshold threshold = existing.get();
            threshold.setThreshold(request.getThreshold());
            return thresholdRepository.save(threshold);
        } else {
            AmountThreshold threshold = new AmountThreshold(
                    request.getUserId(),
                    request.getRole(),
                    request.getThreshold()
            );
            return thresholdRepository.save(threshold);
        }
    }

    public AmountThreshold updateThreshold(Long id, BigDecimal threshold) {
        AmountThreshold existing = thresholdRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_THRESHOLD_NOT_FOUND)));
        
        existing.setThreshold(threshold);
        return thresholdRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public AmountThreshold getThreshold(Long id) {
        return thresholdRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_THRESHOLD_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    public List<AmountThreshold> getUserThresholds(Long userId) {
        return thresholdRepository.findAll().stream()
                .filter(t -> t.getUserId().equals(userId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AmountThreshold> listThresholds() {
        return thresholdRepository.findAll();
    }

    public void deleteThreshold(Long id) {
        thresholdRepository.deleteById(id);
    }
}