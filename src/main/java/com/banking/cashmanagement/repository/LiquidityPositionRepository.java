package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.LiquidityPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LiquidityPositionRepository extends JpaRepository<LiquidityPosition, Long> {
    
    List<LiquidityPosition> findByCustomerId(Long customerId);
    
    Optional<LiquidityPosition> findFirstByCustomerIdOrderByCalculationDateTimeDesc(Long customerId);
    
    List<LiquidityPosition> findByCustomerIdAndCurrency(Long customerId, String currency);
}
