package com.banking.product.repository;

import com.banking.product.domain.entity.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    List<ProductFeature> findByProductVersionId(Long productVersionId);
    Optional<ProductFeature> findByProductVersionIdAndFeatureKey(Long productVersionId, String featureKey);
    void deleteByProductVersionId(Long productVersionId);
}
