package com.banking.common.security.jwt;

import com.banking.common.security.config.JwtConfig;
import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.entity.UserType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("your-256-bit-secret-key-here-change-in-production");
        jwtConfig.setAccessTokenExpiry(900000L);
        jwtTokenProvider = new JwtTokenProvider(jwtConfig);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldExtractTokenFromAuthorizationHeader() throws Exception {
        User user = new User();
        user.setEmail("test@bank.com");
        user.setUserType(UserType.INTERNAL);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String validToken = jwtTokenProvider.generateAccessToken(1L, "test@bank.com", "INTERNAL");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotAuthenticateWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotAuthenticateWhenInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenDoesNotStartWithBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}