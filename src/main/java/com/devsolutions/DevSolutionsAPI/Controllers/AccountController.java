package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        var user = accountService.login(username, password);

        if (user.isEmpty())
            return ResponseEntity.status(401).body("No user matching credentials");

        return ResponseEntity.ok("Found user");
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        var user = accountService.register(username, password);

        if (user.isEmpty())
            return ResponseEntity.status(401).body("Username already registered");

        return ResponseEntity.ok("Registered");
    }
}

