package com.banking.masterdata.dto.request;

public class UpdateDocumentTypeRequest {

    private String name;

    private String category;

    public UpdateDocumentTypeRequest() {
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
