package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.UserCompact;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Entities.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
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
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        var user = userService.login(username, password);

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

        var user = userService.register(firstname, lastname, username, password, email);

        if (user.isEmpty())
            return ResponseEntity.status(409).body("Username already registered");

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
    public ResponseEntity<Optional<UserCompact>> getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("Null cookie");
            return ResponseEntity.status(401).body(Optional.empty());
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {
                Claims claims = JwtUtil.parseToken(cookie.getValue());

                String username = claims.getSubject();

                Optional<Users> user = userService.getUser(username);

                if (user.isEmpty()){
                    System.out.println("Empty user");
                    return ResponseEntity.status(401).body(Optional.empty());
                }

                UserCompact userCompact = new UserCompact(
                        user.get().getFirstname(),
                        user.get().getLastname(),
                        user.get().getUsername(),
                        user.get().getEmail(),
                        user.get().getRole()
                );

                return ResponseEntity.ok(Optional.of(userCompact));
            }
        }

        return ResponseEntity.status(401).body(Optional.empty());
    }
}