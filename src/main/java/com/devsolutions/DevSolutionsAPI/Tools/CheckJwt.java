package com.devsolutions.DevSolutionsAPI.Tools;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class CheckJwt {
    private final UserService userService;

    @Autowired
    public CheckJwt(UserService userService) {
        this.userService = userService;
    }

    public Optional<Users> checkJwtForUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Invalid or missing Authorization header");
            return Optional.empty();
        }

        String jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            Claims claims = JwtUtil.parseToken(jwt);

            String username = claims.getSubject();

            Optional<Users> jwtUser = userService.getUser(username);

            if (jwtUser.isPresent()) {
                return jwtUser;
            } else {
                System.out.println("User not found for the given JWT");
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("Failed to parse JWT");
            return Optional.empty();
        }
    }
}
