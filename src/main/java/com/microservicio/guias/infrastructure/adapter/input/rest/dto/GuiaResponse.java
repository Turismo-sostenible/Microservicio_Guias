package com.microservicio.guias.infrastructure.adapter.input.rest.dto;
import java.util.List;
import java.util.UUID;

public record GuiaResponse(UUID id, String nombre, String email, String telefono, String estado, List<DisponibilidadDiariaResponse> horarios) {}