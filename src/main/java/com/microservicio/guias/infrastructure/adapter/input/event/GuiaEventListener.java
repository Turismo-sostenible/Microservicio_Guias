package com.microservicio.guias.infrastructure.adapter.input.event;
import com.microservicio.guias.application.port.input.SynchronizeGuiaReadModelUseCase;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada que escucha eventos de dominio y dispara el caso de uso de sincronización.
 */
@Component
public class GuiaEventListener {

    private final SynchronizeGuiaReadModelUseCase synchronizeGuiaReadModelUseCase;

    public GuiaEventListener(SynchronizeGuiaReadModelUseCase synchronizeGuiaReadModelUseCase) {
        this.synchronizeGuiaReadModelUseCase = synchronizeGuiaReadModelUseCase;
    }

    @EventListener
    public void handle(GuiaCreadoEvent event) {
        synchronizeGuiaReadModelUseCase.handleGuiaCreado(event);
    }

    @EventListener
    public void handle(GuiaActualizadoEvent event) {
        synchronizeGuiaReadModelUseCase.handleGuiaActualizado(event);
    }
    
    @EventListener
    public void handle(GuiaEliminadoEvent event) {
        synchronizeGuiaReadModelUseCase.handleGuiaEliminado(event);
    }
}