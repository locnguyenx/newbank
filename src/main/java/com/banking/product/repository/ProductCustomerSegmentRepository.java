package com.banking.product.repository;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.product.domain.entity.ProductCustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCustomerSegmentRepository extends JpaRepository<ProductCustomerSegment, Long> {
    List<ProductCustomerSegment> findByProductVersionId(Long productVersionId);
    Optional<ProductCustomerSegment> findByProductVersionIdAndCustomerType(Long productVersionId, CustomerType customerType);
    void deleteByProductVersionId(Long productVersionId);
}
