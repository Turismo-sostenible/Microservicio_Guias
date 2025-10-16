package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.model.Guia;

import java.util.Optional;

public class UpdateGuiaUseCaseImpl implements UpdateGuiaUseCase {

    private final GuiaCommandRepository guiaCommandRepository;
    private final EventPublisher eventPublisher;

    public UpdateGuiaUseCaseImpl(GuiaCommandRepository guiaCommandRepository, EventPublisher eventPublisher) {
        this.guiaCommandRepository = guiaCommandRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean updateGuia(UpdateGuiaCommand command) {
        Optional<Guia> guiaOptional = guiaCommandRepository.findById(command.id());
        if (guiaOptional.isEmpty()) {
            return false;
        }

        Guia guia = guiaOptional.get();
        // 1. Usar métodos del dominio para modificar el estado
        guia.actualizarDatos(command.nombre(), command.email(), command.telefono());

        // 2. Persistir los cambios
        guiaCommandRepository.save(guia);

        // 3. Publicar el evento
        eventPublisher.publish(new GuiaActualizadoEvent(guia.getId(), guia.getNombre(), guia.getEmail(), guia.getTelefono(), guia.getEstado().name()));
        
        return true;
    }
}