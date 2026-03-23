package com.banking.common.security.controller;

import com.banking.common.exception.BaseException;
import com.banking.common.security.auth.exception.InvalidCredentialsException;
import com.banking.common.security.exception.InvalidTokenException;
import com.banking.common.security.exception.TokenExpiredException;
import com.banking.common.security.mfa.MfaService;
import com.banking.common.audit.AuditLogService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@Import(SecurityExceptionHandler.class)
@Disabled("Requires JPA context for AuditEntityListener - to be fixed with proper test configuration")
class SecurityExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MfaService mfaService;

    @MockBean
    private AuditLogService auditLogService;

    @Test
    void handleInvalidCredentialsException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test-invalid-credentials"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messageCode").value("AUTH_001"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void handleTokenExpiredException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test-token-expired"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messageCode").value("AUTH_002"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void handleInvalidTokenException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test-invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messageCode").value("AUTH_003"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void handleBadCredentialsException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test-bad-credentials"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messageCode").value("AUTH_001"))
                .andExpect(jsonPath("$.status").value(401));
    }
}