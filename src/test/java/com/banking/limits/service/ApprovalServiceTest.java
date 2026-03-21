package com.banking.limits.service;

import com.banking.limits.domain.entity.ApprovalRequest;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.ApprovalStatus;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.response.ApprovalRequestResponse;
import com.banking.limits.exception.ApprovalRequestNotFoundException;
import com.banking.limits.exception.InvalidApprovalActionException;
import com.banking.limits.repository.ApprovalRequestRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

    @Mock
    private ApprovalRequestRepository approvalRequestRepository;

    @Mock
    private LimitDefinitionRepository limitDefinitionRepository;

    private ApprovalService approvalService;

    private LimitDefinition limitDefinition;

    @BeforeEach
    void setUp() {
        approvalService = new ApprovalService(approvalRequestRepository, limitDefinitionRepository);
        limitDefinition = new LimitDefinition("Daily Transfer", LimitType.DAILY,
                new BigDecimal("50000.00"), "USD");
    }

    @Test
    void approve_success() {
        ApprovalRequest request = new ApprovalRequest(limitDefinition, "TXN-001",
                new BigDecimal("10000.00"), "USD", "1234567890");

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(approvalRequestRepository.save(any())).thenReturn(request);

        approvalService.approve(1L, "admin");

        assertEquals(ApprovalStatus.APPROVED, request.getStatus());
        assertEquals("admin", request.getApprovedBy());
        assertNotNull(request.getDecisionAt());

        verify(approvalRequestRepository).save(any());
    }

    @Test
    void reject_success() {
        ApprovalRequest request = new ApprovalRequest(limitDefinition, "TXN-002",
                new BigDecimal("15000.00"), "USD", "9876543210");

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(approvalRequestRepository.save(any())).thenReturn(request);

        approvalService.reject(1L, "admin", "Exceeds daily limit");

        assertEquals(ApprovalStatus.REJECTED, request.getStatus());
        assertEquals("admin", request.getApprovedBy());
        assertEquals("Exceeds daily limit", request.getRejectionReason());
        assertNotNull(request.getDecisionAt());

        verify(approvalRequestRepository).save(any());
    }

    @Test
    void approve_alreadyDecided_throws() {
        ApprovalRequest request = new ApprovalRequest(limitDefinition, "TXN-003",
                new BigDecimal("5000.00"), "USD", "1111111111");
        request.setStatus(ApprovalStatus.APPROVED);

        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(InvalidApprovalActionException.class, () ->
                approvalService.approve(1L, "admin"));

        verify(approvalRequestRepository, never()).save(any());
    }

    @Test
    void approve_notFound_throws() {
        when(approvalRequestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ApprovalRequestNotFoundException.class, () ->
                approvalService.approve(999L, "admin"));

        verify(approvalRequestRepository, never()).save(any());
    }

    @Test
    void getPendingApprovals_success() {
        ApprovalRequest request1 = new ApprovalRequest(limitDefinition, "TXN-001",
                new BigDecimal("10000.00"), "USD", "1234567890");
        Page<ApprovalRequest> page = new PageImpl<>(List.of(request1));

        when(approvalRequestRepository.findByStatus(eq(ApprovalStatus.PENDING), any(PageRequest.class)))
                .thenReturn(page);

        List<ApprovalRequestResponse> result = approvalService.getPendingApprovals(PageRequest.of(0, 10));

        assertEquals(1, result.size());
        assertEquals("TXN-001", result.get(0).getTransactionReference());
    }
}
