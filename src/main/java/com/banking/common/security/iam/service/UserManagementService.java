package com.banking.common.security.iam.service;

import com.banking.common.message.MessageCatalog;
import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.entity.UserStatus;
import com.banking.common.security.iam.dto.UserRequest;
import com.banking.common.security.iam.dto.UserResponse;
import com.banking.common.security.iam.exception.RoleNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_USER_ALREADY_EXISTS));
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUserType(request.getUserType() != null ? request.getUserType() : com.banking.common.security.entity.UserType.INTERNAL);
        user.setStatus(UserStatus.ACTIVE);
        
        user = userRepository.save(user);
        return toResponse(user);
    }

    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_USER_NOT_FOUND)));
        
        user.setStatus(UserStatus.INACTIVE);
        user = userRepository.save(user);
        return toResponse(user);
    }

    public UserResponse activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_USER_NOT_FOUND)));
        
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageCatalog.getMessage(MessageCatalog.IAM_USER_NOT_FOUND)));
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listActiveUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setMfaEnabled(user.isMfaEnabled());
        response.setCreatedAt(user.getAuditFields().getCreatedAt());
        return response;
    }
}