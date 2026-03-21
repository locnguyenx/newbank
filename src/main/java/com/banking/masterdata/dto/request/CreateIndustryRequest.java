package com.banking.masterdata.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateIndustryRequest {

    @NotBlank(message = "Industry code is required")
    @Size(min = 1, max = 10, message = "Industry code must be between 1 and 10 characters")
    private String code;

    @NotBlank(message = "Industry name is required")
    private String name;

    private String parentCode;

    public CreateIndustryRequest() {
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
