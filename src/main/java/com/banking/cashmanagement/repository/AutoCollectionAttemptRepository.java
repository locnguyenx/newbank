package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.AutoCollectionAttempt;
import com.banking.cashmanagement.domain.enums.AutoCollectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AutoCollectionAttemptRepository extends JpaRepository<AutoCollectionAttempt, Long> {
    
    List<AutoCollectionAttempt> findByAutoCollectionRuleId(Long autoCollectionRuleId);
    
    List<AutoCollectionAttempt> findByInvoiceId(Long invoiceId);
    
    List<AutoCollectionAttempt> findByStatus(AutoCollectionStatus status);
}
