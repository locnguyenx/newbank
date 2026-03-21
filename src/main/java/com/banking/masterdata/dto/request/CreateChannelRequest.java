package com.banking.masterdata.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateChannelRequest {

    @NotBlank(message = "Channel code is required")
    private String code;

    @NotBlank(message = "Channel name is required")
    private String name;

    public CreateChannelRequest() {
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
}
