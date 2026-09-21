package com.portfolio.app.service;

import com.portfolio.app.model.AdminUser;
import com.portfolio.app.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminUserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${portfolio.admin.username:admin}")
    private String configuredDefaultUsername;

    @Value("${portfolio.admin.password:admin123}")
    private String configuredDefaultPassword;

    public AdminUserService(AdminUserRepository adminUserRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole())
                .build();
    }

    @Transactional
    public void initDefaultAdminIfNotPresent() {
        if (adminUserRepository.count() == 0) {
            String encoded = passwordEncoder.encode(configuredDefaultPassword);
            AdminUser defaultAdmin = new AdminUser(configuredDefaultUsername, encoded, "ADMIN");
            adminUserRepository.save(defaultAdmin);
            log.info("Initialized default admin user '{}' in database.", configuredDefaultUsername);
        }
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public String getPrimaryAdminUsername() {
        return adminUserRepository.findAll().stream()
                .findFirst()
                .map(AdminUser::getUsername)
                .orElse(configuredDefaultUsername);
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword, String confirmPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("Please enter your current password.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin account not found for user: " + username));

        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(newPassword, admin.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as your current password.");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setUpdatedAt(LocalDateTime.now());
        adminUserRepository.save(admin);

        log.info("Admin password successfully updated for user '{}'.", username);
    }
}

