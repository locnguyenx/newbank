package com.banking.limits.repository;

import com.banking.limits.domain.entity.ProductLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLimitRepository extends JpaRepository<ProductLimit, Long> {
    List<ProductLimit> findByProductCode(String productCode);
}
