package com.devsolutions.DevSolutionsAPI;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String secret = "secret-tester";
    private static final long time = 86_400_000;

    private static final String prefix = "Bearer ";
    private static final String header_string = "Authorization";
    private static final String level_claim = "level";

    public static String generateToken(String username, String level){
        return Jwts.builder()
                .setSubject(username)
                .claim(level_claim, level)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
