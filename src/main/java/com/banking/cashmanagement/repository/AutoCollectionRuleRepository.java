package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.AutoCollectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AutoCollectionRuleRepository extends JpaRepository<AutoCollectionRule, Long> {
    
    List<AutoCollectionRule> findByCustomerId(Long customerId);
    
    List<AutoCollectionRule> findByCustomerIdAndIsActiveTrue(Long customerId);
    
    Optional<AutoCollectionRule> findByIdAndIsActiveTrue(Long id);
}
