package com.banking.common.security.auth;

import com.banking.common.security.entity.UserStatus;
import com.banking.common.security.entity.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String passwordHash;
    private final UserType userType;
    private final UserStatus userStatus;

    public UserPrincipal(Long userId, String email, String passwordHash, UserType userType, UserStatus userStatus) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userType.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userStatus != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userStatus == UserStatus.ACTIVE;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
}