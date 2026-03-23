package com.banking.common.security.auth;

import com.banking.common.security.auth.dto.LoginRequest;
import com.banking.common.security.auth.dto.RefreshTokenRequest;
import com.banking.common.security.auth.dto.TokenResponse;
import com.banking.common.security.entity.RefreshTokenRepository;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private LoginRequest validLoginRequest;
    private RefreshTokenRequest validRefreshRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@bank.com");
        validLoginRequest.setPassword("password123");

        validRefreshRequest = new RefreshTokenRequest();
        validRefreshRequest.setRefreshToken("refresh-token-value");

        tokenResponse = new TokenResponse("access-token", "refresh-token", 900000L);
    }

    @Test
    void login_shouldReturnTokenResponse() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.expiresIn").value(900000))
                .andExpect(jsonPath("$.mfaRequired").value(false));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withMissingEmail_shouldReturn400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withMissingPassword_shouldReturn400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@bank.com");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_shouldReturnNewTokenResponse() throws Exception {
        when(authService.refresh(any(RefreshTokenRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.expiresIn").value(900000))
                .andExpect(jsonPath("$.mfaRequired").value(false));
    }

    @Test
    void refresh_withInvalidToken_shouldReturn401() throws Exception {
        when(authService.refresh(any(RefreshTokenRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid refresh token"));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRefreshRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_withMissingToken_shouldReturn400() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}