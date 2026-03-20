package com.banking.product.service;

import com.banking.product.domain.entity.ProductAuditLog;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductAuditLogResponse;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductAuditLogRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAuditService {

    private final ProductAuditLogRepository productAuditLogRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ProductMapper productMapper;

    public ProductAuditService(ProductAuditLogRepository productAuditLogRepository,
                               ProductVersionRepository productVersionRepository,
                               ProductMapper productMapper) {
        this.productAuditLogRepository = productAuditLogRepository;
        this.productVersionRepository = productVersionRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public void log(Long versionId, AuditAction action, String actor,
                    ProductStatus fromStatus, ProductStatus toStatus,
                    String comment, String makerUsername) {
        ProductVersion version = productVersionRepository.getReferenceById(versionId);
        ProductAuditLog auditLog = new ProductAuditLog(
                version, action, actor, fromStatus, toStatus, comment, makerUsername
        );
        productAuditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<ProductAuditLogResponse> getAuditTrail(Long versionId) {
        return productAuditLogRepository.findByProductVersionIdOrderByTimestampDesc(versionId)
                .stream()
                .map(productMapper::toAuditLogResponse)
                .collect(Collectors.toList());
    }
}
