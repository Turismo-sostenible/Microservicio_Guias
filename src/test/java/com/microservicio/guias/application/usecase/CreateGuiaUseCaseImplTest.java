package com.microservicio.guias.application.usecase;

import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.domain.model.Guia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateGuiaUseCaseImplTest {

    @Mock
    private GuiaCommandRepository guiaCommandRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateGuiaUseCaseImpl createGuiaUseCase;

    @Test
    @DisplayName("Debería crear Guía, guardarlo y publicar evento")
    void deberiaCrearGuardarYPublicar() {
        CreateGuiaUseCase.CreateGuiaCommand command = new CreateGuiaUseCase.CreateGuiaCommand(
            "Nuevo Guia", "nuevo@guia.com", "111222333"
        );
        
        Guia guiaCreado = Guia.crear(command.nombre(), command.email(), command.telefono());
        
        when(guiaCommandRepository.save(any(Guia.class))).thenReturn(guiaCreado);

        Guia resultado = createGuiaUseCase.createGuia(command);

        assertNotNull(resultado);
        assertEquals("Nuevo Guia", resultado.getNombre());
        
        verify(guiaCommandRepository, times(1)).save(any(Guia.class));
        verify(eventPublisher, times(1)).publish(any(GuiaCreadoEvent.class));
    }
}