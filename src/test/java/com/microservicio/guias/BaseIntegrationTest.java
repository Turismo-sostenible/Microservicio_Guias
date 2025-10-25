package com.microservicio.guias;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

@Testcontainers
@ActiveProfiles("test") 
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "spring.cloud.bootstrap.enabled=false",
        "spring.datasource.defer-initialization=true", 
        "spring.sql.init.mode=never" 
    }
)
@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
public abstract class BaseIntegrationTest {

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

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Environment env = applicationContext.getEnvironment();
            if (!env.acceptsProfiles(org.springframework.core.env.Profiles.of("ci"))) {
                System.out.println("CI profile not active, starting Testcontainers...");
                Startables.deepStart(Stream.of(postgres, mongo, rabbit)).join();

                System.out.println("Testcontainers started. Setting properties...");
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
                 org.springframework.boot.test.util.TestPropertyValues.of(
                    "spring.datasource.driver-class-name=org.postgresql.Driver"
                 ).applyTo(applicationContext.getEnvironment());
                 System.out.println("Driver class set for CI profile via TestPropertyValues.");
            }
        }
    }
}