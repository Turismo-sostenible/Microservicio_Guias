package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper;

import com.microservicio.guias.domain.model.DisponibilidadDiaria;
// import com.microservicio.guias.domain.model.EstadoGuia;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.DisponibilidadDiariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.FranjaHorariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
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
        doc.setHorarios(guia.getDisponibilidadSemanal().stream().map(this::toDisponibilidadDocument).collect(Collectors.toList()));
        return doc;
    }

    public DisponibilidadDiariaDocument toDisponibilidadDocument(DisponibilidadDiaria disponibilidad) {
        DisponibilidadDiariaDocument doc = new DisponibilidadDiariaDocument();
        doc.setDia(disponibilidad.dia().toString());
        doc.setFranjas(disponibilidad.franjas().stream()
                .map(f -> new FranjaHorariaDocument(f.horaInicio(), f.horaFin()))
                .collect(Collectors.toList()));

        return doc;
    }
    public Guia toDomainEntity(GuiaMongoDocument doc) {
        return Guia.reconstitute(
                GuiaId.fromString(doc.getId()),
                doc.getNombre(),
                doc.getEmail(),
                doc.getTelefono(),
                Guia.EstadoGuia.valueOf(doc.getEstado()),
                doc.getHorarios().stream().map(this::toDisponibilidadDomain).collect(Collectors.toList())
        );

    }
    private DisponibilidadDiaria toDisponibilidadDomain(DisponibilidadDiariaDocument embeddable) {
        return new DisponibilidadDiaria(
                DayOfWeek.valueOf(embeddable.getDia()),
                embeddable.isDisponible(),
                embeddable.getFranjas().stream().map(f -> new FranjaHoraria(f.getHoraInicio(), f.getHoraFin())).collect(Collectors.toList())
                );
    }
}