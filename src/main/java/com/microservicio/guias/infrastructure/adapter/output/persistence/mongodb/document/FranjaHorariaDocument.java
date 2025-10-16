package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranjaHorariaDocument {
    private LocalTime horaInicio;
    private LocalTime horaFin;
} 