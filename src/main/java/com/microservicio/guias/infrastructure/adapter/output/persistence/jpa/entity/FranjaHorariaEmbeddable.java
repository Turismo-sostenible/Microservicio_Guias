package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranjaHorariaEmbeddable {
    private LocalTime horaInicio;
    private LocalTime horaFin;
}