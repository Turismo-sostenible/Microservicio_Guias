package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.util.List;

@Embeddable
@Data
public class DisponibilidadDiariaEmbeddable {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dia;
    private boolean disponible;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "guia_franjas_horarias", joinColumns = @JoinColumn(name = "disponibilidad_id"))
    private List<FranjaHorariaEmbeddable> franjas;
}