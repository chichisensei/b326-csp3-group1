package com.joysistvi.CyberAccess.model;

import java.time.LocalDateTime;

public class Users {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    private int id;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private String status;
    private LocalDateTime createdAt;

    public Users() {
    }

    public Users(
            String fullName,
            String username,
            String password,
            String role,
            String status) {

        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Users(
            int id,
            String fullName,
            String username,
            String password,
            String role,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(role);
    }

    @Override
    public String toString() {

        return String.format(
                "%-4d %-25s %-15s %-10s %-10s",
                id,
                fullName,
                username,
                role,
                status
        );
    }
}