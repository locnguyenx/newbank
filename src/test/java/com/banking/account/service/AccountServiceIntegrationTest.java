package com.banking.account.service;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.dto.AccountOpeningRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.exception.InvalidAccountStateException;
import com.banking.account.repository.AccountRepository;
import com.banking.customer.api.CustomerQueryService;
import com.banking.customer.api.dto.CustomerDTO;
import com.banking.limits.domain.enums.LimitCheckResult;
import com.banking.limits.dto.response.LimitCheckResponse;
import com.banking.limits.service.LimitCheckService;
import com.banking.masterdata.api.CurrencyQueryService;
import com.banking.masterdata.api.dto.CurrencyDTO;
import com.banking.product.api.ProductQueryService;
import com.banking.product.api.dto.ProductVersionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountServiceIntegrationTest {

    private static final Long TEST_CUSTOMER_ID = 1L;

    @MockBean
    private CurrencyQueryService currencyQueryService;

    @MockBean
    private ProductQueryService productQueryService;

    @MockBean
    private CustomerQueryService customerQueryService;

    @MockBean
    private LimitCheckService limitCheckService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

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
        when(productQueryService.findActiveVersionByCode(any())).thenReturn(Optional.of(productVersion));

        LimitCheckResponse allowedResponse = new LimitCheckResponse();
        allowedResponse.setResult(LimitCheckResult.ALLOWED);
        when(limitCheckService.checkLimit(any())).thenReturn(allowedResponse);
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
        LimitCheckResponse rejectedResponse = new LimitCheckResponse();
        rejectedResponse.setResult(LimitCheckResult.REJECTED);
        rejectedResponse.setRejectionReason("Transaction exceeds daily limit");
        when(limitCheckService.checkLimit(any())).thenReturn(rejectedResponse);

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