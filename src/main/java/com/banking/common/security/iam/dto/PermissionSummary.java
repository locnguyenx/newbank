package com.banking.common.security.iam.dto;

import java.util.List;

public class PermissionSummary {

    private String resource;
    private List<String> actions;

    public PermissionSummary() {
    }

    public PermissionSummary(String resource, List<String> actions) {
        this.resource = resource;
        this.actions = actions;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}