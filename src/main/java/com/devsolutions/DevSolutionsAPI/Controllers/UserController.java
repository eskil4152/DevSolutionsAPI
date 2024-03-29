package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.UserCompact;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.C;
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
        this.userService = userService;;

        this.checkCookie = new CheckCookie(userService);
    }

    @PostMapping("/login")
    public ResponseEntity<Optional<UserRole>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Users> user = userService.login(username, password);

        if (user.isEmpty())
            return ResponseEntity.status(401).body(Optional.empty());

        UserRole role = user.get().getRole();
        String token = JwtUtil.generateToken(username, role);

        Cookie cookie = new Cookie("Authentication", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(Optional.of(role));
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<UserRole>> register(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String firstname = loginRequest.getFirstname();
        String lastname = loginRequest.getLastname();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();

        var user = userService.register(firstname, lastname, username, password, email);

        if (user.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        String token = JwtUtil.generateToken(username, UserRole.USER);

        Cookie cookie = new Cookie("Authentication", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(Optional.of(UserRole.USER));
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

    @GetMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("Authentication", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}