package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.model.Guia;

public class CreateGuiaUseCaseImpl implements CreateGuiaUseCase {

    private final GuiaCommandRepository guiaCommandRepository;
    private final EventPublisher eventPublisher;

    public CreateGuiaUseCaseImpl(GuiaCommandRepository guiaCommandRepository, EventPublisher eventPublisher) {
        this.guiaCommandRepository = guiaCommandRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Guia createGuia(CreateGuiaCommand command) {
        // 1. Usar el factory method del dominio para crear el agregado
        Guia nuevoGuia = Guia.crear(command.nombre(), command.email(), command.telefono());

        // 2. Persistir el nuevo agregado usando el repositorio de comandos
        Guia guiaGuardado = guiaCommandRepository.save(nuevoGuia);

        // 3. Publicar un evento de dominio para notificar a otros componentes
        eventPublisher.publish(new GuiaCreadoEvent(guiaGuardado.getId(), guiaGuardado.getNombre()));
        
        return guiaGuardado;
    }
}