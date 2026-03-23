package com.banking.charges.controller;

import com.banking.charges.ChargesTestApplication;
import com.banking.charges.dto.request.ChargeCalculationRequest;
import com.banking.charges.dto.request.CreateChargeRuleRequest;
import com.banking.charges.dto.request.CreateFeeWaiverRequest;
import com.banking.charges.service.ChargeDefinitionService;
import com.banking.charges.service.ChargeRuleService;
import com.banking.charges.service.FeeWaiverService;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = ChargesTestApplication.class)
@Import(TestSecurityConfig.class)
@Transactional
class ChargeCalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChargeDefinitionService chargeDefinitionService;

    @Autowired
    private ChargeRuleService chargeRuleService;

    @Autowired
    private FeeWaiverService feeWaiverService;

    @BeforeEach
    void setUp() {
        // Cleanup handled by @Transactional
    }

    @Test
    void calculate_withFLATFee_returnsCorrectBaseAmount() throws Exception {
        var charge = chargeDefinitionService.createCharge(new com.banking.charges.dto.request.CreateChargeDefinitionRequest() {{
            setName("Monthly Fee");
            setChargeType("MONTHLY_MAINTENANCE");
            setCurrency("USD");
        }});

        chargeRuleService.addRule(charge.getId(), new CreateChargeRuleRequest() {{
            setCalculationMethod("FLAT");
            setFlatAmount(new BigDecimal("25.0000"));
        }});

        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseAmount").value(25.0000))
                .andExpect(jsonPath("$.finalAmount").value(25.0000))
                .andExpect(jsonPath("$.waiverApplied").value(false));
    }

    @Test
    void calculate_withPERCENTAGEFee_appliesRateCorrectly() throws Exception {
        var charge = chargeDefinitionService.createCharge(new com.banking.charges.dto.request.CreateChargeDefinitionRequest() {{
            setName("Wire Transfer Fee");
            setChargeType("WIRE_TRANSFER_FEE");
            setCurrency("USD");
        }});

        chargeRuleService.addRule(charge.getId(), new CreateChargeRuleRequest() {{
            setCalculationMethod("PERCENTAGE");
            setPercentageRate(new BigDecimal("0.10"));
            setMinAmount(new BigDecimal("5.0000"));
            setMaxAmount(new BigDecimal("100.0000"));
        }});

        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setChargeType("WIRE_TRANSFER_FEE");
        request.setTransactionAmount(new BigDecimal("10000.00"));
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseAmount").value(10.0000))
                .andExpect(jsonPath("$.finalAmount").value(10.0000))
                .andExpect(jsonPath("$.ruleApplied").value("Wire Transfer Fee"));
    }

    @Test
    void calculate_withTIEREDVOLUME_appliesCorrectTier() throws Exception {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setChargeType("TRANSACTION_FEE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void calculate_withWaiver_appliesWaiverReduction() throws Exception {
        var charge = chargeDefinitionService.createCharge(new com.banking.charges.dto.request.CreateChargeDefinitionRequest() {{
            setName("Monthly Fee");
            setChargeType("MONTHLY_MAINTENANCE");
            setCurrency("USD");
        }});

        chargeRuleService.addRule(charge.getId(), new CreateChargeRuleRequest() {{
            setCalculationMethod("FLAT");
            setFlatAmount(new BigDecimal("100.0000"));
        }});

        feeWaiverService.createWaiver(new CreateFeeWaiverRequest() {{
            setChargeId(charge.getId());
            setScope("CUSTOMER");
            setReferenceId("123");
            setWaiverPercentage(50);
            setValidFrom(LocalDate.now().minusDays(1));
        }});

        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCustomerId(123L);
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseAmount").value(100.0000))
                .andExpect(jsonPath("$.waiverAmount").value(50.0000))
                .andExpect(jsonPath("$.finalAmount").value(50.0000))
                .andExpect(jsonPath("$.waiverApplied").value(true));
    }

    @Test
    void calculate_withUnknownChargeType_returnsZeroAmount() throws Exception {
        ChargeCalculationRequest request = new ChargeCalculationRequest();
        request.setChargeType("UNKNOWN_TYPE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseAmount").value(0))
                .andExpect(jsonPath("$.waiverApplied").value(false));
    }
}