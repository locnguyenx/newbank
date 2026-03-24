package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.CashPoolingTransaction;
import com.banking.cashmanagement.domain.enums.CashPoolingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashPoolingTransactionRepository extends JpaRepository<CashPoolingTransaction, Long> {
    
    Optional<CashPoolingTransaction> findByPoolReference(String poolReference);
    
    List<CashPoolingTransaction> findByCustomerId(Long customerId);
    
    List<CashPoolingTransaction> findByCustomerIdAndStatus(Long customerId, CashPoolingStatus status);
}
