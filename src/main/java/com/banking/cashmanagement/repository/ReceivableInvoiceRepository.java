package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.ReceivableInvoice;
import com.banking.cashmanagement.domain.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivableInvoiceRepository extends JpaRepository<ReceivableInvoice, Long> {
    
    Optional<ReceivableInvoice> findByInvoiceNumber(String invoiceNumber);
    
    List<ReceivableInvoice> findByCustomerId(Long customerId);
    
    List<ReceivableInvoice> findByCustomerIdAndStatus(Long customerId, InvoiceStatus status);
    
    List<ReceivableInvoice> findByBillToCustomerId(Long billToCustomerId);
    
    List<ReceivableInvoice> findByStatus(InvoiceStatus status);
}
