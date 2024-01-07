package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.RequestBodies.TokenRequest;
import com.devsolutions.DevSolutionsAPI.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/api/tokentest")
    public ResponseEntity<String> TokenTest(){
        String token = JwtUtil.generateToken("eskil", "ADMIN");
        System.out.println("Token: " + token);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/api/tokentest2")
    public ResponseEntity<String> TokenTest2(){
        String token = JwtUtil.generateToken("eskil", "USER");
        System.out.println("Token: " + token);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/checktoken")
    public void TokenCheck(@RequestBody TokenRequest tokenRequest){
        String token = tokenRequest.getToken();
        System.out.println("Token: " + token);

        System.out.println("Token-level: " + JwtUtil.parseToken(token));
    }
}

