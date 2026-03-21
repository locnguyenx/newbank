package com.banking.limits.service;

import com.banking.limits.domain.entity.AccountLimit;
import com.banking.limits.domain.entity.CustomerLimit;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.response.EffectiveLimitResponse;
import com.banking.limits.repository.AccountLimitRepository;
import com.banking.limits.repository.CustomerLimitRepository;
import com.banking.limits.repository.ProductLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LimitQueryServiceTest {

    @Mock
    private AccountLimitRepository accountLimitRepository;

    @Mock
    private CustomerLimitRepository customerLimitRepository;

    @Mock
    private ProductLimitRepository productLimitRepository;

    private LimitQueryService limitQueryService;

    @BeforeEach
    void setUp() {
        limitQueryService = new LimitQueryService(
                accountLimitRepository,
                customerLimitRepository,
                productLimitRepository
        );
    }

    @Test
    void getEffectiveLimit_withAccountLevel_returnsAccountSource() {
        LimitDefinition limitDef = createLimitDefinition(1L, "Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(limitDef, "ACC123", new BigDecimal("40000.00"));

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));

        EffectiveLimitResponse response = limitQueryService.getEffectiveLimit("ACC123", 100L, "PROD1", "USD", "DAILY");

        assertNotNull(response);
        assertEquals("ACCOUNT", response.getSource());
        assertEquals(0, new BigDecimal("40000.00").compareTo(response.getEffectiveLimit()));
    }

    @Test
    void getEffectiveLimit_withCustomerLevel_returnsCustomerSource() {
        LimitDefinition accountLimitDef = createLimitDefinition(1L, "Account Daily", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(accountLimitDef, "ACC123", null);
        
        LimitDefinition customerLimitDef = createLimitDefinition(2L, "Customer Monthly", LimitType.MONTHLY, new BigDecimal("30000.00"), "USD", LimitStatus.ACTIVE);
        CustomerLimit customerLimit = new CustomerLimit(customerLimitDef, 100L, new BigDecimal("30000.00"));

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(List.of(customerLimit));

        EffectiveLimitResponse response = limitQueryService.getEffectiveLimit("ACC123", 100L, "PROD1", "USD", "MONTHLY");

        assertNotNull(response);
        assertEquals("CUSTOMER", response.getSource());
    }

    @Test
    void getEffectiveLimit_noLimit_returnsEmpty() {
        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(Collections.emptyList());
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(Collections.emptyList());
        when(productLimitRepository.findByProductCode("PROD1")).thenReturn(Collections.emptyList());

        EffectiveLimitResponse response = limitQueryService.getEffectiveLimit("ACC123", 100L, "PROD1", "USD", "DAILY");

        assertNull(response);
    }

    @Test
    void getAllEffectiveLimits_withMultipleSources_returnsAll() {
        LimitDefinition accountLimitDef = createLimitDefinition(1L, "Account Daily", LimitType.DAILY, new BigDecimal("50000.00"), "USD", LimitStatus.ACTIVE);
        AccountLimit accountLimit = new AccountLimit(accountLimitDef, "ACC123", new BigDecimal("40000.00"));

        when(accountLimitRepository.findByAccountNumber("ACC123")).thenReturn(List.of(accountLimit));
        when(customerLimitRepository.findByCustomerId(100L)).thenReturn(Collections.emptyList());
        when(productLimitRepository.findByProductCode("PROD1")).thenReturn(Collections.emptyList());

        List<EffectiveLimitResponse> responses = limitQueryService.getAllEffectiveLimits("ACC123", 100L, "PROD1", "USD");

        assertFalse(responses.isEmpty());
        assertTrue(responses.stream().anyMatch(r -> "ACCOUNT".equals(r.getSource())));
    }

    private LimitDefinition createLimitDefinition(Long id, String name, LimitType type, BigDecimal amount, 
                                                   String currency, LimitStatus status) {
        LimitDefinition limitDef = new LimitDefinition(name, type, amount, currency);
        limitDef.setId(id);
        limitDef.setStatus(status);
        return limitDef;
    }
}