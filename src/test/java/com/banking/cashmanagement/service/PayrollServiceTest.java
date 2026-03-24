package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.PayrollBatch;
import com.banking.cashmanagement.domain.enums.PayrollBatchStatus;
import com.banking.cashmanagement.domain.enums.PayrollFileFormat;
import com.banking.cashmanagement.dto.PayrollBatchRequest;
import com.banking.cashmanagement.dto.PayrollBatchResponse;
import com.banking.cashmanagement.exception.PayrollBatchNotFoundException;
import com.banking.cashmanagement.integration.AccountServiceAdapter;
import com.banking.cashmanagement.integration.CustomerServiceAdapter;
import com.banking.cashmanagement.repository.PayrollBatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollServiceTest {
    
    @Mock
    private PayrollBatchRepository payrollBatchRepository;
    
    @Mock
    private AccountServiceAdapter accountServiceAdapter;
    
    @Mock
    private CustomerServiceAdapter customerServiceAdapter;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    private PayrollService payrollService;
    
    @BeforeEach
    void setUp() {
        payrollService = new PayrollService(
            payrollBatchRepository,
            accountServiceAdapter,
            customerServiceAdapter,
            eventPublisher
        );
    }
    
    @Test
    void shouldCreatePayrollBatch() {
        PayrollBatchRequest request = new PayrollBatchRequest();
        request.setCustomerId(1L);
        request.setFileFormat(PayrollFileFormat.CSV);
        request.setRecordCount(100);
        request.setTotalAmount(new BigDecimal("50000.00"));
        request.setCurrency("USD");
        request.setPaymentDate(LocalDate.now());
        
        when(customerServiceAdapter.isValidCustomer(1L)).thenReturn(true);
        
        PayrollBatch savedBatch = new PayrollBatch();
        savedBatch.setId(1L);
        savedBatch.setBatchReference("PAY-20260324-ABC123");
        savedBatch.setCustomerId(1L);
        savedBatch.setStatus(PayrollBatchStatus.PENDING);
        savedBatch.setFileFormat(PayrollFileFormat.CSV);
        savedBatch.setRecordCount(100);
        savedBatch.setTotalAmount(new BigDecimal("50000.00"));
        savedBatch.setCurrency("USD");
        savedBatch.setPaymentDate(LocalDate.now());
        
        when(payrollBatchRepository.save(any(PayrollBatch.class))).thenReturn(savedBatch);
        
        PayrollBatchResponse response = payrollService.createPayrollBatch(request);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PAY-20260324-ABC123", response.getBatchReference());
        assertEquals(PayrollBatchStatus.PENDING, response.getStatus());
        verify(payrollBatchRepository).save(any(PayrollBatch.class));
    }
    
    @Test
    void shouldThrowExceptionWhenCustomerInvalid() {
        PayrollBatchRequest request = new PayrollBatchRequest();
        request.setCustomerId(999L);
        
        when(customerServiceAdapter.isValidCustomer(999L)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> {
            payrollService.createPayrollBatch(request);
        });
    }
    
    @Test
    void shouldGetPayrollBatchById() {
        PayrollBatch batch = new PayrollBatch();
        batch.setId(1L);
        batch.setBatchReference("PAY-20260324-ABC123");
        batch.setCustomerId(1L);
        batch.setStatus(PayrollBatchStatus.PENDING);
        
        when(payrollBatchRepository.findById(1L)).thenReturn(Optional.of(batch));
        
        PayrollBatchResponse response = payrollService.getPayrollBatch(1L);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PAY-20260324-ABC123", response.getBatchReference());
    }
    
    @Test
    void shouldThrowExceptionWhenPayrollBatchNotFound() {
        when(payrollBatchRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(PayrollBatchNotFoundException.class, () -> {
            payrollService.getPayrollBatch(999L);
        });
    }
    
    @Test
    void shouldListPayrollBatchesByCustomerId() {
        PayrollBatch batch = new PayrollBatch();
        batch.setId(1L);
        batch.setCustomerId(1L);
        
        when(payrollBatchRepository.findByCustomerId(1L)).thenReturn(List.of(batch));
        
        List<PayrollBatchResponse> responses = payrollService.listPayrollBatches(1L);
        
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getCustomerId());
    }
    
    @Test
    void shouldListAllPayrollBatchesWhenNoCustomerId() {
        PayrollBatch batch = new PayrollBatch();
        batch.setId(1L);
        batch.setCustomerId(1L);
        
        when(payrollBatchRepository.findAll()).thenReturn(List.of(batch));
        
        List<PayrollBatchResponse> responses = payrollService.listPayrollBatches(null);
        
        assertEquals(1, responses.size());
        verify(payrollBatchRepository).findAll();
    }
    
    @Test
    void shouldVerifySufficientFunds() {
        when(accountServiceAdapter.getAvailableBalance(1L)).thenReturn(new BigDecimal("100000.00"));
        
        boolean result = payrollService.verifySufficientFunds(1L, new BigDecimal("50000.00"));
        
        assertTrue(result);
        verify(accountServiceAdapter).getAvailableBalance(1L);
    }
    
    @Test
    void shouldReturnFalseWhenInsufficientFunds() {
        when(accountServiceAdapter.getAvailableBalance(1L)).thenReturn(new BigDecimal("30000.00"));
        
        boolean result = payrollService.verifySufficientFunds(1L, new BigDecimal("50000.00"));
        
        assertFalse(result);
    }
    
    @Test
    void shouldReturnFalseWhenAccountNotFound() {
        when(accountServiceAdapter.getAvailableBalance(999L)).thenReturn(null);
        
        boolean result = payrollService.verifySufficientFunds(999L, new BigDecimal("50000.00"));
        
        assertFalse(result);
    }
    
    @Test
    void shouldCompletePayrollBatchAndPublishEvent() {
        PayrollBatch batch = new PayrollBatch();
        batch.setId(1L);
        batch.setBatchReference("PAY-20260324-ABC123");
        batch.setCustomerId(1L);
        batch.setStatus(PayrollBatchStatus.PENDING);
        batch.setTotalAmount(new BigDecimal("50000.00"));
        batch.setCurrency("USD");
        
        when(payrollBatchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(payrollBatchRepository.save(any(PayrollBatch.class))).thenReturn(batch);
        
        PayrollBatchResponse response = payrollService.completePayrollBatch(1L);
        
        assertNotNull(response);
        assertEquals(PayrollBatchStatus.COMPLETED, response.getStatus());
        verify(eventPublisher).publishEvent(any());
    }
}
