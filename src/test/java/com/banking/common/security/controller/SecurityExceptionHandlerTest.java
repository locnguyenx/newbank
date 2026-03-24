package com.banking.common.security.controller;

import com.banking.common.security.auth.exception.InvalidCredentialsException;
import com.banking.common.security.exception.InvalidTokenException;
import com.banking.common.security.exception.TokenExpiredException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.banking.BankingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Disabled("Requires test-specific controller endpoints to throw exceptions")
class SecurityExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void handleBadCredentialsException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test/bad-credentials"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void handleInvalidCredentialsException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test/invalid-credentials"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void handleInvalidTokenException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test/invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void handleTokenExpiredException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test/token-expired"))
                .andExpect(status().isUnauthorized());
    }
}
