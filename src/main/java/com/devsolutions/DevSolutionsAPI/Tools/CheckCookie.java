package com.devsolutions.DevSolutionsAPI.Tools;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CheckCookie {
    private final UserService userService;

    @Autowired
    public CheckCookie(UserService userService) {
        this.userService = userService;
    }

    public Optional<Users> CheckCookieForUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("Null cookie");
            return Optional.empty();
        }

        Optional<Users> cookieUser = Optional.empty();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {
                Claims claims = JwtUtil.parseToken(cookie.getValue());

                String username = claims.getSubject();

                cookieUser = userService.getUser(username);

                if (cookieUser.isPresent()) {
                    break;
                }
            }
        }

        if (cookieUser.isEmpty()) {
            System.out.println("Empty user");
            return Optional.empty();
        }

        return cookieUser;
    }

    public Optional<UserRole> CheckCookieForRole(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("Null cookie");
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {
                Claims claims = JwtUtil.parseToken(cookie.getValue());

                UserRole role = UserRole.valueOf(claims.get("role", String.class));

                return Optional.of(role);
            }
        }

        return Optional.empty();
    }
}