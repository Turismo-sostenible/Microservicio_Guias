package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.SynchronizeGuiaReadModelUseCase;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;

public class SynchronizeGuiaReadModelUseCaseImpl implements SynchronizeGuiaReadModelUseCase {
    
    private final GuiaCommandRepository commandRepository;
    private final GuiaQueryRepository queryRepository;

    public SynchronizeGuiaReadModelUseCaseImpl(GuiaCommandRepository commandRepository, GuiaQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public void handleGuiaCreado(GuiaCreadoEvent event) {
        // Busca la fuente de la verdad (BD de comandos) para obtener el estado completo
        commandRepository.findById(event.guiaId()).ifPresent(queryRepository::save);
    }

    @Override
    public void handleGuiaActualizado(GuiaActualizadoEvent event) {
        // Busca la fuente de la verdad para obtener los datos más recientes
        commandRepository.findById(event.guiaId()).ifPresent(queryRepository::save);
    }

    @Override
    public void handleGuiaEliminado(GuiaEliminadoEvent event) {
        queryRepository.deleteById(event.guiaId());
    }
}