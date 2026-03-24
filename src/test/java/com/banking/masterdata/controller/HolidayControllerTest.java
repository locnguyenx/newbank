package com.banking.masterdata.controller;

import com.banking.masterdata.MasterDataTestApplication;
import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.dto.request.CreateHolidayRequest;
import com.banking.masterdata.repository.CountryRepository;
import com.banking.masterdata.repository.HolidayRepository;
import com.banking.common.security.config.TestSecurityConfig;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = MasterDataTestApplication.class)
@Import(TestSecurityConfig.class)
@Transactional
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll();

        if (!countryRepository.existsById("US")) {
            Country us = new Country("US", "United States", "North America");
            countryRepository.save(us);
        }
    }

    @Test
    void createHoliday_returns201() throws Exception {
        CreateHolidayRequest request = new CreateHolidayRequest();
        request.setCountryCode("US");
        request.setHolidayDate(LocalDate.of(2026, 7, 4));
        request.setDescription("Independence Day");

        mockMvc.perform(post("/api/master-data/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryCode").value("US"))
                .andExpect(jsonPath("$.holidayDate").value("2026-07-04"))
                .andExpect(jsonPath("$.description").value("Independence Day"));
    }

    @Test
    void checkIsHoliday_returnsTrue() throws Exception {
        CreateHolidayRequest request = new CreateHolidayRequest();
        request.setCountryCode("US");
        request.setHolidayDate(LocalDate.of(2026, 1, 1));
        request.setDescription("New Year's Day");

        mockMvc.perform(post("/api/master-data/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/master-data/holidays/check")
                        .param("countryCode", "US")
                        .param("date", "2026-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getHolidaysForYear_returns200() throws Exception {
        CreateHolidayRequest newYear = new CreateHolidayRequest();
        newYear.setCountryCode("US");
        newYear.setHolidayDate(LocalDate.of(2026, 1, 1));
        newYear.setDescription("New Year's Day");

        CreateHolidayRequest independence = new CreateHolidayRequest();
        independence.setCountryCode("US");
        independence.setHolidayDate(LocalDate.of(2026, 7, 4));
        independence.setDescription("Independence Day");

        mockMvc.perform(post("/api/master-data/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newYear)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/master-data/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(independence)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/master-data/holidays")
                        .param("countryCode", "US")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
