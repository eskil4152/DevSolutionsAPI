package com.devsolutions.DevSolutionsAPI.Tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class GetEnvProfile {
    private final Environment environment;

    public GetEnvProfile(Environment environment) {
        this.environment = environment;
    }

    public Collection<String> getProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }
}
