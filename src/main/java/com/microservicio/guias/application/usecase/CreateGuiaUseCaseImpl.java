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

        eventPublisher.publish(new GuiaCreadoEvent(
        new GuiaCreadoEvent.GuiaId(guiaGuardado.getId().value()),
        guiaGuardado.getNombre(),
        guiaGuardado.getEmail(),
        guiaGuardado.getTelefono(),
        guiaGuardado.getEstado().name() 
        ));
        return guiaGuardado;
    }
}