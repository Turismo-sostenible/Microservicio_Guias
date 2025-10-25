package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper;

import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.DisponibilidadDiariaEntity;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.FranjaHorariaEmbeddable;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GuiaJpaMapperTest {

    private GuiaJpaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GuiaJpaMapper();
    }

    @Test
    @DisplayName("Debería mapear Guia (Dominio) a GuiaJpaEntity")
    void deberiaMapearDeDominioAJpa() {
        Guia guia = Guia.crear("Test", "test@test.com", "123");
        List<DisponibilidadDiaria> disponibilidad = List.of(
            new DisponibilidadDiaria(
                DayOfWeek.TUESDAY,
                true,
                List.of(new FranjaHoraria(LocalTime.of(10, 0), LocalTime.of(11, 0)))
            )
        );
        guia.actualizarDisponibilidad(disponibilidad);

        GuiaJpaEntity entity = mapper.toJpaEntity(guia);

        assertEquals(guia.getId().value(), entity.getId());
        assertEquals("Test", entity.getNombre());
        assertEquals("test@test.com", entity.getEmail());
        assertEquals(GuiaJpaEntity.EstadoGuiaJpa.ACTIVO, entity.getEstado());
        assertEquals(1, entity.getDisponibilidadSemanal().size());
        
        DisponibilidadDiariaEntity dispEntity = entity.getDisponibilidadSemanal().get(0);
        assertEquals(DayOfWeek.TUESDAY, dispEntity.getDia());
        assertTrue(dispEntity.isDisponible());
        assertEquals(entity, dispEntity.getGuia());
        assertEquals(1, dispEntity.getFranjas().size());
        assertEquals(LocalTime.of(10, 0), dispEntity.getFranjas().get(0).getHoraInicio());
    }

    @Test
    @DisplayName("Debería mapear GuiaJpaEntity a Guia (Dominio)")
    void deberiaMapearDeJpaADominio() {
        GuiaJpaEntity entity = new GuiaJpaEntity();
        UUID uuid = UUID.randomUUID();
        entity.setId(uuid);
        entity.setNombre("JPA");
        entity.setEmail("jpa@test.com");
        entity.setTelefono("555");
        entity.setEstado(GuiaJpaEntity.EstadoGuiaJpa.INACTIVO);
        
        DisponibilidadDiariaEntity dispEntity = new DisponibilidadDiariaEntity();
        dispEntity.setGuia(entity);
        dispEntity.setDia(DayOfWeek.FRIDAY);
        dispEntity.setDisponible(true);
        dispEntity.setFranjas(List.of(
            new FranjaHorariaEmbeddable(LocalTime.of(14, 0), LocalTime.of(15, 0))
        ));
        entity.setDisponibilidadSemanal(List.of(dispEntity));

        Guia guia = mapper.toDomainEntity(entity);

        assertEquals(entity.getId(), guia.getId().value());
        assertEquals("JPA", guia.getNombre());
        assertEquals("jpa@test.com", guia.getEmail());
        assertEquals(Guia.EstadoGuia.INACTIVO, guia.getEstado());
        assertEquals(1, guia.getDisponibilidadSemanal().size());

        DisponibilidadDiaria dispDomain = guia.getDisponibilidadSemanal().get(0);
        assertEquals(DayOfWeek.FRIDAY, dispDomain.dia());
        assertTrue(dispDomain.disponible());
        assertEquals(1, dispDomain.franjas().size());
        assertEquals(LocalTime.of(14, 0), dispDomain.franjas().get(0).horaInicio());
    }
}