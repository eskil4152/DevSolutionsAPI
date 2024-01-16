package com.devsolutions.DevSolutionsAPI.Tools;

import io.github.cdimascio.dotenv.Dotenv;

public class GetVariables {
    private static final boolean IS_LOCAL = "localhost".equals(System.getenv("SPRING_PROFILES_ACTIVE"));

    private static Dotenv dotenv = IS_LOCAL ? Dotenv.configure().load() : null;

    public static String getSecret() {
        String secret = System.getenv("JWT_SECRET");
        return (secret != null) ? secret : (IS_LOCAL ? dotenv.get("JWT_SECRET") : null);
    }

    public static String getRoleClaim() {
        String roleClaim = System.getenv("ROLE_CLAIM");
        return (roleClaim != null) ? roleClaim : (IS_LOCAL ? dotenv.get("ROLE_CLAIM") : null);
    }
}