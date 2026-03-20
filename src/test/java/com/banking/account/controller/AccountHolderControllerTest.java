package com.banking.account.controller;

import com.banking.account.dto.AccountHolderRequest;
import com.banking.account.dto.AccountHolderResponse;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountHolderController.class)
@ContextConfiguration(classes = {AccountHolderController.class, AccountExceptionHandler.class})
class AccountHolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void shouldAddHolder() throws Exception {
        AccountHolderRequest request = new AccountHolderRequest();
        request.setCustomerId(2L);
        request.setRole(AccountHolderRole.JOINT);

        doNothing().when(accountService).addAccountHolder(eq("ACC-20240115-000001"), any(AccountHolderRequest.class));

        mockMvc.perform(post("/api/accounts/ACC-20240115-000001/holders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenAddingHolderToNonExistentAccount() throws Exception {
        AccountHolderRequest request = new AccountHolderRequest();
        request.setCustomerId(2L);
        request.setRole(AccountHolderRole.JOINT);

        doThrow(new AccountNotFoundException("ACC-99999999-999999"))
                .when(accountService).addAccountHolder(eq("ACC-99999999-999999"), any(AccountHolderRequest.class));

        mockMvc.perform(post("/api/accounts/ACC-99999999-999999/holders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCT-002"));
    }

    @Test
    void shouldRemoveHolder() throws Exception {
        doNothing().when(accountService).removeAccountHolder(1L);

        mockMvc.perform(delete("/api/accounts/ACC-20240115-000001/holders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenRemovingHolderFromNonExistentAccount() throws Exception {
        doThrow(new AccountNotFoundException("Holder not found: 1"))
                .when(accountService).removeAccountHolder(1L);

        mockMvc.perform(delete("/api/accounts/ACC-99999999-999999/holders/1"))
                .andExpect(status().isNotFound());
    }
}
