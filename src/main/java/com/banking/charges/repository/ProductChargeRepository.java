package com.banking.charges.repository;

import com.banking.charges.domain.entity.ProductCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductChargeRepository extends JpaRepository<ProductCharge, Long> {
    List<ProductCharge> findByProductCode(String productCode);
    Optional<ProductCharge> findByChargeDefinitionIdAndProductCode(Long chargeDefinitionId, String productCode);
    void deleteByChargeDefinitionIdAndProductCode(Long chargeDefinitionId, String productCode);
}
