package com.devsolutions.DevSolutionsAPI.Tools;

import io.github.cdimascio.dotenv.Dotenv;

public class GetVariables {
    static Dotenv dotenv = Dotenv.configure().load();

    public static String getSecret() {
        String secret = System.getenv("JWT_SECRET");
        return (secret != null) ? secret : dotenv.get("JWT_SECRET");
    }

    public static String getRoleClaim() {
        String roleClaim = System.getenv("ROLE_CLAIM");
        return (roleClaim != null) ? roleClaim : dotenv.get("ROLE_CLAIM");
    }
}
