package com.banking.limits.service;

import com.banking.limits.domain.entity.ApprovalRequest;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.ApprovalStatus;
import com.banking.limits.dto.response.ApprovalRequestResponse;
import com.banking.limits.exception.ApprovalRequestNotFoundException;
import com.banking.limits.exception.InvalidApprovalActionException;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.repository.ApprovalRequestRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ApprovalService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final LimitDefinitionRepository limitDefinitionRepository;

    public ApprovalService(ApprovalRequestRepository approvalRequestRepository,
                           LimitDefinitionRepository limitDefinitionRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.limitDefinitionRepository = limitDefinitionRepository;
    }

    public ApprovalRequestResponse createApprovalRequest(String txnRef, Long limitId,
                                                          BigDecimal amount, String currency,
                                                          String accountNumber) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));

        ApprovalRequest request = new ApprovalRequest(limitDefinition, txnRef, amount, currency, accountNumber);
        ApprovalRequest saved = approvalRequestRepository.save(request);
        return ApprovalRequestResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ApprovalRequestResponse> getPendingApprovals(Pageable pageable) {
        Page<ApprovalRequest> page = approvalRequestRepository.findByStatus(ApprovalStatus.PENDING, pageable);
        return page.getContent().stream()
                .map(ApprovalRequestResponse::fromEntity)
                .toList();
    }

    public void approve(Long requestId, String username) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApprovalRequestNotFoundException(requestId));

        if (request.getStatus() != ApprovalStatus.PENDING) {
            throw new InvalidApprovalActionException(
                    "Cannot approve request in " + request.getStatus() + " status");
        }

        request.setStatus(ApprovalStatus.APPROVED);
        request.setApprovedBy(username);
        request.setDecisionAt(LocalDateTime.now());
        approvalRequestRepository.save(request);
    }

    public void reject(Long requestId, String username, String reason) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApprovalRequestNotFoundException(requestId));

        if (request.getStatus() != ApprovalStatus.PENDING) {
            throw new InvalidApprovalActionException(
                    "Cannot reject request in " + request.getStatus() + " status");
        }

        request.setStatus(ApprovalStatus.REJECTED);
        request.setApprovedBy(username);
        request.setRejectionReason(reason);
        request.setDecisionAt(LocalDateTime.now());
        approvalRequestRepository.save(request);
    }
}
