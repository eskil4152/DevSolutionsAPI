package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.UserRole;
import com.devsolutions.DevSolutionsAPI.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        var user = accountService.login(username, password);

        if (user.isEmpty())
            return ResponseEntity.status(401).body("No user matching credentials");

        UserRole role = user.get().getRole();
        String token = JwtUtil.generateToken(username, role);

        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged in " + username);
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();

        var user = accountService.register(username, password, email);

        if (user.isEmpty())
            return ResponseEntity.status(401).body("Username already registered");

        String token = JwtUtil.generateToken(username, UserRole.USER);

        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Registered " + username);
    }

    @GetMapping("/api/checktoken")
    public void TokenCheck(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                System.out.println("Cookie found: " + cookie.getValue());
                System.out.println("Auth: " + JwtUtil.parseToken(cookie.getValue()));
                return;
            }
        }

        System.out.println("Failed");
    }

    @GetMapping("/api/cookie")
    public ResponseEntity<String> GetCookie(HttpServletResponse response){
        String token = JwtUtil.generateToken("eskil", UserRole.ADMIN);
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("OK");
    }
}

