package com.banking.common.security.iam.service;

import com.banking.common.message.MessageCatalog;
import com.banking.common.security.iam.dto.BulkImportRequest;
import com.banking.common.security.iam.dto.BulkImportResult;
import com.banking.common.security.iam.dto.UserRequest;
import com.banking.common.security.iam.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulkImportService {

    private final UserManagementService userManagementService;

    public BulkImportService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    public BulkImportResult importUsers(BulkImportRequest request) {
        BulkImportResult result = new BulkImportResult(request.getUsers().size());
        
        List<UserRequest> users = request.getUsers();
        
        for (int i = 0; i < users.size(); i++) {
            UserRequest userRequest = users.get(i);
            try {
                validateUserRequest(userRequest, i);
                UserResponse created = userManagementService.createUser(userRequest);
                result.addSuccessUser(created);
            } catch (Exception e) {
                result.setFailureCount(result.getFailureCount() + 1);
                result.addError("Row " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return result;
    }

    private void validateUserRequest(UserRequest request, int rowIndex) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_INVALID_BULK_IMPORT) + 
                    " - Row " + (rowIndex + 1) + ": Email is required");
        }
        
        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_INVALID_BULK_IMPORT) + 
                    " - Row " + (rowIndex + 1) + ": Invalid email format");
        }
        
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_INVALID_BULK_IMPORT) + 
                    " - Row " + (rowIndex + 1) + ": Password must be at least 8 characters");
        }
    }
}