package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.CashManagementAudit;
import com.banking.cashmanagement.domain.enums.CashManagementEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashManagementAuditRepository extends JpaRepository<CashManagementAudit, Long> {
    
    List<CashManagementAudit> findByCustomerId(Long customerId);
    
    List<CashManagementAudit> findByCustomerIdAndEventType(Long customerId, CashManagementEventType eventType);
    
    List<CashManagementAudit> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<CashManagementAudit> findByCustomerIdAndEventTimestampBetween(Long customerId, LocalDateTime start, LocalDateTime end);
}
