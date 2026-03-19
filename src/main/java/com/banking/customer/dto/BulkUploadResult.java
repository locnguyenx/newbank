package com.banking.customer.dto;

import java.util.ArrayList;
import java.util.List;

public class BulkUploadResult {

    private int successCount;
    private int failureCount;
    private List<String> errors = new ArrayList<>();

    public BulkUploadResult() {
    }

    public BulkUploadResult(int successCount, int failureCount, List<String> errors) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void incrementSuccessCount() {
        this.successCount++;
    }

    public void incrementFailureCount() {
        this.failureCount++;
    }
}
