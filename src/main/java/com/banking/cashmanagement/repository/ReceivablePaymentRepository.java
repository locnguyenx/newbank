package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.ReceivablePayment;
import com.banking.cashmanagement.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivablePaymentRepository extends JpaRepository<ReceivablePayment, Long> {
    
    Optional<ReceivablePayment> findByPaymentReference(String paymentReference);
    
    List<ReceivablePayment> findByInvoiceId(Long invoiceId);
    
    List<ReceivablePayment> findByStatus(PaymentStatus status);
}
