package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.BatchPaymentInstruction;
import com.banking.cashmanagement.domain.enums.PaymentInstructionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BatchPaymentInstructionRepository extends JpaRepository<BatchPaymentInstruction, Long> {
    
    List<BatchPaymentInstruction> findByBatchPaymentId(Long batchPaymentId);
    
    List<BatchPaymentInstruction> findByBatchPaymentIdAndStatus(Long batchPaymentId, PaymentInstructionStatus status);
    
    List<BatchPaymentInstruction> findByStatus(PaymentInstructionStatus status);
}
