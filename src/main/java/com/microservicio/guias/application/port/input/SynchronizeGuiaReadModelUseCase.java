package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;

public interface SynchronizeGuiaReadModelUseCase {
    void handleGuiaCreado(GuiaCreadoEvent event);
    void handleGuiaActualizado(GuiaActualizadoEvent event);
    void handleGuiaEliminado(GuiaEliminadoEvent event);
}