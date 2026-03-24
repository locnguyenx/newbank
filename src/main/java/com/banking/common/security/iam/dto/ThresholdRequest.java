package com.banking.common.security.iam.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import com.banking.common.security.rbac.Role;

public class ThresholdRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Threshold is required")
    private BigDecimal threshold;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }
}