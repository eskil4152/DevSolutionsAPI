package com.devsolutions.DevSolutionsAPI.Tools;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"getEnvProfile"})
public class GetVariables {
    private static boolean isLocal;
    private static boolean isTest;
    private static Dotenv dotenv;

    @Autowired
    public GetVariables(GetEnvProfile getEnvProfile) {
        isLocal = getEnvProfile.getProfiles().contains("dev");
        isTest = getEnvProfile.getProfiles().contains("test");

        dotenv = isLocal ? Dotenv.configure().load() : null;
    }

    public static String getSecret() {
        if (isTest){
            System.out.println("IT IS TEST ENVIRONMENT");
            return "Test-secret-123";
        }

        String secret = System.getenv("JWT_SECRET");
        return (secret != null) ? secret : (isLocal ? dotenv.get("JWT_SECRET") : null);
    }
}
