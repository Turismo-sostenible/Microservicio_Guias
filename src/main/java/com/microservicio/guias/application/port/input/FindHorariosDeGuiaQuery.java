package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.domain.model.Horario;

import java.util.List;

public interface FindHorariosDeGuiaQuery {
    List<Horario> findHorariosByGuiaId(GuiaId id);
}