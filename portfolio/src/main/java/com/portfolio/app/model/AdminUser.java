package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_users")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @NotBlank(message = "Password hash is required")
    @Column(nullable = false, length = 255)
    private String password;

    private String role = "ADMIN";

    private LocalDateTime updatedAt;

    public AdminUser() {
    }

    public AdminUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "ADMIN";
        this.updatedAt = LocalDateTime.now();
    }

    public AdminUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

