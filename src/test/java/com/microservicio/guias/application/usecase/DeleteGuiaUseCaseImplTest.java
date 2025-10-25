package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import com.microservicio.guias.domain.model.GuiaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteGuiaUseCaseImplTest {

    @Mock
    private GuiaCommandRepository guiaCommandRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private DeleteGuiaUseCaseImpl deleteGuiaUseCase;

    @Test
    @DisplayName("Debería llamar a deleteById y publicar evento")
    void deberiaEliminarYPublicar() {
        GuiaId id = GuiaId.generate();

        doNothing().when(guiaCommandRepository).deleteById(id);
        doNothing().when(eventPublisher).publish(any(GuiaEliminadoEvent.class));

        deleteGuiaUseCase.deleteGuia(id);

        verify(guiaCommandRepository, times(1)).deleteById(id);
        verify(eventPublisher, times(1)).publish(any(GuiaEliminadoEvent.class));
    }
}