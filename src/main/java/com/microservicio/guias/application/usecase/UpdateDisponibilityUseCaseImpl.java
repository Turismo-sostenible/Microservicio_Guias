package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.UpdateDisponibilityUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.model.Guia;
import java.util.Optional;

public class UpdateDisponibilityUseCaseImpl implements UpdateDisponibilityUseCase {

    private final GuiaCommandRepository guiaCommandRepository;
    private final EventPublisher eventPublisher;

    public UpdateDisponibilityUseCaseImpl(GuiaCommandRepository repo, EventPublisher publisher) {
        this.guiaCommandRepository = repo;
        this.eventPublisher = publisher;
    }

    @Override
    public boolean actualizarDisponibilidad(UpdateDisponibilityCommand command) {
        Optional<Guia> guiaOptional = guiaCommandRepository.findById(command.guiaId());

        if (guiaOptional.isEmpty()) {
            return false;
        }

        Guia guia = guiaOptional.get();
        guia.actualizarDisponibilidad(command.nuevaDisponibilidad()); // Usamos el nuevo método del dominio
        guiaCommandRepository.save(guia);
        eventPublisher.publish(new GuiaActualizadoEvent(guia.getId(), guia.getNombre(), guia.getEmail(), guia.getTelefono(), guia.getEstado().name(), guia.getDisponibilidadSemanal()));
        
        return true;
    }
}