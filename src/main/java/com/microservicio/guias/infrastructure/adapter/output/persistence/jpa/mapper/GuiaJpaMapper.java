
package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper;

import com.microservicio.guias.domain.model.*;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.DisponibilidadDiariaEmbeddable;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.FranjaHorariaEmbeddable;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;

import org.springframework.stereotype.Component;
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

        entity.setFranjas(guia.getDisponibilidadSemanal().stream()
                .map(this::toDisponibilidadEmbeddable)
                .collect(Collectors.toList()));
        
        return entity;
    }

    private DisponibilidadDiariaEmbeddable toDisponibilidadEmbeddable(DisponibilidadDiaria disponibilidad) {
        var embeddable = new DisponibilidadDiariaEmbeddable();
        embeddable.setDia(disponibilidad.dia());
        embeddable.setDisponible(disponibilidad.disponible());
        embeddable.setFranjas(disponibilidad.franjas().stream()
                .map(f -> new FranjaHorariaEmbeddable(f.horaInicio(), f.horaFin()))
                .collect(Collectors.toList()));
        return embeddable;
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
                entity.getFranjas().stream()
                        .map(this::toDisponibilidadDomain)
                        .collect(Collectors.toList())
        );
    }

    private DisponibilidadDiaria toDisponibilidadDomain(DisponibilidadDiariaEmbeddable embeddable) {
        return new DisponibilidadDiaria(
                embeddable.getDia(),
                embeddable.isDisponible(),
                embeddable.getFranjas().stream()
                        .map(f -> new FranjaHoraria(f.getHoraInicio(), f.getHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}