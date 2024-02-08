package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.UserCompact;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.RequestBodies.LoginRequest;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
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
    private final CheckJwt checkJwt;

    @Autowired
    public UserController(UserService userService, CheckJwt checkJwt){
        this.userService = userService;
        this.checkJwt = checkJwt;

        this.checkCookie = new CheckCookie(userService);
    }

    @PostMapping("/login")
    public ResponseEntity<Optional<UserRole>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        var user = userService.login(username, password);

        if (user.isEmpty())
            return ResponseEntity.status(401).body(Optional.empty());

        UserRole role = user.get().getRole();
        String token = JwtUtil.generateToken(username, role);

        response.addHeader("Authorization", "Bearer " + token);

        Cookie cookie = new Cookie("AuthCookie", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        //cookie.setSecure(true);
        cookie.setSecure(false);
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

        response.addHeader("Authorization", "Bearer " + token);

        Cookie cookie = new Cookie("AuthCookie", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        //cookie.setSecure(true);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(Optional.of(UserRole.USER));
    }

    @GetMapping("/user")
    public ResponseEntity<Optional<UserCompact>> getUser(HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

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