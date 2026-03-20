package com.banking.account.controller;

import com.banking.account.dto.AccountHolderRequest;
import com.banking.account.dto.AccountHolderResponse;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountHolderController.class)
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
        request.setRole("JOINT");

        AccountHolderResponse response = new AccountHolderResponse();
        response.setId(1L);
        response.setCustomerId(2L);
        response.setCustomerName("John Doe");
        response.setRole("JOINT");
        response.setAddedAt(Instant.parse("2024-01-15T10:00:00Z"));

        when(accountService.addHolder(eq("ACC-20240115-000001"), any(AccountHolderRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/accounts/ACC-20240115-000001/holders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(2L))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.role").value("JOINT"));
    }

    @Test
    void shouldReturn404WhenAddingHolderToNonExistentAccount() throws Exception {
        AccountHolderRequest request = new AccountHolderRequest();
        request.setCustomerId(2L);
        request.setRole("JOINT");

        when(accountService.addHolder(eq("ACC-99999999-999999"), any(AccountHolderRequest.class)))
                .thenThrow(new AccountNotFoundException("ACC-99999999-999999"));

        mockMvc.perform(post("/api/accounts/ACC-99999999-999999/holders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACC-001"));
    }

    @Test
    void shouldRemoveHolder() throws Exception {
        doNothing().when(accountService).removeHolder("ACC-20240115-000001", 1L);

        mockMvc.perform(delete("/api/accounts/ACC-20240115-000001/holders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenRemovingHolderFromNonExistentAccount() throws Exception {
        doThrow(new AccountNotFoundException("ACC-99999999-999999"))
                .when(accountService).removeHolder("ACC-99999999-999999", 1L);

        mockMvc.perform(delete("/api/accounts/ACC-99999999-999999/holders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACC-001"));
    }
}
