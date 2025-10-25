package com.microservicio.guias;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
// Removed DynamicPropertyRegistry/Source imports
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

@Testcontainers
@ActiveProfiles("test") // Ensures application-test.yml is loaded first
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "spring.cloud.bootstrap.enabled=false",
        // Defer DataSource initialization until properties are set
        "spring.datasource.defer-initialization=true", // Requires Spring Boot 2.5+
        "spring.sql.init.mode=never" // Avoid schema init attempts before properties are set
    }
)
// Use ContextConfiguration to apply conditional logic *before* context refresh
@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
public abstract class BaseIntegrationTest {

    // Keep @Container for lifecycle management by JUnit Jupiter extension
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("guias_db_command")
        .withUsername("user")
        .withPassword("password");

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0")
        .withExposedPorts(27017);

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.11-management")
        .withUser("user", "password");

    // Inner static class for the Initializer
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Environment env = applicationContext.getEnvironment();
            // Check if the 'ci' profile is NOT active
            if (!env.acceptsProfiles(org.springframework.core.env.Profiles.of("ci"))) {
                System.out.println("CI profile not active, starting Testcontainers...");
                // Start containers ONLY if 'ci' is not active
                // Use Startables for parallel startup
                Startables.deepStart(Stream.of(postgres, mongo, rabbit)).join();

                System.out.println("Testcontainers started. Setting properties...");
                // Set properties dynamically using the TestPropertyValues API
                org.springframework.boot.test.util.TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.datasource.driver-class-name=" + postgres.getDriverClassName(), // Explicitly add driver class
                    "spring.data.mongodb.uri=" + String.format("mongodb://%s:%d/guias_db_query", mongo.getHost(), mongo.getMappedPort(27017)),
                    "spring.rabbitmq.host=" + rabbit.getHost(),
                    "spring.rabbitmq.port=" + rabbit.getAmqpPort(),
                    "spring.rabbitmq.username=" + rabbit.getAdminUsername(),
                    "spring.rabbitmq.password=" + rabbit.getAdminPassword()
                ).applyTo(applicationContext.getEnvironment());
                 System.out.println("Properties set for Testcontainers.");
            } else {
                 System.out.println("CI profile active, Testcontainers will not be started by Initializer.");
                 // In CI, properties come from application-test.yml (ci profile) + env vars
                 // Explicitly set driver class name just in case the URL isn't enough
                 org.springframework.boot.test.util.TestPropertyValues.of(
                    "spring.datasource.driver-class-name=org.postgresql.Driver"
                 ).applyTo(applicationContext.getEnvironment());
                 System.out.println("Driver class set for CI profile via TestPropertyValues.");
            }
        }
    }
}