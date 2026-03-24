package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.PayrollBatch;
import com.banking.cashmanagement.domain.enums.PayrollBatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollBatchRepository extends JpaRepository<PayrollBatch, Long> {
    
    Optional<PayrollBatch> findByBatchReference(String batchReference);
    
    List<PayrollBatch> findByCustomerId(Long customerId);
    
    List<PayrollBatch> findByStatus(PayrollBatchStatus status);
    
    List<PayrollBatch> findByCustomerIdAndStatus(Long customerId, PayrollBatchStatus status);
}
