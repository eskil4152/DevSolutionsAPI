package com.devsolutions.DevSolutionsAPI.Entities;

public class AccountCompact {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private UserRole role;

    public AccountCompact(){}

    public AccountCompact(String firstname, String lastname, String username, String email, UserRole role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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