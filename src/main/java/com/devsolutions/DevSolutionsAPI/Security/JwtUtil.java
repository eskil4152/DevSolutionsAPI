package com.devsolutions.DevSolutionsAPI.Security;

import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Tools.GetVariables;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = GetVariables.getSecret();
    //private static final long EXPIRATION_TIME = 3_600_000; // 1 hour (ms)
    private static final long EXPIRATION_TIME = 2_147_483_647; // 596,52 hour / max int
    private static final String ROLE_CLAIM = "role";

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
