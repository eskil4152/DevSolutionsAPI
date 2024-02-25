package com.devsolutions.DevSolutionsAPI.Tools;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DataSourceConfig {
    private static final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .url(dotenv.get("DB_URL"))
                .username(dotenv.get("DB_USER"))
                .password(dotenv.get("DB_PASSWORD"))
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
