package com.devsolutions.DevSolutionsAPI;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    private static final String SECRET = "secret-tester";
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Token ";
    private static final String ROLE_CLAIM = "role";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWTfilter is being executed");
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && Jwts.parser().setSigningKey(SECRET).isSigned(jwt)) {
                Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody();
                String username = claims.getSubject();
                String role = claims.get(ROLE_CLAIM, String.class);

                if (username != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, getAuthorities(role));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            // Handle expired token exception
        }

        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        if (role == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        System.out.println("Trying extract");
        if (bearerToken != null) {
            System.out.println("Not null");
            if (bearerToken.startsWith(TOKEN_PREFIX)){
                String token = bearerToken.replace(TOKEN_PREFIX, "");
                System.out.println("Token ex: " + token);
                return bearerToken.replace(TOKEN_PREFIX, "");
            }
        }

        System.out.println("Was null");
        return null;
    }
}
