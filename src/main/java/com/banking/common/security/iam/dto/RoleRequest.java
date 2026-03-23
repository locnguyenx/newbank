package com.banking.common.security.iam.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class RoleRequest {

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;

    private List<String> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}