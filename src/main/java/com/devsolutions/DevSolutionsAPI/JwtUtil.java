package com.devsolutions.DevSolutionsAPI;

import com.devsolutions.DevSolutionsAPI.Entities.UserRole;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    static Dotenv dotenv = Dotenv.configure().load();

    private static final String SECRET = dotenv.get("JWT_SECRET");
    private static final long EXPIRATION_TIME = 3_600_000; // 24 hours
    private static final String ROLE_CLAIM = dotenv.get("ROLE_CLAIM");

    public static String generateToken(String username, UserRole role) {
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
