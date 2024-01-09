package com.devsolutions.DevSolutionsAPI.RequestBodies;

public class LoginRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
