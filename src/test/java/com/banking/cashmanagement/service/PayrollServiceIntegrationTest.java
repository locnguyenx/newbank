package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.PayrollBatch;
import com.banking.cashmanagement.domain.enums.PayrollBatchStatus;
import com.banking.cashmanagement.domain.enums.PayrollFileFormat;
import com.banking.cashmanagement.repository.PayrollBatchRepository;
import com.banking.common.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PayrollServiceIntegrationTest extends AbstractIntegrationTest {

    @MockBean
    private com.banking.cashmanagement.integration.AccountServiceAdapter accountServiceAdapter;

    @MockBean
    private com.banking.cashmanagement.integration.CustomerServiceAdapter customerServiceAdapter;

    @MockBean
    private com.banking.common.security.auth.AuthService authService;

    @Autowired
    private PayrollBatchRepository payrollBatchRepository;

    @Autowired
    private PayrollService payrollService;

    @BeforeEach
    void setUp() {
        payrollBatchRepository.deleteAll();
        when(customerServiceAdapter.isValidCustomer(any())).thenReturn(true);
        when(accountServiceAdapter.getAvailableBalance(any())).thenReturn(new BigDecimal("100000.00"));
    }

    @Test
    void shouldCreatePayrollBatch() {
        var request = new com.banking.cashmanagement.dto.PayrollBatchRequest();
        request.setCustomerId(1L);
        request.setFileFormat(PayrollFileFormat.CSV);
        request.setRecordCount(50);
        request.setTotalAmount(new BigDecimal("50000.00"));
        request.setCurrency("USD");
        request.setPaymentDate(LocalDate.now());

        var response = payrollService.createPayrollBatch(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getBatchReference());
        assertEquals(PayrollBatchStatus.PENDING, response.getStatus());
    }

    @Test
    void shouldFindPayrollBatchById() {
        var batch = new PayrollBatch();
        batch.setBatchReference("PAY-TEST-001");
        batch.setCustomerId(1L);
        batch.setStatus(PayrollBatchStatus.PENDING);
        batch.setFileFormat(PayrollFileFormat.CSV);
        batch.setRecordCount(50);
        batch.setTotalAmount(new BigDecimal("50000.00"));
        batch.setCurrency("USD");
        batch.setPaymentDate(LocalDate.now());
        
        var saved = payrollBatchRepository.save(batch);

        var response = payrollService.getPayrollBatch(saved.getId());

        assertNotNull(response);
        assertEquals(saved.getId(), response.getId());
        assertEquals("PAY-TEST-001", response.getBatchReference());
    }

    @Test
    void shouldThrowExceptionWhenBatchNotFound() {
        assertThrows(com.banking.cashmanagement.exception.PayrollBatchNotFoundException.class, () -> {
            payrollService.getPayrollBatch(999L);
        });
    }

    @Test
    void shouldListPayrollBatchesByCustomer() {
        var batch1 = new PayrollBatch();
        batch1.setBatchReference("PAY-TEST-001");
        batch1.setCustomerId(1L);
        batch1.setStatus(PayrollBatchStatus.PENDING);
        batch1.setFileFormat(PayrollFileFormat.CSV);
        batch1.setRecordCount(50);
        batch1.setTotalAmount(new BigDecimal("50000.00"));
        batch1.setCurrency("USD");
        batch1.setPaymentDate(LocalDate.now());
        
        var batch2 = new PayrollBatch();
        batch2.setBatchReference("PAY-TEST-002");
        batch2.setCustomerId(1L);
        batch2.setStatus(PayrollBatchStatus.COMPLETED);
        batch2.setFileFormat(PayrollFileFormat.CSV);
        batch2.setRecordCount(100);
        batch2.setTotalAmount(new BigDecimal("100000.00"));
        batch2.setCurrency("USD");
        batch2.setPaymentDate(LocalDate.now());

        payrollBatchRepository.save(batch1);
        payrollBatchRepository.save(batch2);

        var responses = payrollService.listPayrollBatches(1L);

        assertEquals(2, responses.size());
    }

    @Test
    void shouldVerifySufficientFunds() {
        var result = payrollService.verifySufficientFunds(1L, new BigDecimal("50000.00"));

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenInsufficientFunds() {
        var result = payrollService.verifySufficientFunds(1L, new BigDecimal("200000.00"));

        assertFalse(result);
    }

    @Test
    void shouldCompletePayrollBatchAndPublishEvent() {
        var batch = new PayrollBatch();
        batch.setBatchReference("PAY-TEST-001");
        batch.setCustomerId(1L);
        batch.setStatus(PayrollBatchStatus.PENDING);
        batch.setFileFormat(PayrollFileFormat.CSV);
        batch.setRecordCount(50);
        batch.setTotalAmount(new BigDecimal("50000.00"));
        batch.setCurrency("USD");
        batch.setPaymentDate(LocalDate.now());
        
        var saved = payrollBatchRepository.save(batch);

        var response = payrollService.completePayrollBatch(saved.getId());

        assertEquals(PayrollBatchStatus.COMPLETED, response.getStatus());
    }
}
