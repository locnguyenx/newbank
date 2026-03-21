package com.banking.masterdata.controller;

import com.banking.masterdata.MasterDataTestApplication;
import com.banking.masterdata.dto.request.CreateChannelRequest;
import com.banking.masterdata.repository.ChannelRepository;
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
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
    }

    @Test
    void createChannel_returns201() throws Exception {
        CreateChannelRequest request = new CreateChannelRequest();
        request.setCode("ONLINE");
        request.setName("Online Banking");

        mockMvc.perform(post("/api/master-data/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ONLINE"))
                .andExpect(jsonPath("$.name").value("Online Banking"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void listChannels_returns200() throws Exception {
        CreateChannelRequest online = new CreateChannelRequest();
        online.setCode("ONLINE");
        online.setName("Online Banking");

        CreateChannelRequest mobile = new CreateChannelRequest();
        mobile.setCode("MOBILE");
        mobile.setName("Mobile Banking");

        mockMvc.perform(post("/api/master-data/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(online)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/master-data/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mobile)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/master-data/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
