package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data // Incluye Getters, Setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
public class HorarioEmbeddable implements Serializable {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
}