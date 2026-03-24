package com.banking.common.security.iam.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BulkImportRequest {

    @NotNull(message = "Users list is required")
    private List<UserRequest> users;

    public List<UserRequest> getUsers() {
        return users;
    }

    public void setUsers(List<UserRequest> users) {
        this.users = users;
    }
}