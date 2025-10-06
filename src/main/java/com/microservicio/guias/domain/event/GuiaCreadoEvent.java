package com.microservicio.guias.domain.event;
import com.microservicio.guias.domain.model.GuiaId;

public record GuiaCreadoEvent(GuiaId guiaId, String nombre) {}