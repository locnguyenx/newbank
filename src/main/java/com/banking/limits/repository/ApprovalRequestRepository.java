package com.banking.limits.repository;

import com.banking.limits.domain.entity.ApprovalRequest;
import com.banking.limits.domain.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    Page<ApprovalRequest> findByStatus(ApprovalStatus status, Pageable pageable);
}
