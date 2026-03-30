package com.hospital.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties(Environment env) {
        DataSourceProperties props = new DataSourceProperties();

        // Prefer explicit JDBC env vars (common on many platforms)
        String jdbcUrl = env.getProperty("JDBC_DATABASE_URL");
        if (jdbcUrl != null && !jdbcUrl.isBlank()) {
            props.setUrl(jdbcUrl);
            props.setUsername(env.getProperty("JDBC_DATABASE_USERNAME", "root"));
            props.setPassword(env.getProperty("JDBC_DATABASE_PASSWORD", "root"));
            props.setDriverClassName(env.getProperty("JDBC_DATABASE_DRIVER", "com.mysql.cj.jdbc.Driver"));
            return props;
        }

        // Render provides DATABASE_URL in postgres form: postgres://user:pass@host:port/db
        String databaseUrl = env.getProperty("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.isBlank()) {
            URI dbUri = URI.create(databaseUrl);
            String[] userInfo = dbUri.getUserInfo().split(":");
            String username = userInfo[0];
            String password = userInfo.length > 1 ? userInfo[1] : "";
            String url = String.format("jdbc:postgresql://%s:%d%s", dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

            props.setUrl(url);
            props.setUsername(username);
            props.setPassword(password);
            props.setDriverClassName("org.postgresql.Driver");
            return props;
        }

        // DB_URL override, e.g. Render may provide jdbc:dpg-xxxx/your_db
        String dbUrl = env.getProperty("DB_URL");
        if (dbUrl != null && !dbUrl.isBlank()) {
            String normalizedUrl = normalizeJdbcUrl(dbUrl);
            props.setUrl(normalizedUrl);
            props.setUsername(env.getProperty("DB_USERNAME", "postgres"));
            props.setPassword(env.getProperty("DB_PASSWORD", "postgres"));
            props.setDriverClassName("org.postgresql.Driver");
            return props;
        }

        // Fallback to properties/config defaults in application.properties
        return props;
    }

    private String normalizeJdbcUrl(String rawUrl) {
        if (rawUrl.startsWith("jdbc:postgresql://")) {
            return rawUrl;
        }
        if (rawUrl.startsWith("postgres://")) {
            // Convert DATABASE_URL-like URL to JDBC URL
            return rawUrl.replaceFirst("^postgres://", "jdbc:postgresql://");
        }
        if (rawUrl.startsWith("jdbc:dpg-")) {
            // Render may send JDBC URL in shorthand form; convert to postgres URL
            return rawUrl.replaceFirst("^jdbc:dpg-", "jdbc:postgresql://dpg-");
        }
        if (rawUrl.startsWith("jdbc:")) {
            // Generic fallback: ensure jdbc:postgresql prefix if missing
            return rawUrl.replaceFirst("^jdbc:[^:]+:", "jdbc:postgresql://");
        }
        return rawUrl;
    }

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}
