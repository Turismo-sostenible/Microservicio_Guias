package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateGuiaUseCaseImplTest {

    @Mock
    private GuiaCommandRepository guiaCommandRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UpdateGuiaUseCaseImpl updateGuiaUseCase;

    @Spy
    private Guia guiaExistente = Guia.crear("Guia Viejo", "viejo@guia.com", "123");

    @Test
    @DisplayName("Debería actualizar Guía y publicar evento si Guía existe")
    void deberiaActualizarYPublicarSiExiste() {
        GuiaId id = guiaExistente.getId();
        UpdateGuiaUseCase.UpdateGuiaCommand command = new UpdateGuiaUseCase.UpdateGuiaCommand(
            id, "Guia Nuevo", "nuevo@guia.com", "456"
        );

        when(guiaCommandRepository.findById(id)).thenReturn(Optional.of(guiaExistente));

        boolean resultado = updateGuiaUseCase.updateGuia(command);

        assertTrue(resultado);
        verify(guiaExistente).actualizarDatos("Guia Nuevo", "nuevo@guia.com", "456");
        verify(guiaCommandRepository, times(1)).save(guiaExistente);
        verify(eventPublisher, times(1)).publish(any(GuiaActualizadoEvent.class));
    }

    @Test
    @DisplayName("Debería retornar false si Guía no existe")
    void deberiaRetornarFalseSiNoExiste() {
        GuiaId id = GuiaId.generate();
        UpdateGuiaUseCase.UpdateGuiaCommand command = new UpdateGuiaUseCase.UpdateGuiaCommand(
            id, "Guia Nuevo", "nuevo@guia.com", "456"
        );

        when(guiaCommandRepository.findById(id)).thenReturn(Optional.empty());

        boolean resultado = updateGuiaUseCase.updateGuia(command);

        assertFalse(resultado);
        verify(guiaCommandRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}