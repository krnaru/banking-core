package org.example.corebanking.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
/*
 * Configuration class for the database.
 * It uses the Spring's @Configuration annotation to indicate that it's a configuration class.
 * It also uses the @Value annotation to inject values from the application.properties file.
 */
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * Bean for the DataSource.
     * It uses the DataSourceBuilder to build the DataSource with the provided database URL, username, and password.
     * @return the configured DataSource
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
