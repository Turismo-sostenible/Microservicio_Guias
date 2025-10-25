package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper;

import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.DisponibilidadDiariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.FranjaHorariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GuiaMongoMapperTest {

    private GuiaMongoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GuiaMongoMapper();
    }

    @Test
    @DisplayName("Debería mapear Guia (Dominio) a GuiaMongoDocument")
    void deberiaMapearDeDominioAMongo() {
        Guia guia = Guia.crear("Mongo", "mongo@test.com", "999");
        List<DisponibilidadDiaria> disponibilidad = List.of(
            new DisponibilidadDiaria(
                DayOfWeek.SATURDAY,
                true,
                List.of(new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(13, 0)))
            ),
            new DisponibilidadDiaria(DayOfWeek.SUNDAY, false, List.of())
        );
        guia.actualizarDisponibilidad(disponibilidad);

        GuiaMongoDocument doc = mapper.toDocument(guia);

        assertEquals(guia.getId().value().toString(), doc.getId());
        assertEquals("Mongo", doc.getNombre());
        assertEquals("mongo@test.com", doc.getEmail());
        assertEquals("ACTIVO", doc.getEstado());
        assertEquals(2, doc.getHorarios().size());

        DisponibilidadDiariaDocument dispDoc = doc.getHorarios().get(0);
        assertEquals("SATURDAY", dispDoc.getDia());
        assertTrue(dispDoc.isDisponible());
        assertEquals(1, dispDoc.getFranjas().size());
        assertEquals(LocalTime.of(9, 0), dispDoc.getFranjas().get(0).getHoraInicio());

        DisponibilidadDiariaDocument dispDoc2 = doc.getHorarios().get(1);
        assertEquals("SUNDAY", dispDoc2.getDia());
        assertFalse(dispDoc2.isDisponible());
        assertTrue(dispDoc2.getFranjas().isEmpty());
    }

    @Test
    @DisplayName("Debería mapear GuiaMongoDocument a Guia (Dominio)")
    void deberiaMapearDeMongoADominio() {
        GuiaMongoDocument doc = new GuiaMongoDocument();
        UUID id = UUID.randomUUID();
        doc.setId(id.toString());
        doc.setNombre("Doc");
        doc.setEmail("doc@test.com");
        doc.setEstado("INACTIVO");
        
        DisponibilidadDiariaDocument dispDoc = new DisponibilidadDiariaDocument();
        dispDoc.setDia("WEDNESDAY");
        dispDoc.setDisponible(true);
        dispDoc.setFranjas(List.of(
            new FranjaHorariaDocument(LocalTime.of(15, 0), LocalTime.of(16, 0))
        ));
        doc.setHorarios(List.of(dispDoc));

        Guia guia = mapper.toDomainEntity(doc);

        assertEquals(doc.getId(), guia.getId().value().toString());
        assertEquals("Doc", guia.getNombre());
        assertEquals(Guia.EstadoGuia.INACTIVO, guia.getEstado());
        assertEquals(1, guia.getDisponibilidadSemanal().size());

        DisponibilidadDiaria dispDomain = guia.getDisponibilidadSemanal().get(0);
        assertEquals(DayOfWeek.WEDNESDAY, dispDomain.dia());
        assertTrue(dispDomain.disponible());
        assertEquals(1, dispDomain.franjas().size());
        assertEquals(LocalTime.of(15, 0), dispDomain.franjas().get(0).horaInicio());
    }
}