package com.banking.common.security.iam.dto;

import java.util.ArrayList;
import java.util.List;

public class BulkImportResult {

    private int totalCount;
    private int successCount;
    private int failureCount;
    private List<String> errors = new ArrayList<>();
    private List<UserResponse> createdUsers = new ArrayList<>();

    public BulkImportResult() {
    }

    public BulkImportResult(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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

    public List<UserResponse> getCreatedUsers() {
        return createdUsers;
    }

    public void setCreatedUsers(List<UserResponse> createdUsers) {
        this.createdUsers = createdUsers;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void addSuccessUser(UserResponse user) {
        this.createdUsers.add(user);
        this.successCount++;
    }
}