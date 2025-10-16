package com.microservicio.guias.infrastructure.adapter.input.rest.dto;

import java.time.LocalTime;

public record FranjaHorariaRequest(LocalTime horaInicio, LocalTime horaFin) {}