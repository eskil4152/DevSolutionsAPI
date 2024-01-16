package com.devsolutions.DevSolutionsAPI.Security;

import com.devsolutions.DevSolutionsAPI.Tools.GetVariables;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    private static final String SECRET = GetVariables.getSecret();
    private static final String COOKIE_NAME = "Authentication";
    private static final String ROLE_CLAIM = GetVariables.getRoleClaim();

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException
    { try {
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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME != null){
                    if (COOKIE_NAME.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                } else {
                    System.out.println("ERROR: Variable COOKIE_NAME not found, or value is null");
                }
            }
        }
        return null;
    }
}
