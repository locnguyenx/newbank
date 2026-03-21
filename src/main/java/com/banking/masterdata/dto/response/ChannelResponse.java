package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Channel;

import java.time.LocalDateTime;

public class ChannelResponse {

    private String code;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChannelResponse() {
    }

    public static ChannelResponse fromEntity(Channel channel) {
        ChannelResponse response = new ChannelResponse();
        response.code = channel.getCode();
        response.name = channel.getName();
        response.active = channel.isActive();
        if (channel.getAudit() != null) {
            response.createdAt = channel.getAudit().getCreatedAt();
            response.updatedAt = channel.getAudit().getUpdatedAt();
        }
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
