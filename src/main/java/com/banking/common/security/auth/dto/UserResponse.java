package com.banking.common.security.auth.dto;

import java.util.List;

public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;
    private boolean mfaEnabled;

    public UserResponse() {
    }

    public UserResponse(Long id, String email, String fullName, List<String> roles, boolean mfaEnabled) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.mfaEnabled = mfaEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
