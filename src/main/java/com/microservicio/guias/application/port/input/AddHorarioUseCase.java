package com.microservicio.guias.application.port.input;

import com.microservicio.guias.domain.model.GuiaId;
import java.time.LocalDateTime;

public interface AddHorarioUseCase {
    
    // El método que el controlador llamará
    boolean addHorario(AddHorarioCommand command);

    // El objeto de comando que contiene los datos necesarios
    record AddHorarioCommand(
        GuiaId guiaId,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin
    ) {}
}