package com.microservicio.guias.infrastructure.adapter.input.mapper;
import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.GuiaResponse;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.UpdateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.mapper.GuiaApiMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GuiaApiMapperTest {

    private GuiaApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GuiaApiMapper();
    }

    @Test
    @DisplayName("Debería mapear CreateGuiaRequest a CreateGuiaCommand")
    void deberiaMapearCreateRequestACommand() {
        CreateGuiaRequest request = new CreateGuiaRequest("Ana", "ana@test.com", "123");

        CreateGuiaUseCase.CreateGuiaCommand command = mapper.toCommand(request);

        assertEquals("Ana", command.nombre());
        assertEquals("ana@test.com", command.email());
        assertEquals("123", command.telefono());
    }

    @Test
    @DisplayName("Debería mapear UpdateGuiaRequest a UpdateGuiaCommand")
    void deberiaMapearUpdateRequestACommand() {
        UpdateGuiaRequest request = new UpdateGuiaRequest("Ana G.", "ana.g@test.com", "456");
        String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

        UpdateGuiaUseCase.UpdateGuiaCommand command = mapper.toCommand(request, id);

        assertEquals(id, command.id().value().toString());
        assertEquals("Ana G.", command.nombre());
        assertEquals("ana.g@test.com", command.email());
        assertEquals("456", command.telefono());
    }

    @Test
    @DisplayName("Debería mapear Guia (Dominio) a GuiaResponse")
    void deberiaMapearDominioAResponse() {
        Guia guia = Guia.crear("Juan", "juan@test.com", "789");
        List<DisponibilidadDiaria> disponibilidad = List.of(
            new DisponibilidadDiaria(
                DayOfWeek.MONDAY,
                true,
                List.of(new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(12, 0)))
            )
        );
        guia.actualizarDisponibilidad(disponibilidad);

        GuiaResponse response = mapper.toResponse(guia);

        assertEquals(guia.getId().value(), response.id());
        assertEquals("Juan", response.nombre());
        assertEquals("juan@test.com", response.email());
        assertEquals("ACTIVO", response.estado());
        assertEquals(1, response.horarios().size());
        assertEquals(DayOfWeek.MONDAY, response.horarios().get(0).dia());
        assertEquals(1, response.horarios().get(0).franjas().size());
        assertEquals(LocalTime.of(9, 0), response.horarios().get(0).franjas().get(0).horaInicio());
    }
}