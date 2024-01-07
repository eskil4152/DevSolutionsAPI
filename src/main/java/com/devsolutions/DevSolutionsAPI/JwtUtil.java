package com.devsolutions.DevSolutionsAPI;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "secret-tester";
    private static final long EXPIRATION_TIME = 86_400_000; // 24 hours

    private static final String PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String ROLE_CLAIM = "role";


    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim(ROLE_CLAIM, role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
