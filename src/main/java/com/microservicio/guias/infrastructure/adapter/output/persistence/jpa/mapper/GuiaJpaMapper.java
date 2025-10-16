
package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper;

import com.microservicio.guias.domain.model.*;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.DisponibilidadDiariaEntity;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.FranjaHorariaEmbeddable;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuiaJpaMapper {

    // --- De Dominio a Entidad JPA ---
    public GuiaJpaEntity toJpaEntity(Guia guia) {
        GuiaJpaEntity entity = new GuiaJpaEntity();
        entity.setId(guia.getId().value());
        entity.setNombre(guia.getNombre());
        entity.setEmail(guia.getEmail());
        entity.setTelefono(guia.getTelefono());
        entity.setEstado(GuiaJpaEntity.EstadoGuiaJpa.valueOf(guia.getEstado().name()));

        List<DisponibilidadDiariaEntity> disponibilidadEntities = guia.getDisponibilidadSemanal().stream()
                .map(d -> toDisponibilidadEntity(d, entity)) // Pasamos la entidad Guia como referencia
                .collect(Collectors.toList());
        entity.setDisponibilidadSemanal(disponibilidadEntities);
        
        return entity;
    }

    private DisponibilidadDiariaEntity toDisponibilidadEntity(DisponibilidadDiaria disponibilidad, GuiaJpaEntity guia) {
        var entity = new DisponibilidadDiariaEntity();
        entity.setGuia(guia);
        entity.setDia(disponibilidad.dia());
        entity.setDisponible(disponibilidad.disponible());
        entity.setFranjas(disponibilidad.franjas().stream()
                .map(f -> new FranjaHorariaEmbeddable(f.horaInicio(), f.horaFin()))
                .collect(Collectors.toList()));
        return entity;
    }
 

    // --- De Entidad JPA a Dominio ---
    public Guia toDomainEntity(GuiaJpaEntity entity) {
        return Guia.reconstitute(
                new GuiaId(entity.getId()),
                entity.getNombre(),
                entity.getEmail(),
                entity.getTelefono(),
                Guia.EstadoGuia.valueOf(entity.getEstado().name()),
                // NUEVO: Mapeo de vuelta a la disponibilidad del dominio
                entity.getDisponibilidadSemanal().stream()
                        .map(this::toDisponibilidadDomain)
                        .collect(Collectors.toList())
        );
    }

    private DisponibilidadDiaria toDisponibilidadDomain(DisponibilidadDiariaEntity entity) {
        return new DisponibilidadDiaria(
                entity.getDia(),
                entity.isDisponible(),
                entity.getFranjas().stream()
                        .map(f -> new FranjaHoraria(f.getHoraInicio(), f.getHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}