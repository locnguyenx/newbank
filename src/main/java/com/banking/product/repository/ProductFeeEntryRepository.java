package com.banking.product.repository;

import com.banking.product.domain.entity.ProductFeeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFeeEntryRepository extends JpaRepository<ProductFeeEntry, Long> {
    List<ProductFeeEntry> findByProductVersionId(Long productVersionId);
    void deleteByProductVersionId(Long productVersionId);
}
