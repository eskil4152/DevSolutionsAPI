package com.devsolutions.DevSolutionsAPI.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:3001", "https://client-aste32wdra-nw.a.run.app", "http://34.149.51.81:8080", "http://34.149.51.81")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
