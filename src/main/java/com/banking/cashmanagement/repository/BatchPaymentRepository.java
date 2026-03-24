package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.BatchPayment;
import com.banking.cashmanagement.domain.enums.BatchPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchPaymentRepository extends JpaRepository<BatchPayment, Long> {
    
    Optional<BatchPayment> findByBatchReference(String batchReference);
    
    List<BatchPayment> findByCustomerId(Long customerId);
    
    List<BatchPayment> findByStatus(BatchPaymentStatus status);
    
    List<BatchPayment> findByCustomerIdAndStatus(Long customerId, BatchPaymentStatus status);
}
