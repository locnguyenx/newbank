package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductFeature;

public class ProductFeatureResponse {

    private Long id;
    private String featureKey;
    private String featureValue;

    public ProductFeatureResponse() {
    }

    public static ProductFeatureResponse fromEntity(ProductFeature feature) {
        ProductFeatureResponse response = new ProductFeatureResponse();
        response.id = feature.getId();
        response.featureKey = feature.getFeatureKey();
        response.featureValue = feature.getFeatureValue();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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