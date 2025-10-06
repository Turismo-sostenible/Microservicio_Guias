package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.Guia;

public interface CreateGuiaUseCase {
    Guia createGuia(CreateGuiaCommand command);

    record CreateGuiaCommand(String nombre, String email, String telefono) {}
}