package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.BulkImportRequest;
import com.banking.common.security.iam.dto.BulkImportResult;
import com.banking.common.security.iam.dto.UserRequest;
import com.banking.common.security.iam.dto.UserResponse;
import com.banking.common.security.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkImportServiceTest {

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private BulkImportService bulkImportService;

    private BulkImportRequest validRequest;
    private UserRequest validUser;

    @BeforeEach
    void setUp() {
        validUser = new UserRequest();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
        validUser.setUserType(UserType.INTERNAL);

        validRequest = new BulkImportRequest();
        validRequest.setUsers(Collections.singletonList(validUser));
    }

    @Test
    void importUsers_Success() {
        UserResponse createdResponse = new UserResponse();
        createdResponse.setId(1L);
        createdResponse.setEmail("test@example.com");
        
        when(userManagementService.createUser(any(UserRequest.class))).thenReturn(createdResponse);

        BulkImportResult result = bulkImportService.importUsers(validRequest);

        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertEquals(1, result.getCreatedUsers().size());
    }

    @Test
    void importUsers_MultipleUsers() {
        UserRequest user2 = new UserRequest();
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setUserType(UserType.INTERNAL);

        BulkImportRequest multiRequest = new BulkImportRequest();
        multiRequest.setUsers(Arrays.asList(validUser, user2));

        UserResponse response1 = new UserResponse();
        response1.setId(1L);
        response1.setEmail("test@example.com");
        
        UserResponse response2 = new UserResponse();
        response2.setId(2L);
        response2.setEmail("user2@example.com");
        
        when(userManagementService.createUser(any(UserRequest.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        BulkImportResult result = bulkImportService.importUsers(multiRequest);

        assertEquals(2, result.getTotalCount());
        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
    }

    @Test
    void importUsers_FailureInvalidEmail() {
        UserRequest invalidUser = new UserRequest();
        invalidUser.setEmail("invalid-email");
        invalidUser.setPassword("password123");

        BulkImportRequest request = new BulkImportRequest();
        request.setUsers(Collections.singletonList(invalidUser));

        BulkImportResult result = bulkImportService.importUsers(request);

        assertEquals(1, result.getTotalCount());
        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertFalse(result.getErrors().isEmpty());
    }

    @Test
    void importUsers_FailureShortPassword() {
        UserRequest invalidUser = new UserRequest();
        invalidUser.setEmail("test@example.com");
        invalidUser.setPassword("short");

        BulkImportRequest request = new BulkImportRequest();
        request.setUsers(Collections.singletonList(invalidUser));

        BulkImportResult result = bulkImportService.importUsers(request);

        assertEquals(1, result.getTotalCount());
        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
    }

    @Test
    void importUsers_PartialSuccess() {
        UserRequest validUser2 = new UserRequest();
        validUser2.setEmail("user2@example.com");
        validUser2.setPassword("password123");

        BulkImportRequest request = new BulkImportRequest();
        request.setUsers(Arrays.asList(validUser, validUser2));

        UserResponse successResponse = new UserResponse();
        successResponse.setId(1L);
        successResponse.setEmail("test@example.com");
        
        when(userManagementService.createUser(any(UserRequest.class)))
                .thenReturn(successResponse)
                .thenThrow(new IllegalArgumentException("User already exists"));

        BulkImportResult result = bulkImportService.importUsers(request);

        assertEquals(2, result.getTotalCount());
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
    }
}