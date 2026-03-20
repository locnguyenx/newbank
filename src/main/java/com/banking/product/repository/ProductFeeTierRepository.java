package com.banking.product.repository;

import com.banking.product.domain.entity.ProductFeeTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFeeTierRepository extends JpaRepository<ProductFeeTier, Long> {
    List<ProductFeeTier> findByFeeEntryId(Long feeEntryId);
    void deleteByFeeEntryId(Long feeEntryId);
}
