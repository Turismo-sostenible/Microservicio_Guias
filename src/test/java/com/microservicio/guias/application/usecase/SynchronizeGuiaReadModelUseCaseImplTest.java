package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizeGuiaReadModelUseCaseImplTest {

    @Mock
    private GuiaCommandRepository commandRepository;

    @Mock
    private GuiaQueryRepository queryRepository;

    @InjectMocks
    private SynchronizeGuiaReadModelUseCaseImpl synchronizeUseCase;

    @Test
    @DisplayName("HandleGuiaCreado debería buscar en CommandRepo y guardar en QueryRepo")
    void handleGuiaCreadoDeberiaSincronizar() {
        Guia guia = Guia.crear("Test", "test@test.com", "123");
        GuiaCreadoEvent.GuiaId eventoId = new GuiaCreadoEvent.GuiaId(guia.getId().value());
        GuiaCreadoEvent event = new GuiaCreadoEvent(eventoId, "Test", "test@test.com", "123", "ACTIVO");

        when(commandRepository.findById(guia.getId())).thenReturn(Optional.of(guia));

        synchronizeUseCase.handleGuiaCreado(event);

        verify(commandRepository).findById(guia.getId());
        verify(queryRepository).save(guia);
    }

    @Test
    @DisplayName("HandleGuiaActualizado debería buscar en CommandRepo y guardar en QueryRepo")
    void handleGuiaActualizadoDeberiaSincronizar() {
        Guia guia = Guia.crear("Test", "test@test.com", "123");
        GuiaActualizadoEvent event = new GuiaActualizadoEvent(guia.getId(), "Test", "test@test.com", "123", "ACTIVO", List.of());

        when(commandRepository.findById(guia.getId())).thenReturn(Optional.of(guia));

        synchronizeUseCase.handleGuiaActualizado(event);

        verify(commandRepository).findById(guia.getId());
        verify(queryRepository).save(guia);
    }
    
    @Test
    @DisplayName("HandleGuiaActualizado no debería hacer nada si el Guía no se encuentra")
    void handleGuiaActualizadoNoDeberiaHacerNadaSiNoSeEncuentra() {
        GuiaId id = GuiaId.generate();
        GuiaActualizadoEvent event = new GuiaActualizadoEvent(id, "Test", "test@test.com", "123", "ACTIVO", List.of());

        when(commandRepository.findById(id)).thenReturn(Optional.empty());

        synchronizeUseCase.handleGuiaActualizado(event);

        verify(commandRepository).findById(id);
        verify(queryRepository, never()).save(any());
    }

    @Test
    @DisplayName("HandleGuiaEliminado debería eliminar del QueryRepo")
    void handleGuiaEliminadoDeberiaSincronizar() {
        GuiaId id = GuiaId.generate();
        GuiaEliminadoEvent event = new GuiaEliminadoEvent(id);

        synchronizeUseCase.handleGuiaEliminado(event);

        verify(queryRepository).deleteById(id);
    }
}