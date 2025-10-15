package com.microservicio.guias.application.usecase;


import com.microservicio.guias.application.port.input.SynchronizeGuiaReadModelUseCase;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import com.microservicio.guias.domain.model.GuiaId;

public class SynchronizeGuiaReadModelUseCaseImpl implements SynchronizeGuiaReadModelUseCase {
    
    private final GuiaCommandRepository commandRepository;
    private final GuiaQueryRepository queryRepository;

    public SynchronizeGuiaReadModelUseCaseImpl(GuiaCommandRepository commandRepository, GuiaQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public void handleGuiaCreado(GuiaCreadoEvent event) {
        GuiaId idOficial = new GuiaId(event.guiaId().value());
        commandRepository.findById(idOficial).ifPresent(queryRepository::save);
    }

    @Override
    public void handleGuiaActualizado(GuiaActualizadoEvent event) {
        GuiaId idOficial = new GuiaId(event.guiaId().value());
        commandRepository.findById(idOficial).ifPresent(queryRepository::save);
    }

    @Override
    public void handleGuiaEliminado(GuiaEliminadoEvent event) {
        GuiaId idOficial = new GuiaId(event.guiaId().value());
        queryRepository.deleteById(idOficial);
    }
}