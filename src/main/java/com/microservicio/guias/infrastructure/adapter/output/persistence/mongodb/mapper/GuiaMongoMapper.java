package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.DisponibilidadDiariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.FranjaHorariaDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
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

        doc.setHorarios(guia.getDisponibilidadSemanal().stream()
                .map(this::toDisponibilidadDocument)
                .collect(Collectors.toList()));
        
        return doc;
    }

    private DisponibilidadDiariaDocument toDisponibilidadDocument(DisponibilidadDiaria disponibilidad) {
        var doc = new DisponibilidadDiariaDocument();
        doc.setDia(disponibilidad.dia().name());
        doc.setDisponible(disponibilidad.disponible());
        List<FranjaHorariaDocument> franjas = disponibilidad.disponible()
                ? disponibilidad.franjas().stream().map(f -> {
                    var franjaDoc = new FranjaHorariaDocument();
                    franjaDoc.setHoraInicio(f.horaInicio());
                    franjaDoc.setHoraFin(f.horaFin());
                    return franjaDoc;
                }).collect(Collectors.toList())
                : Collections.emptyList();
        
        doc.setFranjas(franjas);
        return doc;
    }

    // --- De Documento MongoDB a Dominio ---
    public Guia toDomainEntity(GuiaMongoDocument doc) {
        return Guia.reconstitute(
                GuiaId.fromString(doc.getId()),
                doc.getNombre(),
                doc.getEmail(),
                doc.getTelefono(),
                Guia.EstadoGuia.valueOf(doc.getEstado()),
                doc.getHorarios() != null ? doc.getHorarios().stream()
                        .map(this::toDisponibilidadDomain)
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    private DisponibilidadDiaria toDisponibilidadDomain(DisponibilidadDiariaDocument doc) {
        return new DisponibilidadDiaria(
                DayOfWeek.valueOf(doc.getDia()),
                doc.isDisponible(),
                doc.getFranjas() != null ? doc.getFranjas().stream()
                        .map(f -> new FranjaHoraria(f.getHoraInicio(), f.getHoraFin()))
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}