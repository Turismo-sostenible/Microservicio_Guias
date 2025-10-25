package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.input.UpdateDisponibilityUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDisponibilityUseCaseImplTest {

    @Mock
    private GuiaCommandRepository guiaCommandRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UpdateDisponibilityUseCaseImpl updateDisponibilityUseCase;

    @Spy
    private Guia guiaExistente = Guia.crear("Guia", "guia@guia.com", "123");

    @Test
    @DisplayName("Debería actualizar disponibilidad y publicar evento si Guía existe")
    void deberiaActualizarDisponibilidadYPublicarSiExiste() {
        GuiaId id = guiaExistente.getId();
        List<DisponibilidadDiaria> nuevaDisp = List.of(
            new DisponibilidadDiaria(DayOfWeek.MONDAY, true, List.of())
        );
        UpdateDisponibilityUseCase.UpdateDisponibilityCommand command =
            new UpdateDisponibilityUseCase.UpdateDisponibilityCommand(id, nuevaDisp);

        when(guiaCommandRepository.findById(id)).thenReturn(Optional.of(guiaExistente));

        boolean resultado = updateDisponibilityUseCase.actualizarDisponibilidad(command);

        assertTrue(resultado);
        verify(guiaExistente).actualizarDisponibilidad(nuevaDisp);
        verify(guiaCommandRepository).save(guiaExistente);
        verify(eventPublisher).publish(any(GuiaActualizadoEvent.class));
    }

    @Test
    @DisplayName("Debería retornar false si Guía no existe")
    void deberiaRetornarFalseSiNoExiste() {
        GuiaId id = GuiaId.generate();
        UpdateDisponibilityUseCase.UpdateDisponibilityCommand command =
            new UpdateDisponibilityUseCase.UpdateDisponibilityCommand(id, List.of());

        when(guiaCommandRepository.findById(id)).thenReturn(Optional.empty());

        boolean resultado = updateDisponibilityUseCase.actualizarDisponibilidad(command);

        assertFalse(resultado);
        verify(guiaCommandRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}