package com.microservicio.guias.domain.event;

import java.util.List;

import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.domain.model.Horario;

public record GuiaActualizadoEvent(GuiaId guiaId,
                                     String nombre,
                                     String email,
                                     String telefono,
                                     String estado,
                                     List<Horario> horarios) {}