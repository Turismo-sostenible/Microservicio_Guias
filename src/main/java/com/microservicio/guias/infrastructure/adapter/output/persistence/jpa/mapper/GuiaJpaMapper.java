package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper;

import com.microservicio.guias.domain.model.EstadoGuia;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.domain.model.Horario;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.HorarioEmbeddable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GuiaJpaMapper {

    public GuiaJpaEntity toJpaEntity(Guia guia) {
        GuiaJpaEntity entity = new GuiaJpaEntity();
        entity.setId(guia.getId().value());
        entity.setNombre(guia.getNombre());
        entity.setEmail(guia.getEmail());
        entity.setTelefono(guia.getTelefono());
        entity.setEstado(GuiaJpaEntity.EstadoGuiaJpa.valueOf(guia.getEstado().name()));
        entity.setHorarios(guia.getHorarios().stream()
                .map(h -> new HorarioEmbeddable(h.fechaHoraInicio(), h.fechaHoraFin()))
                .collect(Collectors.toList()));
        return entity;
    }

    public Guia toDomainEntity(GuiaJpaEntity entity) {
        return Guia.reconstitute(
                new GuiaId(entity.getId()),
                entity.getNombre(),
                entity.getEmail(),
                entity.getTelefono(),
                EstadoGuia.valueOf(entity.getEstado().name()),
                entity.getHorarios().stream()
                        .map(h -> new Horario(h.getFechaHoraInicio(), h.getFechaHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}