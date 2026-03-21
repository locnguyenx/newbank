package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.ProductLimit;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.response.AccountLimitResponse;
import com.banking.limits.dto.response.CustomerLimitResponse;
import com.banking.limits.dto.response.ProductLimitResponse;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitAssignmentServiceTest {

    @Mock
    private LimitDefinitionRepository limitDefinitionRepository;

    @Mock
    private ProductLimitRepository productLimitRepository;

    @Mock
    private CustomerLimitRepository customerLimitRepository;

    @Mock
    private AccountLimitRepository accountLimitRepository;

    private LimitAssignmentService limitAssignmentService;

    private LimitDefinition limitDefinition;

    @BeforeEach
    void setUp() {
        limitAssignmentService = new LimitAssignmentService(
                limitDefinitionRepository, productLimitRepository,
                customerLimitRepository, accountLimitRepository);
        limitDefinition = new LimitDefinition("Daily Transfer", LimitType.DAILY,
                new BigDecimal("50000.00"), "USD");
    }

    // BDD S2.1
    @Test
    void assignToProduct_success() {
        Long limitId = 1L;
        String productCode = "SAVINGS";
        BigDecimal override = new BigDecimal("30000.00");

        when(limitDefinitionRepository.findById(limitId)).thenReturn(Optional.of(limitDefinition));
        ProductLimit savedEntity = new ProductLimit(limitDefinition, productCode, override);
        when(productLimitRepository.save(any(ProductLimit.class))).thenReturn(savedEntity);

        ProductLimitResponse response = limitAssignmentService.assignToProduct(limitId, productCode, override);

        assertNotNull(response);
        assertEquals("SAVINGS", response.getProductCode());
        assertEquals(0, new BigDecimal("30000.00").compareTo(response.getOverrideAmount()));
        verify(limitDefinitionRepository).findById(limitId);
        verify(productLimitRepository).save(any(ProductLimit.class));
    }

    // BDD S3.1
    @Test
    void assignToCustomer_success() {
        Long limitId = 1L;
        Long customerId = 100L;
        BigDecimal override = new BigDecimal("40000.00");

        when(limitDefinitionRepository.findById(limitId)).thenReturn(Optional.of(limitDefinition));
        CustomerLimit savedEntity = new CustomerLimit(limitDefinition, customerId, override);
        when(customerLimitRepository.save(any(CustomerLimit.class))).thenReturn(savedEntity);

        CustomerLimitResponse response = limitAssignmentService.assignToCustomer(limitId, customerId, override);

        assertNotNull(response);
        assertEquals(100L, response.getCustomerId());
        assertEquals(0, new BigDecimal("40000.00").compareTo(response.getOverrideAmount()));
        verify(limitDefinitionRepository).findById(limitId);
        verify(customerLimitRepository).save(any(CustomerLimit.class));
    }

    // BDD S4.1
    @Test
    void assignToAccount_success() {
        Long limitId = 1L;
        String accountNumber = "ACC-001-2024";
        BigDecimal override = new BigDecimal("25000.00");

        when(limitDefinitionRepository.findById(limitId)).thenReturn(Optional.of(limitDefinition));
        AccountLimit savedEntity = new AccountLimit(limitDefinition, accountNumber, override);
        when(accountLimitRepository.save(any(AccountLimit.class))).thenReturn(savedEntity);

        AccountLimitResponse response = limitAssignmentService.assignToAccount(limitId, accountNumber, override);

        assertNotNull(response);
        assertEquals("ACC-001-2024", response.getAccountNumber());
        assertEquals(0, new BigDecimal("25000.00").compareTo(response.getOverrideAmount()));
        verify(limitDefinitionRepository).findById(limitId);
        verify(accountLimitRepository).save(any(AccountLimit.class));
    }

    @Test
    void assignToProduct_limitNotFound_throws() {
        when(limitDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LimitNotFoundException.class, () ->
                limitAssignmentService.assignToProduct(999L, "SAVINGS", null));
    }

    @Test
    void assignToCustomer_limitNotFound_throws() {
        when(limitDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LimitNotFoundException.class, () ->
                limitAssignmentService.assignToCustomer(999L, 100L, null));
    }

    @Test
    void assignToAccount_limitNotFound_throws() {
        when(limitDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LimitNotFoundException.class, () ->
                limitAssignmentService.assignToAccount(999L, "ACC-001", null));
    }
}
