package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.repository;

import com.microservicio.guias.BaseIntegrationTest;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper.GuiaMongoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({MongoGuiaQueryRepositoryAdapter.class, GuiaMongoMapper.class})
class MongoGuiaQueryRepositoryAdapterIT extends BaseIntegrationTest {

    @Autowired
    private MongoGuiaQueryRepositoryAdapter adapter;

    @Autowired
    private SpringDataGuiaMongoRepository mongoRepository;

    @AfterEach
    void limpiar() {
        mongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería guardar y leer un Guía del Read Model en MongoDB")
    void deberiaGuardarYLeerEnMongo() {
        Guia guia = Guia.crear("Guia Mongo", "mongo@test.com", "777");
        List<DisponibilidadDiaria> disponibilidad = List.of(
            new DisponibilidadDiaria(
                DayOfWeek.FRIDAY,
                true,
                List.of(new FranjaHoraria(LocalTime.of(16, 0), LocalTime.of(18, 0)))
            )
        );
        guia.actualizarDisponibilidad(disponibilidad);

        adapter.save(guia);

        Optional<Guia> guiaEncontradoOpt = adapter.findById(guia.getId());
        assertTrue(guiaEncontradoOpt.isPresent());
        assertEquals("Guia Mongo", guiaEncontradoOpt.get().getNombre());
        
        List<Guia> guias = adapter.findAll();
        assertEquals(1, guias.size());
        
        Guia guiaEncontrado = guias.get(0);
        assertEquals(1, guiaEncontrado.getDisponibilidadSemanal().size());
        assertEquals(DayOfWeek.FRIDAY, guiaEncontrado.getDisponibilidadSemanal().get(0).dia());
    }

    @Test
    @DisplayName("Debería eliminar un Guía del Read Model en MongoDB")
    void deberiaEliminarDeMongo() {
        Guia guia = Guia.crear("Para Borrar Mongo", "borrar-mongo@test.com", "000");
        adapter.save(guia);
        assertEquals(1, mongoRepository.count());

        adapter.deleteById(guia.getId());

        assertEquals(0, mongoRepository.count());
        assertFalse(adapter.findById(guia.getId()).isPresent());
    }
}