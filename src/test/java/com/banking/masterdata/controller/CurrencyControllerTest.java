package com.banking.masterdata.controller;

import com.banking.masterdata.MasterDataTestApplication;
import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.repository.CurrencyRepository;
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
@ContextConfiguration(classes = MasterDataTestApplication.class)
@Transactional
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void createCurrency_returns201() throws Exception {
        CreateCurrencyRequest request = new CreateCurrencyRequest();
        request.setCode("USD");
        request.setName("US Dollar");
        request.setSymbol("$");
        request.setDecimalPlaces(2);

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("$"))
                .andExpect(jsonPath("$.decimalPlaces").value(2))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void createDuplicateCurrency_returns409() throws Exception {
        CreateCurrencyRequest request = new CreateCurrencyRequest();
        request.setCode("USD");
        request.setName("US Dollar");
        request.setSymbol("$");
        request.setDecimalPlaces(2);

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageCode").value("MDATA-002"));
    }

    @Test
    void listCurrencies_returns200() throws Exception {
        CreateCurrencyRequest usd = new CreateCurrencyRequest();
        usd.setCode("USD");
        usd.setName("US Dollar");
        usd.setSymbol("$");
        usd.setDecimalPlaces(2);

        CreateCurrencyRequest eur = new CreateCurrencyRequest();
        eur.setCode("EUR");
        eur.setName("Euro");
        eur.setSymbol("\u20ac");
        eur.setDecimalPlaces(2);

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usd)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eur)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/master-data/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCurrencyByCode_returns200() throws Exception {
        CreateCurrencyRequest request = new CreateCurrencyRequest();
        request.setCode("USD");
        request.setName("US Dollar");
        request.setSymbol("$");
        request.setDecimalPlaces(2);

        mockMvc.perform(post("/api/master-data/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/master-data/currencies/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"));
    }

    @Test
    void getCurrencyByCode_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/master-data/currencies/ZZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("MDATA-001"));
    }
}
