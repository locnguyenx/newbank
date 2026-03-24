package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.PayrollBatch;
import com.banking.cashmanagement.domain.enums.PayrollBatchStatus;
import com.banking.cashmanagement.dto.PayrollBatchRequest;
import com.banking.cashmanagement.dto.PayrollBatchResponse;
import com.banking.cashmanagement.exception.PayrollBatchNotFoundException;
import com.banking.cashmanagement.repository.PayrollBatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PayrollService {
    
    private final PayrollBatchRepository payrollBatchRepository;
    
    public PayrollService(PayrollBatchRepository payrollBatchRepository) {
        this.payrollBatchRepository = payrollBatchRepository;
    }
    
    public PayrollBatchResponse createPayrollBatch(PayrollBatchRequest request) {
        PayrollBatch batch = new PayrollBatch();
        batch.setBatchReference(generateBatchReference());
        batch.setCustomerId(request.getCustomerId());
        batch.setStatus(PayrollBatchStatus.PENDING);
        batch.setFileFormat(request.getFileFormat());
        batch.setRecordCount(request.getRecordCount());
        batch.setTotalAmount(request.getTotalAmount());
        batch.setCurrency(request.getCurrency());
        batch.setPaymentDate(request.getPaymentDate());
        
        PayrollBatch saved = payrollBatchRepository.save(batch);
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public PayrollBatchResponse getPayrollBatch(Long id) {
        PayrollBatch batch = payrollBatchRepository.findById(id)
            .orElseThrow(() -> new PayrollBatchNotFoundException(id));
        return mapToResponse(batch);
    }
    
    @Transactional(readOnly = true)
    public List<PayrollBatchResponse> listPayrollBatches(Long customerId) {
        List<PayrollBatch> batches;
        if (customerId != null) {
            batches = payrollBatchRepository.findByCustomerId(customerId);
        } else {
            batches = payrollBatchRepository.findAll();
        }
        return batches.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private String generateBatchReference() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "PAY-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private PayrollBatchResponse mapToResponse(PayrollBatch batch) {
        PayrollBatchResponse response = new PayrollBatchResponse();
        response.setId(batch.getId());
        response.setBatchReference(batch.getBatchReference());
        response.setCustomerId(batch.getCustomerId());
        response.setStatus(batch.getStatus());
        response.setFileFormat(batch.getFileFormat());
        response.setRecordCount(batch.getRecordCount());
        response.setProcessedCount(batch.getProcessedCount());
        response.setErrorCount(batch.getErrorCount());
        response.setTotalAmount(batch.getTotalAmount());
        response.setCurrency(batch.getCurrency());
        response.setPaymentDate(batch.getPaymentDate());
        return response;
    }
}
