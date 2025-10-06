package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper;

import com.microservicio.guias.domain.model.EstadoGuia;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.domain.model.Horario;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.HorarioDocument;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GuiaMongoMapper {
    public GuiaMongoDocument toDocument(Guia guia) {
        GuiaMongoDocument doc = new GuiaMongoDocument();
        doc.setId(guia.getId().value().toString());
        doc.setNombre(guia.getNombre());
        doc.setEmail(guia.getEmail());
        doc.setTelefono(guia.getTelefono());
        doc.setEstado(guia.getEstado().name());
        doc.setHorarios(guia.getHorarios().stream()
                .map(h -> new HorarioDocument(h.fechaHoraInicio(), h.fechaHoraFin()))
                .collect(Collectors.toList()));
        return doc;
    }

    public Guia toDomainEntity(GuiaMongoDocument doc) {
        return Guia.reconstitute(
                GuiaId.fromString(doc.getId()),
                doc.getNombre(),
                doc.getEmail(),
                doc.getTelefono(),
                EstadoGuia.valueOf(doc.getEstado()),
                doc.getHorarios().stream()
                        .map(h -> new Horario(h.getFechaHoraInicio(), h.getFechaHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}