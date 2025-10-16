package com.microservicio.guias.infrastructure.adapter.input.rest.dto;
import java.time.DayOfWeek;
import java.util.List;

public record DisponibilidadDiariaResponse(
    DayOfWeek dia,
    boolean disponible,
    List<FranjaHorariaRequest> franjas // Reutilizamos el Request DTO para la respuesta
) {}