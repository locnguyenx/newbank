package com.banking.charges.controller;

import com.banking.charges.ChargesTestApplication;
import com.banking.charges.dto.request.CreateInterestRateRequest;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.InterestRateRepository;
import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.domain.enums.InterestSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = ChargesTestApplication.class)
@Transactional
class InterestRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChargeDefinitionRepository chargeDefinitionRepository;

    @Autowired
    private InterestRateRepository interestRateRepository;

    @BeforeEach
    void setUp() {
        interestRateRepository.deleteAll();
        chargeDefinitionRepository.deleteAll();
    }

    @Test
    void createInterestRate_returns201() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateInterestRateRequest request = new CreateInterestRateRequest();
        request.setChargeId(charge.getId());
        request.setProductCode("SAVINGS-001");
        request.setFixedRate(new java.math.BigDecimal("2.500000"));
        request.setAccrualSchedule(InterestSchedule.DAILY.name());
        request.setApplicationSchedule(InterestSchedule.MONTHLY.name());

        mockMvc.perform(post("/api/charges/interest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.productCode").value("SAVINGS-001"))
                .andExpect(jsonPath("$.fixedRate").value(2.500000));
    }

    @Test
    void getInterestRatesByProduct_returns200() throws Exception {
        ChargeDefinition charge = new ChargeDefinition("Interest", ChargeType.INTEREST, "USD");
        charge.setStatus(ChargeStatus.ACTIVE);
        charge = chargeDefinitionRepository.save(charge);

        CreateInterestRateRequest request = new CreateInterestRateRequest();
        request.setChargeId(charge.getId());
        request.setProductCode("SAVINGS-001");
        request.setFixedRate(new java.math.BigDecimal("2.500000"));
        request.setAccrualSchedule(InterestSchedule.DAILY.name());
        request.setApplicationSchedule(InterestSchedule.MONTHLY.name());

        mockMvc.perform(post("/api/charges/interest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/charges/interest/product/SAVINGS-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productCode").value("SAVINGS-001"));
    }
}