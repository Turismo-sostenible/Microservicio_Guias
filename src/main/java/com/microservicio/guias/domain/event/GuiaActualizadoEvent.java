package com.microservicio.guias.domain.event;

import com.microservicio.guias.domain.model.GuiaId;

public record GuiaActualizadoEvent(GuiaId guiaId) {}