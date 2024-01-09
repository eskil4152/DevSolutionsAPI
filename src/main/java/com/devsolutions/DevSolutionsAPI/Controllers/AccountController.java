package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.AccountCompact;
import com.devsolutions.DevSolutionsAPI.Entities.Accounts;
import com.devsolutions.DevSolutionsAPI.Entities.UserRole;
import com.devsolutions.DevSolutionsAPI.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.AccountService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

        Cookie cookie = new Cookie("Authentication", token);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged in " + username);
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String firstname = loginRequest.getFirstname();
        String lastname = loginRequest.getLastname();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();

        System.out.println("FN: " + firstname + ", LN: " + lastname);

        var user = accountService.register(firstname, lastname, username, password, email);

        if (user.isEmpty())
            return ResponseEntity.status(401).body("Username already registered");

        String token = JwtUtil.generateToken(username, UserRole.USER);

        Cookie cookie = new Cookie("Authentication", token);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok("Registered " + username);
    }

    @GetMapping("/api/user")
    public ResponseEntity<Optional<AccountCompact>> getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("Null cookie");
            return ResponseEntity.status(404).body(Optional.empty());
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {
                Claims claims = JwtUtil.parseToken(cookie.getValue());

                String username = claims.getSubject();

                Optional<Accounts> user = accountService.getUser(username);

                if (user.isEmpty()){
                    System.out.println("Empty user");
                    return ResponseEntity.status(404).body(Optional.empty());
                }

                AccountCompact accountCompact = new AccountCompact(
                        user.get().getFirstname(),
                        user.get().getLastname(),
                        user.get().getUsername(),
                        user.get().getEmail(),
                        user.get().getRole()
                );

                return ResponseEntity.ok(Optional.of(accountCompact));
            }
        }

        return ResponseEntity.status(404).body(Optional.empty());
    }

    @GetMapping("/api/checktoken")
    public void TokenCheck(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {

                Claims claims = JwtUtil.parseToken(cookie.getValue());
                System.out.println("Cookie belongs to: " + claims.getSubject());
                System.out.println("Cookie auth level: " + claims.get("role"));

                return;
            }
        }

        System.out.println("Failed");
    }
}