package com.banking.account.controller;

import com.banking.account.dto.AccountBalanceResponse;
import com.banking.account.dto.AccountDetails;
import com.banking.account.dto.AccountOpeningRequest;
import com.banking.account.dto.AccountHolderRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.AccountStatement;
import com.banking.account.dto.AccountStatementResponse;
import com.banking.account.dto.StatementEntry;
import com.banking.account.dto.AccountStatementFilter;
import com.banking.account.domain.embeddable.AccountBalance;
import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.domain.enums.Currency;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.account.exception.DuplicateAccountException;
import com.banking.account.exception.InvalidAccountStateException;
import com.banking.account.service.AccountInquiryService;
import com.banking.account.service.AccountService;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class)
@ContextConfiguration(classes = {AccountController.class, AccountExceptionHandler.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountInquiryService accountInquiryService;

    @Test
    void shouldOpenAccount() throws Exception {
        AccountOpeningRequest request = createAccountOpeningRequest();
        AccountResponse response = createAccountResponse();

        when(accountService.openAccount(any())).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC-20240115-000001"))
                .andExpect(jsonPath("$.type").value("CURRENT"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.productId").value(1L));
    }

    @Test
    void shouldReturn409WhenDuplicateAccount() throws Exception {
        AccountOpeningRequest request = createAccountOpeningRequest();

        when(accountService.openAccount(any()))
                .thenThrow(new DuplicateAccountException("ACC-20240115-000001"));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("ACCT-001"));
    }

    @Test
    void shouldReturn400WhenOpeningAccountWithMissingCustomerId() throws Exception {
        AccountOpeningRequest request = createAccountOpeningRequest();
        request.setCustomerId(null);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("customerId"));
    }

    @Test
    void shouldReturn400WhenOpeningAccountWithMissingCurrency() throws Exception {
        AccountOpeningRequest request = createAccountOpeningRequest();
        request.setCurrency(null);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("currency"));
    }

@Test
void shouldGetAccountDetails() throws Exception {
        AccountDetails response = createAccountDetails();

        when(accountInquiryService.getAccountDetails("ACC-20240115-000001")).thenReturn(response);

        mockMvc.perform(get("/api/accounts/ACC-20240115-000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC-20240115-000001"))
                .andExpect(jsonPath("$.type").value("CURRENT"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {
        when(accountInquiryService.getAccountDetails("ACC-99999999-999999"))
                .thenThrow(new AccountNotFoundException("ACC-99999999-999999"));

        mockMvc.perform(get("/api/accounts/ACC-99999999-999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

    @Test
void shouldGetAccountBalance() throws Exception {
        AccountBalance response = createBalance();

        when(accountInquiryService.getAccountBalance("ACC-20240115-000001")).thenReturn(response);

        mockMvc.perform(get("/api/accounts/ACC-20240115-000001/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableBalance").value(1000.00))
                .andExpect(jsonPath("$.ledgerBalance").value(1000.00))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void shouldReturn404WhenGettingBalanceForNonExistentAccount() throws Exception {
        when(accountInquiryService.getAccountBalance("ACC-99999999-999999"))
                .thenThrow(new AccountNotFoundException("ACC-99999999-999999"));

        mockMvc.perform(get("/api/accounts/ACC-99999999-999999/balance"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

@Test
void shouldGetAccountStatement() throws Exception {
        AccountStatement response = createAccountStatement();

        when(accountInquiryService.getAccountStatement(
                eq("ACC-20240115-000001"), 
                any(AccountStatementFilter.class), 
                any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/accounts/ACC-20240115-000001/statement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC-20240115-000001"))
                .andExpect(jsonPath("$.openingBalance").value(1000.00))
                .andExpect(jsonPath("$.closingBalance").value(1500.00))
                .andExpect(jsonPath("$.transactions.length()").value(1))
                .andExpect(jsonPath("$.transactions[0].description").value("Deposit"));
    }

@Test
    void shouldCloseAccount() throws Exception {
        doNothing().when(accountService).closeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/close"))
                .andExpect(status().isNoContent());
    }

    @Test
void shouldReturn404WhenClosingNonExistentAccount() throws Exception {
        doThrow(new AccountNotFoundException("ACC-99999999-999999"))
                .when(accountService).closeAccount("ACC-99999999-999999");

        mockMvc.perform(put("/api/accounts/ACC-99999999-999999/close"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

    @Test
    void shouldReturn400WhenClosingAlreadyClosedAccount() throws Exception {
        doThrow(new InvalidAccountStateException("Account is already closed"))
                .when(accountService).closeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/close"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ACCT-003"));
    }

    @Test
void shouldFreezeAccount() throws Exception {
        doNothing().when(accountService).freezeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/freeze"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenFreezingNonExistentAccount() throws Exception {
        doThrow(new AccountNotFoundException("ACC-99999999-999999"))
                .when(accountService).freezeAccount("ACC-99999999-999999");

        mockMvc.perform(put("/api/accounts/ACC-99999999-999999/freeze"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

    @Test
    void shouldReturn400WhenFreezingClosedAccount() throws Exception {
        doThrow(new InvalidAccountStateException("Cannot freeze a closed account"))
                .when(accountService).freezeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/freeze"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ACCT-003"));
    }

    @Test
    void shouldUnfreezeAccount() throws Exception {
        doNothing().when(accountService).unfreezeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/unfreeze"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenUnfreezingNonExistentAccount() throws Exception {
        doThrow(new AccountNotFoundException("ACC-99999999-999999"))
                .when(accountService).unfreezeAccount("ACC-99999999-999999");

        mockMvc.perform(put("/api/accounts/ACC-99999999-999999/unfreeze"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

    @Test
    void shouldReturn400WhenUnfreezingActiveAccount() throws Exception {
        doThrow(new InvalidAccountStateException("Account is not frozen"))
                .when(accountService).unfreezeAccount("ACC-20240115-000001");

        mockMvc.perform(put("/api/accounts/ACC-20240115-000001/unfreeze"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ACCT-003"));
    }

    private AccountOpeningRequest createAccountOpeningRequest() {
        AccountOpeningRequest request = new AccountOpeningRequest();
        request.setCustomerId(1L);
        request.setProductCode("CUR-001");
        request.setType(AccountType.CURRENT);
        request.setCurrency(Currency.USD);
        request.setInitialDeposit(new BigDecimal("1000.00"));

        AccountHolderRequest holder = new AccountHolderRequest();
        holder.setCustomerId(1L);
        holder.setRole(AccountHolderRole.PRIMARY);
        request.setHolders(List.of(holder));

        return request;
    }

    private AccountResponse createAccountResponse() {
        AccountResponse response = new AccountResponse();
        response.setId(1L);
        response.setAccountNumber("ACC-20240115-000001");
        response.setType(AccountType.CURRENT);
        response.setStatus(AccountStatus.ACTIVE);
        response.setCurrency(Currency.USD);
        response.setBalance(new BigDecimal("1000.00"));
        response.setProductId(1L);
        response.setOpenedAt(LocalDateTime.ofInstant(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.systemDefault()));
        response.setClosedAt(null);
        return response;
    }

    private AccountBalanceResponse createBalanceResponse() {
        AccountBalanceResponse response = new AccountBalanceResponse();
        response.setAccountNumber("ACC-20240115-000001");
        response.setAvailableBalance(new BigDecimal("1000.00"));
        response.setLedgerBalance(new BigDecimal("1000.00"));
        response.setCurrency("USD");
        response.setAsOfDate(Instant.parse("2024-01-15T10:00:00Z"));
        return response;
    }

    private AccountDetails createAccountDetails() {
        AccountDetails response = new AccountDetails();
        response.setId(1L);
        response.setAccountNumber("ACC-20240115-000001");
        response.setType(AccountType.CURRENT);
        response.setStatus(AccountStatus.ACTIVE);
        response.setCurrency(Currency.USD);
        response.setBalance(new BigDecimal("1000.00"));
        response.setProductId(1L);
        response.setOpenedAt(LocalDateTime.ofInstant(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.systemDefault()));
        response.setClosedAt(null);
        return response;
    }

    private AccountBalance createBalance() {
        return new AccountBalance(
            new BigDecimal("1000.00"),
            new BigDecimal("1000.00"),
            BigDecimal.ZERO,
            Currency.USD
        );
    }

    private AccountStatement createAccountStatement() {
        AccountStatement response = new AccountStatement();
        response.setAccountNumber("ACC-20240115-000001");
        response.setFromDate(LocalDate.of(2024, 1, 1));
        response.setToDate(LocalDate.of(2024, 1, 31));
        response.setOpeningBalance(new BigDecimal("1000.00"));
        response.setClosingBalance(new BigDecimal("1500.00"));
        response.setTotalCredits(new BigDecimal("500.00"));
        response.setTotalDebits(BigDecimal.ZERO);

        AccountStatement.TransactionEntry entry = new AccountStatement.TransactionEntry();
        entry.setId("TXN-001");
        entry.setDate(LocalDate.of(2024, 1, 15));
        entry.setDescription("Deposit");
        entry.setAmount(new BigDecimal("500.00"));
        entry.setType("CREDIT");
        response.setTransactions(List.of(entry));

        return response;
    }

    private AccountStatementResponse createStatementResponse() {
        AccountStatementResponse response = new AccountStatementResponse();
        response.setAccountNumber("ACC-20240115-000001");
        response.setCurrency("USD");
        response.setFromDate(Instant.parse("2024-01-01T00:00:00Z"));
        response.setToDate(Instant.parse("2024-01-31T23:59:59Z"));
        response.setOpeningBalance(new BigDecimal("1000.00"));
        response.setClosingBalance(new BigDecimal("1500.00"));

        StatementEntry entry = new StatementEntry();
        entry.setDate(Instant.parse("2024-01-15T10:00:00Z"));
        entry.setDescription("Deposit");
        entry.setDebit(null);
        entry.setCredit(new BigDecimal("500.00"));
        entry.setBalance(new BigDecimal("1500.00"));
        response.setEntries(List.of(entry));

        return response;
    }
}
