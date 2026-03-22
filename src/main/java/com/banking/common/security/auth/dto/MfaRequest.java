package com.banking.common.security.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class MfaRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "MFA code is required")
    private String code;

    public MfaRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
