package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.GuiaId;

import java.util.List;

public interface FindHorariosDeGuiaQuery {
    List<DisponibilidadDiaria> findHorariosByGuiaId(GuiaId id);
}