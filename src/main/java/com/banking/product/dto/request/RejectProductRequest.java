package com.banking.product.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RejectProductRequest {

    @NotBlank(message = "Rejection comment is required")
    private String comment;

    public RejectProductRequest() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}