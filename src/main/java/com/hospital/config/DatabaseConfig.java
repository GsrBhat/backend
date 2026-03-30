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

        // DB_URL override, e.g. Render may provide jdbc:dpg-xxxx/your_db or jdbc:postgresql://user:pass@host/db
        String dbUrl = env.getProperty("DB_URL");
        if (dbUrl != null && !dbUrl.isBlank()) {
            String normalizedUrl = normalizeJdbcUrl(dbUrl);
            String username = env.getProperty("DB_USERNAME", env.getProperty("SPRING_DATASOURCE_USERNAME", "postgres"));
            String password = env.getProperty("DB_PASSWORD", env.getProperty("SPRING_DATASOURCE_PASSWORD", "postgres"));

            // If URL contains embedded user info, extract and remove it from URL.
            if (normalizedUrl.startsWith("jdbc:postgresql://")) {
                String urlWithoutAuth = stripUserInfoFromUrl(normalizedUrl);
                String[] userInfo = extractUserInfo(normalizedUrl);
                if (userInfo != null && userInfo.length == 2) {
                    username = userInfo[0];
                    password = userInfo[1];
                }
                normalizedUrl = urlWithoutAuth;
            }

            props.setUrl(normalizedUrl);
            props.setUsername(username);
            props.setPassword(password);
            props.setDriverClassName("org.postgresql.Driver");
            return props;
        }

        // Fallback from Spring standard properties when DB_URL is not set
        String springUrl = env.getProperty("SPRING_DATASOURCE_URL", env.getProperty("spring.datasource.url"));
        if (springUrl != null && !springUrl.isBlank()) {
            String normalizedUrl = normalizeJdbcUrl(springUrl);
            if (normalizedUrl.startsWith("jdbc:postgresql://")) {
                String urlWithoutAuth = stripUserInfoFromUrl(normalizedUrl);
                String[] userInfo = extractUserInfo(normalizedUrl);
                if (userInfo != null && userInfo.length == 2) {
                    props.setUsername(userInfo[0]);
                    props.setPassword(userInfo[1]);
                }
                props.setUrl(urlWithoutAuth);
            } else {
                props.setUrl(normalizedUrl);
            }
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
        if (rawUrl.startsWith("postgresql://")) {
            // Convert postgresql:// to JDBC URL
            return rawUrl.replaceFirst("^postgresql://", "jdbc:postgresql://");
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

    private String stripUserInfoFromUrl(String jdbcUrl) {
        if (!jdbcUrl.startsWith("jdbc:postgresql://")) {
            return jdbcUrl;
        }
        String noPrefix = jdbcUrl.substring("jdbc:postgresql://".length());
        int at = noPrefix.indexOf('@');
        if (at < 0) {
            return jdbcUrl;
        }
        String hostAndPath = noPrefix.substring(at + 1);
        return "jdbc:postgresql://" + hostAndPath;
    }

    private String[] extractUserInfo(String jdbcUrl) {
        if (!jdbcUrl.startsWith("jdbc:postgresql://")) {
            return null;
        }
        String noPrefix = jdbcUrl.substring("jdbc:postgresql://".length());
        int at = noPrefix.indexOf('@');
        if (at < 0) {
            return null;
        }
        String userInfo = noPrefix.substring(0, at);
        int colon = userInfo.indexOf(':');
        if (colon < 0) {
            return new String[] {userInfo, ""};
        }
        return new String[] {userInfo.substring(0, colon), userInfo.substring(colon + 1)};
    }

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        String url = dataSourceProperties.getUrl();
        if (url != null && !url.isBlank()) {
            dataSourceProperties.setUrl(normalizeJdbcUrl(url));
        }
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}

