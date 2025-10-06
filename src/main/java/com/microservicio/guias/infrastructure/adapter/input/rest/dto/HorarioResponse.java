package com.microservicio.guias.infrastructure.adapter.input.rest.dto;
import java.time.LocalDateTime;

public record HorarioResponse(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {}