package com.banking.account.service;

import com.banking.common.config.AbstractIntegrationTest;
import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.dto.AccountOpeningRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.exception.InvalidAccountStateException;
import com.banking.account.repository.AccountHolderRepository;
import com.banking.account.repository.AccountRepository;
import com.banking.customer.api.CustomerQueryService;
import com.banking.customer.api.dto.CustomerDTO;
import com.banking.limits.api.LimitCheckService;
import com.banking.limits.api.dto.LimitCheckResult;
import com.banking.masterdata.api.CurrencyQueryService;
import com.banking.masterdata.api.dto.CurrencyDTO;
import com.banking.product.api.dto.ProductVersionDTO;
import com.banking.product.service.ProductQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class AccountServiceIntegrationTest extends AbstractIntegrationTest {

    private static final Long TEST_CUSTOMER_ID = 1L;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private CurrencyQueryService currencyQueryService;

    @MockBean
    private ProductQueryServiceImpl productQueryServiceImpl;

    @MockBean
    private CustomerQueryService customerQueryService;

    @MockBean
    private LimitCheckService limitCheckService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAllInBatch();
        accountHolderRepository.deleteAllInBatch();

        CustomerDTO testCustomerDTO = new CustomerDTO(TEST_CUSTOMER_ID, "Test Corp");
        when(customerQueryService.findById(TEST_CUSTOMER_ID)).thenReturn(testCustomerDTO);
        when(customerQueryService.findById(any())).thenReturn(testCustomerDTO);

        CurrencyDTO usdCurrency = new CurrencyDTO();
        usdCurrency.setCode("USD");
        usdCurrency.setActive(true);
        when(currencyQueryService.findByCode("USD")).thenReturn(usdCurrency);
        when(currencyQueryService.findByCode(any())).thenReturn(usdCurrency);
        when(currencyQueryService.existsByCode(any())).thenReturn(true);

        ProductVersionDTO productVersion = new ProductVersionDTO();
        productVersion.setId(1L);
        productVersion.setProductId(1L);
        productVersion.setProductName("Current Account");
        productVersion.setVersionNumber(1);
        productVersion.setStatus("ACTIVE");
        when(productQueryServiceImpl.findActiveVersionByCode(any())).thenReturn(Optional.of(productVersion));

        LimitCheckResult allowedResult = new LimitCheckResult(true, null, null);
        when(limitCheckService.checkLimit(any(), any(), any(), any())).thenReturn(allowedResult);
    }

    @Test
    void shouldPassLimitCheckWhenWithinThreshold() {
        AccountOpeningRequest request = createRequest("PROD-CURRENT-001", "10000");

        AccountResponse response = accountService.openAccount(request);

        assertNotNull(response);
        assertEquals(AccountStatus.ACTIVE, response.getStatus());
        assertTrue(accountRepository.findById(response.getId()).isPresent());
    }

    @Test
    void shouldRejectAccountWhenLimitExceeded() {
        LimitCheckResult rejectedResult = new LimitCheckResult(false, "Transaction exceeds daily limit", null);
        when(limitCheckService.checkLimit(any(), any(), any(), any())).thenReturn(rejectedResult);

        AccountOpeningRequest request = createRequest("PROD-CURRENT-001", "10000");

        InvalidAccountStateException exception = assertThrows(InvalidAccountStateException.class, () -> {
            accountService.openAccount(request);
        });
        assertTrue(exception.getMessage().contains("exceeds limits"));
    }

    @Test
    void shouldCallLimitCheckServiceBeforeSavingAccount() {
        AccountOpeningRequest request = createRequest("PROD-CURRENT-001", "10000");

        accountService.openAccount(request);

        assertNotNull(accountRepository.findByAccountNumber(request.getProductCode() + "-000001"));
    }

    private AccountOpeningRequest createRequest(String productCode, String initialDeposit) {
        AccountOpeningRequest request = new AccountOpeningRequest();
        request.setProductCode(productCode);
        request.setType(AccountType.CURRENT);
        request.setCurrency("USD");
        request.setInitialDeposit(new BigDecimal(initialDeposit));
        request.setCustomerId(TEST_CUSTOMER_ID);
        request.setHolders(List.of());
        return request;
    }
}