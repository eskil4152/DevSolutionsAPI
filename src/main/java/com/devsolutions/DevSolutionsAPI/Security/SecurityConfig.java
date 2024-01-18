package com.devsolutions.DevSolutionsAPI.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorization -> authorization
                    .requestMatchers("/api/admin/**").hasAnyRole("OWNER", "ADMIN")
                    .requestMatchers("/api/moderator/**").hasAnyRole("OWNER", "ADMIN", "MODERATOR")
                    .requestMatchers("/api/user/**").authenticated()
                    .anyRequest().permitAll())
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                    .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
