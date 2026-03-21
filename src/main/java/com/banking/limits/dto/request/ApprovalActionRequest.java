package com.banking.limits.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ApprovalActionRequest {

    @NotBlank(message = "Action is required")
    private String action;

    private String reason;

    public ApprovalActionRequest() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
