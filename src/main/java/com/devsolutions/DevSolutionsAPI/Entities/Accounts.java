package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.*;

@Entity
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(
            name = "user_id_generator",
            allocationSize = 1
    )
    Long id;

    String username;
    String password;
    String email;

    @Enumerated(EnumType.STRING)
    UserRole role;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Accounts(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = UserRole.USER;
    }
}
