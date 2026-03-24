package com.banking.common.security.auth.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidCredentialsException extends BadCredentialsException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public static InvalidCredentialsException invalidCredentials() {
        return new InvalidCredentialsException("Invalid email or password");
    }
}