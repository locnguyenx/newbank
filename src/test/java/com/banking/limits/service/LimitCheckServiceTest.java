package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.ApprovalThreshold;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.ProductLimit;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.request.LimitCheckRequest;
import com.banking.limits.dto.response.LimitCheckResponse;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.ApprovalThresholdRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LimitCheckServiceTest {

    @Mock
    private AccountLimitRepository accountLimitRepository;

    @Mock
    private CustomerLimitRepository customerLimitRepository;

    @Mock
    private ProductLimitRepository productLimitRepository;

    @Mock
    private LimitDefinitionRepository limitDefinitionRepository;

    @Mock
    private ApprovalThresholdRepository approvalThresholdRepository;

    @Mock
    private LimitUsageService limitUsageService;

    private LimitCheckService limitCheckService;

    @BeforeEach
    void setUp() {
        limitCheckService = new LimitCheckService(
                accountLimitRepository,
                customerLimitRepository,
                productLimitRepository,
                limitDefinitionRepository,
                approvalThresholdRepository,
                limitUsageService
        );
    }

    @Test
    void checkLimit_withAccountLevelLimit_returnsCorrectLimit() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(BigDecimal.ZERO);

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
        assertEquals(0, new BigDecimal("50000.00").compareTo(response.getEffectiveLimit()));
        assertEquals(0, new BigDecimal("40000.00").compareTo(response.getRemainingAmount()));
    }

    @Test
    void checkLimit_withCustomerLevelLimit_fallsBack() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        CustomerLimit customerLimit = new CustomerLimit(limitDef, 100L, null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(Collections.emptyList());
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(List.of(customerLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(BigDecimal.ZERO);

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
        assertEquals(0, new BigDecimal("50000.00").compareTo(response.getEffectiveLimit()));
    }

    @Test
    void checkLimit_withProductLevelLimit_fallsBack() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        ProductLimit productLimit = new ProductLimit(limitDef, "PROD1", null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(Collections.emptyList());
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(Collections.emptyList());
        when(productLimitRepository.findByProductCode("PROD1")).thenReturn(List.of(productLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(BigDecimal.ZERO);

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
    }

    @Test
    void checkLimit_withNoLimit_returnsAllowed() {
        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(Collections.emptyList());
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(Collections.emptyList());
        when(productLimitRepository.findByProductCode("PROD1")).thenReturn(Collections.emptyList());

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
        assertNull(response.getEffectiveLimit());
    }

    @Test
    void checkLimit_exceedsLimit_returnsRejected() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(new BigDecimal("45000.00"));

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.REJECTED, response.getResult());
        assertNotNull(response.getRejectionReason());
    }

    @Test
    void checkLimit_aboveThreshold_returnsRequiresApproval() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", null);
        ApprovalThreshold threshold = new ApprovalThreshold(limitDef, new BigDecimal("10000.00"), null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(BigDecimal.ZERO);
        when(approvalThresholdRepository.findByLimitDefinitionId(1L)).thenReturn(Optional.of(threshold));

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("15000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.REQUIRES_APPROVAL, response.getResult());
        assertTrue(response.isApprovalRequired());
    }

    @Test
    void checkLimit_inactiveLimit_returnsAllowed() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.INACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", null);

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
        assertNull(response.getEffectiveLimit());
    }

    @Test
    void checkLimit_withOverrideAmount_usesOverride() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", new BigDecimal("30000.00"));

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(limitUsageService.getCumulativeUsage(eq(1L), eq("ACC123"), any())).thenReturn(BigDecimal.ZERO);

        LimitCheckRequest request = createRequest("ACC123", 100L, "PROD1", new BigDecimal("10000.00"), "USD", "DAILY");

        LimitCheckResponse response = limitCheckService.checkLimit(request);

        assertEquals(com.banking.limits.domain.enums.LimitCheckResult.ALLOWED, response.getResult());
        assertEquals(0, new BigDecimal("30000.00").compareTo(response.getEffectiveLimit()));
    }

    private LimitCheckRequest createRequest(String accountNumber, Long customerId, String productCode, 
                                             BigDecimal amount, String currency, String limitType) {
        LimitCheckRequest request = new LimitCheckRequest();
        request.setAccountNumber(accountNumber);
        request.setCustomerId(customerId);
        request.setProductCode(productCode);
        request.setTransactionAmount(amount);
        request.setCurrency(currency);
        request.setLimitType(limitType);
        return request;
    }

    private LimitDefinition createLimitDefinition(Long id, String name, LimitType type, BigDecimal amount, 
                                                   String currency, LimitStatus status) {
        LimitDefinition limitDef = new LimitDefinition(name, type, amount, currency);
        limitDef.setId(id);
        limitDef.setStatus(status);
        return limitDef;
    }
}