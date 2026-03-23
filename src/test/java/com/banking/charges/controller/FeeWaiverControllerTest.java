package com.banking.charges.controller;

import com.banking.charges.ChargesTestApplication;
import com.banking.charges.dto.request.CreateFeeWaiverRequest;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.FeeWaiverRepository;
import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.banking.common.security.config.TestSecurityConfig;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = ChargesTestApplication.class)
@Import(TestSecurityConfig.class)
@Transactional
class FeeWaiverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChargeDefinitionRepository chargeDefinitionRepository;

    @Autowired
    private FeeWaiverRepository feeWaiverRepository;

    @BeforeEach
    void setUp() {
        feeWaiverRepository.deleteAll();
        chargeDefinitionRepository.deleteAll();
    }

    @Test
    void createWaiver_returns201() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(charge.getId());
        request.setScope("CUSTOMER");
        request.setReferenceId("123");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());

        mockMvc.perform(post("/api/charges/waivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.scope").value("CUSTOMER"))
                .andExpect(jsonPath("$.referenceId").value("123"))
                .andExpect(jsonPath("$.waiverPercentage").value(50));
    }

    @Test
    void deleteWaiver_returns204() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(charge.getId());
        request.setScope("CUSTOMER");
        request.setReferenceId("123");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());

        String response = mockMvc.perform(post("/api/charges/waivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/charges/waivers/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getWaivers_withParams_returns200() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(charge.getId());
        request.setScope("CUSTOMER");
        request.setReferenceId("123");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());

        mockMvc.perform(post("/api/charges/waivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/charges/waivers")
                        .param("scope", "CUSTOMER")
                        .param("referenceId", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].scope").value("CUSTOMER"))
                .andExpect(jsonPath("$[0].referenceId").value("123"));
    }

    @Test
    void getApplicableWaivers_returns200() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(charge.getId());
        request.setScope("CUSTOMER");
        request.setReferenceId("123");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/charges/waivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/charges/waivers/applicable")
                        .param("chargeId", charge.getId().toString())
                        .param("customerId", "123")
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}