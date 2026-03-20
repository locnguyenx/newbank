package com.banking.product.repository;

import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVersionRepository extends JpaRepository<ProductVersion, Long> {
    List<ProductVersion> findByProductIdOrderByVersionNumberDesc(Long productId);
    Optional<ProductVersion> findByProductIdAndVersionNumber(Long productId, Integer versionNumber);
    Optional<ProductVersion> findByProductIdAndStatus(Long productId, ProductStatus status);
    @Query("SELECT pv FROM ProductVersion pv WHERE pv.product.code = :code AND pv.status = :status")
    List<ProductVersion> findByProductCodeAndStatus(@Param("code") String code, @Param("status") ProductStatus status);
    Optional<ProductVersion> findTopByProductIdOrderByVersionNumberDesc(Long productId);
    List<ProductVersion> findByStatus(ProductStatus status);
}
