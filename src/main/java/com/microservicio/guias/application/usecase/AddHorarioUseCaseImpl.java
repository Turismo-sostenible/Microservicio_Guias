package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.input.AddHorarioUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.Horario;

import java.util.Optional;

public class AddHorarioUseCaseImpl implements AddHorarioUseCase {

    private final GuiaCommandRepository guiaCommandRepository;
    private final EventPublisher eventPublisher;

    public AddHorarioUseCaseImpl(GuiaCommandRepository guiaCommandRepository, EventPublisher eventPublisher) {
        this.guiaCommandRepository = guiaCommandRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean addHorario(AddHorarioCommand command) {
        // 1. Buscamos el guía en la base de datos de escritura
        Optional<Guia> guiaOptional = guiaCommandRepository.findById(command.guiaId());

        if (guiaOptional.isEmpty()) {
            return false; // El guía no existe
        }

        Guia guia = guiaOptional.get();
        Horario nuevoHorario = new Horario(command.fechaHoraInicio(), command.fechaHoraFin());

        // 2. Usamos el método del Dominio para añadir el horario.
        // Este método ya contiene la lógica para evitar solapamientos.
        guia.agregarHorario(nuevoHorario);

        // 3. Guardamos el estado actualizado del guía
        guiaCommandRepository.save(guia);
        
        // 4. Publicamos un evento para notificar que el guía fue actualizado
        eventPublisher.publish(new GuiaActualizadoEvent(guia.getId(), guia.getNombre(), guia.getEmail(), guia.getTelefono(), guia.getEstado().name(), guia.getHorarios()));
        
        return true;
    }
}