package com.banking.product.repository;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.enums.ProductFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    List<Product> findByFamily(ProductFamily family);
}
