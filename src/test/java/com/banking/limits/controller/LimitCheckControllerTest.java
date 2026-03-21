package com.banking.limits.controller;

import com.banking.limits.dto.request.LimitCheckRequest;
import com.banking.limits.dto.response.LimitCheckResponse;
import com.banking.limits.service.LimitCheckService;
import com.banking.limits.service.LimitQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LimitCheckController.class)
@ContextConfiguration(classes = {LimitCheckController.class})
class LimitCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LimitCheckService limitCheckService;

    @MockBean
    private LimitQueryService limitQueryService;

    @Test
    void checkLimit_returnsCorrectResponse() throws Exception {
        LimitCheckResponse response = LimitCheckResponse.allowed(
                new BigDecimal("50000.00"), 
                BigDecimal.ZERO, 
                new BigDecimal("40000.00"));
        
        when(limitCheckService.checkLimit(any(LimitCheckRequest.class))).thenReturn(response);

        LimitCheckRequest request = new LimitCheckRequest();
        request.setAccountNumber("ACC123");
        request.setCustomerId(100L);
        request.setProductCode("PROD1");
        request.setTransactionAmount(new BigDecimal("10000.00"));
        request.setCurrency("USD");
        request.setLimitType("DAILY");

        mockMvc.perform(post("/api/limits/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ALLOWED"))
                .andExpect(jsonPath("$.effectiveLimit").value(50000.00))
                .andExpect(jsonPath("$.remainingAmount").value(40000.00));
    }

    @Test
    void effectiveLimits_returnsList() throws Exception {
        when(limitQueryService.getAllEffectiveLimits(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/limits/check/effective")
                        .param("accountNumber", "ACC123")
                        .param("customerId", "100")
                        .param("productCode", "PROD1")
                        .param("currency", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void checkLimit_withNoLimit_returnsAllowed() throws Exception {
        when(limitCheckService.checkLimit(any(LimitCheckRequest.class)))
                .thenReturn(LimitCheckResponse.noLimit());

        LimitCheckRequest request = new LimitCheckRequest();
        request.setAccountNumber("ACC123");
        request.setCustomerId(100L);
        request.setProductCode("PROD1");
        request.setTransactionAmount(new BigDecimal("10000.00"));
        request.setCurrency("USD");
        request.setLimitType("DAILY");

        mockMvc.perform(post("/api/limits/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ALLOWED"))
                .andExpect(jsonPath("$.effectiveLimit").doesNotExist());
    }
}