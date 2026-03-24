package com.banking.common.security.mfa;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "mfa_secrets")
public class MfaSecret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Column(name = "created_at")
    private Instant createdAt;

    public MfaSecret() {
    }

    public MfaSecret(Long userId, String secret) {
        this.userId = userId;
        this.secret = secret;
        this.enabled = false;
        this.verified = false;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
