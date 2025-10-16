package com.microservicio.guias.domain.model;

import java.time.LocalTime;


public record FranjaHoraria(LocalTime horaInicio, LocalTime horaFin) {
    public FranjaHoraria {
        if (horaInicio.isAfter(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la de fin.");
        }
    }
}