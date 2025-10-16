package com.microservicio.guias.infrastructure.adapter.input.rest.dto;

import java.time.DayOfWeek;
import java.util.List;

public record DisponibilidadDiariaRequest(
    DayOfWeek dia,
    boolean disponible,
    List<FranjaHorariaRequest> franjas
) {}