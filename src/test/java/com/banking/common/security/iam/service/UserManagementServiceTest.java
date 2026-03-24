package com.banking.common.security.iam.service;

import com.banking.common.message.MessageCatalog;
import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.entity.UserStatus;
import com.banking.common.security.entity.UserType;
import com.banking.common.security.iam.dto.UserRequest;
import com.banking.common.security.iam.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserManagementService userManagementService;

    private User testUser;
    private UserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "hashedPassword", UserType.INTERNAL);
        testUser.setId(1L);
        testUser.setStatus(UserStatus.ACTIVE);

        createUserRequest = new UserRequest();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setUserType(UserType.INTERNAL);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponse response = userManagementService.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals(UserStatus.ACTIVE, response.getStatus());
    }

    @Test
    void createUser_ThrowsWhenEmailExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            userManagementService.createUser(createUserRequest));
    }

    @Test
    void deactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userManagementService.deactivateUser(1L);

        assertEquals(UserStatus.INACTIVE, response.getStatus());
        verify(userRepository).save(testUser);
    }

    @Test
    void activateUser_Success() {
        testUser.setStatus(UserStatus.INACTIVE);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userManagementService.activateUser(1L);

        assertEquals(UserStatus.ACTIVE, response.getStatus());
    }

    @Test
    void getUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse response = userManagementService.getUser(1L);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void getUser_ThrowsWhenNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
            userManagementService.getUser(999L));
    }

    @Test
    void listUsers_Success() {
        User anotherUser = new User("another@example.com", "hash", UserType.INTERNAL);
        anotherUser.setId(2L);
        anotherUser.setStatus(UserStatus.ACTIVE);
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, anotherUser));

        List<UserResponse> users = userManagementService.listUsers();

        assertEquals(2, users.size());
    }

    @Test
    void listActiveUsers_Success() {
        User inactiveUser = new User("inactive@example.com", "hash", UserType.INTERNAL);
        inactiveUser.setId(2L);
        inactiveUser.setStatus(UserStatus.INACTIVE);
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, inactiveUser));

        List<UserResponse> users = userManagementService.listActiveUsers();

        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
    }
}