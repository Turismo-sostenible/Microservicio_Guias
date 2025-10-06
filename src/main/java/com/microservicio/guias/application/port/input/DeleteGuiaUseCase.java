package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.GuiaId;

public interface DeleteGuiaUseCase {
    void deleteGuia(GuiaId id);
}