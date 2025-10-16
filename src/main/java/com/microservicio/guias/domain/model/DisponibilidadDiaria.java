package com.microservicio.guias.domain.model;

import java.time.DayOfWeek;
import java.util.List;

// Representa la disponibilidad para un día específico de la semana.
public record DisponibilidadDiaria(
    DayOfWeek dia, 
    boolean disponible, 
    List<FranjaHoraria> franjas
) {
    public DisponibilidadDiaria {
        // Regla de negocio: si no está disponible, no puede tener franjas horarias.
        if (!disponible && franjas != null && !franjas.isEmpty()) {
            throw new IllegalArgumentException("Un día no disponible no puede tener franjas horarias.");
        }
    }

    
}