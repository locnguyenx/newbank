package com.banking.product.api;

import com.banking.product.api.dto.ProductDTO;
import com.banking.product.api.dto.ProductVersionDTO;
import java.util.List;
import java.util.Optional;

public interface ProductQueryService {
    ProductDTO findById(Long id);
    ProductDTO findByCode(String code);
    boolean existsById(Long id);
    List<ProductDTO> findByFamily(String family);
    List<ProductDTO> findActiveProducts();
    Optional<ProductVersionDTO> findActiveVersionByCode(String code);
}
