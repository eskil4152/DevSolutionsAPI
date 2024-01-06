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
    Integer authorizationLevel = 1;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAuthorizationLevel() {
        return authorizationLevel;
    }

    public Accounts(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
