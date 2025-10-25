package com.microservicio.guias;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
// ¡Importa esto!
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test") 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE) 
public abstract class BaseIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("guias_db_command")
        .withUsername("user")
        .withPassword("password");
        
    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0")
        .withExposedPorts(27017); 

    @Container
    static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.11-management")
        .withUser("user", "password");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        String activeProfile = System.getProperty("spring.profiles.active", "");
        
        if (!activeProfile.contains("ci")) {
            postgres.start();
            mongo.start();
            rabbit.start();

            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
            
            String mongoUri = String.format("mongodb://%s:%d/guias_db_query", 
                                            mongo.getHost(), 
                                            mongo.getMappedPort(27017));
            registry.add("spring.data.mongodb.uri", () -> mongoUri);

            registry.add("spring.rabbitmq.host", rabbit::getHost);
            registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
            registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
            registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
        }
    }
}