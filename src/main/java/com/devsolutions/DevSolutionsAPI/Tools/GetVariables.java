package com.devsolutions.DevSolutionsAPI.Tools;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"getEnvProfile"})
public class GetVariables {
    private static boolean isLocal;
    private static Dotenv dotenv;

    @Autowired
    public GetVariables(GetEnvProfile getEnvProfile) {
        isLocal = getEnvProfile.getProfiles().contains("dev");
        dotenv = isLocal ? Dotenv.configure().load() : null;
    }

    public static String getSecret() {
        String secret = System.getenv("JWT_SECRET");
        return (secret != null) ? secret : (isLocal ? dotenv.get("JWT_SECRET") : null);
    }

    public static String getRoleClaim() {
        String roleClaim = System.getenv("ROLE_CLAIM");
        return (roleClaim != null) ? roleClaim : (isLocal ? dotenv.get("ROLE_CLAIM") : null);
    }
}
