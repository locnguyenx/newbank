package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.FeeWaiver;
import com.banking.charges.domain.enums.WaiverScope;
import com.banking.charges.dto.request.CreateFeeWaiverRequest;
import com.banking.charges.dto.response.FeeWaiverResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.WaiverAlreadyExistsException;
import com.banking.charges.exception.WaiverNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.FeeWaiverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FeeWaiverService {

    private final FeeWaiverRepository feeWaiverRepository;
    private final ChargeDefinitionRepository chargeDefinitionRepository;

    public FeeWaiverService(FeeWaiverRepository feeWaiverRepository,
                            ChargeDefinitionRepository chargeDefinitionRepository) {
        this.feeWaiverRepository = feeWaiverRepository;
        this.chargeDefinitionRepository = chargeDefinitionRepository;
    }

    public FeeWaiverResponse createWaiver(CreateFeeWaiverRequest request) {
        ChargeDefinition chargeDefinition = chargeDefinitionRepository.findById(request.getChargeId())
                .orElseThrow(() -> new ChargeNotFoundException(request.getChargeId()));

        WaiverScope scope = WaiverScope.valueOf(request.getScope().toUpperCase());

        if (feeWaiverRepository.findByChargeDefinitionIdAndScopeAndReferenceId(
                request.getChargeId(), scope, request.getReferenceId()).isPresent()) {
            throw new WaiverAlreadyExistsException(scope.name(), request.getReferenceId());
        }

        FeeWaiver waiver = new FeeWaiver(
                chargeDefinition,
                scope,
                request.getReferenceId(),
                request.getWaiverPercentage(),
                request.getValidFrom(),
                request.getValidTo()
        );

        FeeWaiver saved = feeWaiverRepository.save(waiver);
        return FeeWaiverResponse.fromEntity(saved);
    }

    public void removeWaiver(Long waiverId) {
        if (!feeWaiverRepository.existsById(waiverId)) {
            throw new WaiverNotFoundException(waiverId);
        }
        feeWaiverRepository.deleteById(waiverId);
    }

    @Transactional(readOnly = true)
    public List<FeeWaiverResponse> getWaivers(String scope, String referenceId) {
        if (scope != null && referenceId != null) {
            WaiverScope waiverScope = WaiverScope.valueOf(scope.toUpperCase());
            return feeWaiverRepository.findByScopeAndReferenceId(waiverScope, referenceId).stream()
                    .map(FeeWaiverResponse::fromEntity)
                    .toList();
        } else if (scope != null) {
            WaiverScope waiverScope = WaiverScope.valueOf(scope.toUpperCase());
            return feeWaiverRepository.findAll().stream()
                    .filter(w -> w.getScope() == waiverScope)
                    .map(FeeWaiverResponse::fromEntity)
                    .toList();
        } else if (referenceId != null) {
            return feeWaiverRepository.findAll().stream()
                    .filter(w -> w.getReferenceId().equals(referenceId))
                    .map(FeeWaiverResponse::fromEntity)
                    .toList();
        }
        return feeWaiverRepository.findAll().stream()
                .map(FeeWaiverResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FeeWaiverResponse> getApplicableWaivers(Long chargeId, Long customerId,
                                                          String accountNumber, String productCode,
                                                          LocalDate date) {
        return feeWaiverRepository.findByChargeDefinitionId(chargeId).stream()
                .filter(waiver -> waiver.isActiveOn(date))
                .filter(waiver -> {
                    switch (waiver.getScope()) {
                        case CUSTOMER:
                            return customerId != null && String.valueOf(customerId).equals(waiver.getReferenceId());
                        case ACCOUNT:
                            return accountNumber != null && accountNumber.equals(waiver.getReferenceId());
                        case PRODUCT:
                            return productCode != null && productCode.equals(waiver.getReferenceId());
                        default:
                            return false;
                    }
                })
                .map(FeeWaiverResponse::fromEntity)
                .toList();
    }
}
