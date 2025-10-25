package com.microservicio.guias;

import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.GuiaResponse;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.repository.SpringDataGuiaJpaRepository;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.repository.SpringDataGuiaMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.cloud.bootstrap.enabled=false"
)
class CqrsFullFlowE2ETest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataGuiaJpaRepository commandRepository;

    @Autowired
    private SpringDataGuiaMongoRepository queryRepository;

    @AfterEach
    void limpiar() {
        commandRepository.deleteAll();
        queryRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería crear un Guía (API), guardarlo en Postgres, " + 
                 "publicar evento y sincronizarlo con MongoDB")
    void deberiaCrearGuiaYVerificarSincronizacionCompleta() {
        
        CreateGuiaRequest request = new CreateGuiaRequest("Guia E2E", "e2e@test.com", "12345");
        
        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
            "/admin", 
            request, 
            Void.class
        );
        
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        URI location = createResponse.getHeaders().getLocation();
        assertNotNull(location);
        
        String[] pathSegments = location.getPath().split("/");
        String id = pathSegments[pathSegments.length - 1];
        assertNotNull(id);

        Optional<GuiaJpaEntity> jpaEntity = commandRepository.findById(UUID.fromString(id));
        assertTrue(jpaEntity.isPresent(), "El guía no se guardó en Postgres");
        assertEquals("Guia E2E", jpaEntity.get().getNombre());

        await().atMost(5, TimeUnit.SECONDS)
               .pollInterval(100, TimeUnit.MILLISECONDS)
               .untilAsserted(() -> {
                    Optional<GuiaMongoDocument> mongoDoc = queryRepository.findById(id);
                    assertTrue(mongoDoc.isPresent(), "El guía no se sincronizó con MongoDB");
                    assertEquals("Guia E2E", mongoDoc.get().getNombre());
               });

        ResponseEntity<GuiaResponse> getResponse = restTemplate.getForEntity(
            "/{id}", 
            GuiaResponse.class, 
            id
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Guia E2E", getResponse.getBody().nombre());
        assertEquals(id, getResponse.getBody().id().toString());
    }
}