package com.banking.masterdata.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateDocumentTypeRequest {

    @NotBlank(message = "Document type code is required")
    private String code;

    @NotBlank(message = "Document type name is required")
    private String name;

    @NotBlank(message = "Document type category is required")
    private String category;

    public CreateDocumentTypeRequest() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
