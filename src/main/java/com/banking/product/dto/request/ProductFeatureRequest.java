package com.banking.product.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ProductFeatureRequest {

    @NotBlank(message = "Feature key is required")
    private String featureKey;

    @NotBlank(message = "Feature value is required")
    private String featureValue;

    public ProductFeatureRequest() {
    }

    public String getFeatureKey() {
        return featureKey;
    }

    public void setFeatureKey(String featureKey) {
        this.featureKey = featureKey;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }
}