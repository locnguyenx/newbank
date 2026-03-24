package com.banking.common.security.controller;

import com.banking.common.security.auth.exception.InvalidCredentialsException;
import com.banking.common.security.exception.InvalidTokenException;
import com.banking.common.security.exception.TokenExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-invalid-credentials")
    public String testInvalidCredentials() {
        throw InvalidCredentialsException.invalidCredentials();
    }

    @GetMapping("/test-token-expired")
    public String testTokenExpired() {
        throw TokenExpiredException.expired();
    }

    @GetMapping("/test-invalid-token")
    public String testInvalidToken() {
        throw InvalidTokenException.invalid();
    }

    @GetMapping("/test-bad-credentials")
    public String testBadCredentials() {
        throw new BadCredentialsException("Bad credentials");
    }
}