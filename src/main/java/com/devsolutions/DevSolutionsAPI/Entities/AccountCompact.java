package com.devsolutions.DevSolutionsAPI.Entities;

public class AccountCompact {
    private String username;
    private String email;
    private UserRole role;

    public AccountCompact(){}

    public AccountCompact(String username, String email, UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}