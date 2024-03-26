package com.iwallet.bookstore.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableR2dbcRepositories
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    @Value("./db/schema.sql")
    private String schemaLocation;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(8001)
                        .username("compose-postgres")
                        .password("compose-postgres")
                        .database("bookstore")
                        .build());
    }

  /*  @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) throws IOException {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        Resource schemaScript = loadSchemaFile(schemaLocation);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaScript);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    private Resource loadSchemaFile(String schemaLocation) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(schemaLocation);
        if (resources.length == 0) {
            throw new IOException("Schema file not found: " + schemaLocation);
        }
        return resources[0];
    }*/
}
