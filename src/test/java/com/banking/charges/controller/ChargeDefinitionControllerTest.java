package com.banking.charges.controller;

import com.banking.charges.ChargesTestApplication;
import com.banking.charges.dto.request.CreateChargeDefinitionRequest;
import com.banking.charges.repository.ChargeDefinitionRepository;
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
class ChargeDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChargeDefinitionRepository chargeDefinitionRepository;

    @BeforeEach
    void setUp() {
        chargeDefinitionRepository.deleteAll();
    }

    @Test
    void createCharge_returns201() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Monthly Fee"))
                .andExpect(jsonPath("$.chargeType").value("MONTHLY_MAINTENANCE"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createCharge_duplicateName_returns409() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageCode").value("CHRG-002"));
    }

    @Test
    void createCharge_invalidChargeType_returns400() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("INVALID_TYPE");
        request.setCurrency("USD");

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("CHRG-003"));
    }

    @Test
    void getAllCharges_returnsList() throws Exception {
        CreateChargeDefinitionRequest request1 = new CreateChargeDefinitionRequest();
        request1.setName("Monthly Fee");
        request1.setChargeType("MONTHLY_MAINTENANCE");
        request1.setCurrency("USD");

        CreateChargeDefinitionRequest request2 = new CreateChargeDefinitionRequest();
        request2.setName("Wire Transfer Fee");
        request2.setChargeType("WIRE_TRANSFER_FEE");
        request2.setCurrency("USD");

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/charges/definitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCharge_returnsCharge() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        String response = mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/charges/definitions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Monthly Fee"));
    }

    @Test
    void getCharge_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/charges/definitions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("CHRG-001"));
    }

    @Test
    void activateCharge_returnsActivated() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        String response = mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/api/charges/definitions/" + id + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deactivateCharge_returnsDeactivated() throws Exception {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName("Monthly Fee");
        request.setChargeType("MONTHLY_MAINTENANCE");
        request.setCurrency("USD");

        String response = mockMvc.perform(post("/api/charges/definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/api/charges/definitions/" + id + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }
}