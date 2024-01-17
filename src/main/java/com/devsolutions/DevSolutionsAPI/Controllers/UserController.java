package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.UserCompact;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final CheckCookie checkCookie;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
        this.checkCookie = new CheckCookie(userService);
    }

    @PostMapping("/login")
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
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged in " + username);
    }

    @PostMapping("/register")
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

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("Authentication", null);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().body("Logged out");
    }

    @GetMapping("/user")
    public ResponseEntity<Optional<UserCompact>> getUser(HttpServletRequest request){
        Optional<Users> user = checkCookie.CheckCookieForUser(request);

        if (user.isEmpty())
            return ResponseEntity.status(401).build();

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