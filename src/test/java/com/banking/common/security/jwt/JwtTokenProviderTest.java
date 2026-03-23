package com.banking.common.security.jwt;

import com.banking.common.security.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        jwtConfig.setSecret("your-256-bit-secret-key-here-change-in-production");
        jwtConfig.setAccessTokenExpiry(900000L);
        jwtConfig.setRefreshTokenExpiry(604800000L);
        jwtTokenProvider = new JwtTokenProvider(jwtConfig);
    }

    @Test
    void shouldGenerateAccessToken() {
        String token = jwtTokenProvider.generateAccessToken(1L, "test@bank.com", "COMPANY_MAKER");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldValidateToken() {
        String token = jwtTokenProvider.generateAccessToken(1L, "test@bank.com", "COMPANY_MAKER");
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void shouldExtractUserIdFromToken() {
        String token = jwtTokenProvider.generateAccessToken(42L, "test@bank.com", "COMPANY_MAKER");
        assertEquals(42L, jwtTokenProvider.getUserIdFromToken(token));
    }

    @Test
    void shouldRejectExpiredToken() {
        JwtConfig expiredConfig = new JwtConfig();
        expiredConfig.setSecret("your-256-bit-secret-key-here-change-in-production");
        expiredConfig.setAccessTokenExpiry(0L);
        JwtTokenProvider expiredProvider = new JwtTokenProvider(expiredConfig);
        
        String token = expiredProvider.generateAccessToken(1L, "test@bank.com", "COMPANY_MAKER");
        assertFalse(expiredProvider.validateToken(token));
    }
}