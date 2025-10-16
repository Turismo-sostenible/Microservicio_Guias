package com.microservicio.guias.application.port.input;

import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.GuiaId;
import java.util.List;

public interface UpdateDisponibilityUseCase {
    
    boolean actualizarDisponibilidad(UpdateDisponibilityCommand command);

    record UpdateDisponibilityCommand(
        GuiaId guiaId,
        List<DisponibilidadDiaria> nuevaDisponibilidad
    ) {}
}