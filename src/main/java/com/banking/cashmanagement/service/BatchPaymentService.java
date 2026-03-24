package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.BatchPayment;
import com.banking.cashmanagement.domain.enums.BatchPaymentStatus;
import com.banking.cashmanagement.dto.BatchPaymentRequest;
import com.banking.cashmanagement.dto.BatchPaymentResponse;
import com.banking.cashmanagement.repository.BatchPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BatchPaymentService {
    
    private final BatchPaymentRepository batchPaymentRepository;
    
    public BatchPaymentService(BatchPaymentRepository batchPaymentRepository) {
        this.batchPaymentRepository = batchPaymentRepository;
    }
    
    public BatchPaymentResponse createBatchPayment(BatchPaymentRequest request) {
        BatchPayment batch = new BatchPayment();
        batch.setBatchReference(generateBatchReference());
        batch.setCustomerId(request.getCustomerId());
        batch.setStatus(BatchPaymentStatus.PENDING);
        batch.setFileFormat(request.getFileFormat());
        batch.setInstructionCount(request.getInstructionCount());
        batch.setTotalAmount(request.getTotalAmount());
        batch.setCurrency(request.getCurrency());
        batch.setPaymentDate(request.getPaymentDate());
        
        BatchPayment saved = batchPaymentRepository.save(batch);
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public BatchPaymentResponse getBatchPayment(Long id) {
        BatchPayment batch = batchPaymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Batch payment not found: " + id));
        return mapToResponse(batch);
    }
    
    @Transactional(readOnly = true)
    public List<BatchPaymentResponse> listBatchPayments(Long customerId) {
        List<BatchPayment> batches;
        if (customerId != null) {
            batches = batchPaymentRepository.findByCustomerId(customerId);
        } else {
            batches = batchPaymentRepository.findAll();
        }
        return batches.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private String generateBatchReference() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "BATCH-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private BatchPaymentResponse mapToResponse(BatchPayment batch) {
        BatchPaymentResponse response = new BatchPaymentResponse();
        response.setId(batch.getId());
        response.setBatchReference(batch.getBatchReference());
        response.setCustomerId(batch.getCustomerId());
        response.setStatus(batch.getStatus());
        response.setFileFormat(batch.getFileFormat());
        response.setInstructionCount(batch.getInstructionCount());
        response.setProcessedCount(batch.getProcessedCount());
        response.setErrorCount(batch.getErrorCount());
        response.setTotalAmount(batch.getTotalAmount());
        response.setCurrency(batch.getCurrency());
        response.setPaymentDate(batch.getPaymentDate());
        return response;
    }
}
