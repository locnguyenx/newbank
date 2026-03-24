package com.banking.common.security;

import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.entity.UserType;
import com.banking.common.security.entity.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserDataLoader.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@bank.com";
        
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("Admin user already exists, skipping.");
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setUserType(UserType.INTERNAL);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setMfaEnabled(false);

        userRepository.save(admin);
        log.info("Admin user created: {} with password: admin123", adminEmail);
    }
}
