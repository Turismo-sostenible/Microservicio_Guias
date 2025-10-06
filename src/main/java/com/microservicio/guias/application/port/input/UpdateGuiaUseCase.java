package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.GuiaId;

public interface UpdateGuiaUseCase {
    boolean updateGuia(UpdateGuiaCommand command);

    record UpdateGuiaCommand(GuiaId id, String nombre, String email, String telefono) {}
}