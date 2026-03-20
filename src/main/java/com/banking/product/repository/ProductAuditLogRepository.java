package com.banking.product.repository;

import com.banking.product.domain.entity.ProductAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAuditLogRepository extends JpaRepository<ProductAuditLog, Long> {
    List<ProductAuditLog> findByProductVersionIdOrderByTimestampDesc(Long productVersionId);
}
