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

    String firstname;
    String lastname;
    String username;
    String password;
    String email;

    @Enumerated(EnumType.STRING)
    UserRole role;

    public Accounts() {
    }

    public Long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Accounts(String firstname, String lastname, String username, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = UserRole.USER;
    }
}



