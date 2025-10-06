package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.DeleteGuiaUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import com.microservicio.guias.domain.model.GuiaId;

public class DeleteGuiaUseCaseImpl implements DeleteGuiaUseCase {

    private final GuiaCommandRepository guiaCommandRepository;
    private final EventPublisher eventPublisher;

    public DeleteGuiaUseCaseImpl(GuiaCommandRepository guiaCommandRepository, EventPublisher eventPublisher) {
        this.guiaCommandRepository = guiaCommandRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void deleteGuia(GuiaId id) {
        guiaCommandRepository.deleteById(id);
        eventPublisher.publish(new GuiaEliminadoEvent(id));
    }
}