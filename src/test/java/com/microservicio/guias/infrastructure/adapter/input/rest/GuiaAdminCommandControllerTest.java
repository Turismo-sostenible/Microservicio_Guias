package com.microservicio.guias.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.guias.application.port.input.*;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.DisponibilidadDiariaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.FranjaHorariaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.mapper.GuiaApiMapper;
import com.microservicio.guias.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuiaAdminCommandController.class)
@Import(SecurityConfig.class)
class GuiaAdminCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateGuiaUseCase createGuiaUseCase;
    @MockBean
    private UpdateGuiaUseCase updateGuiaUseCase;
    @MockBean
    private DeleteGuiaUseCase deleteGuiaUseCase;
    @MockBean
    private UpdateDisponibilityUseCase updateDisponibilityUseCase;
    
    @MockBean
    private GuiaApiMapper mapper; 

    @Test
    @DisplayName("Debería crear un guía (POST /admin) y retornar 201 Created")
    void deberiaCrearGuia() throws Exception {
        CreateGuiaRequest request = new CreateGuiaRequest("Nuevo Guía", "guia@test.com", "123456");
        Guia guiaCreado = Guia.crear(request.nombre(), request.email(), request.telefono());

        given(createGuiaUseCase.createGuia(any())).willReturn(guiaCreado);

        mockMvc.perform(post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(header().string("Location", "http://localhost/admin/" + guiaCreado.getId().value().toString()));
    }

    @Test
    @DisplayName("Debería actualizar disponibilidad (PUT /admin/{id}/disponibilidad) y retornar 200 OK")
    void deberiaActualizarDisponibilidad() throws Exception {
        String guiaId = UUID.randomUUID().toString();
        FranjaHorariaRequest franja = new FranjaHorariaRequest(LocalTime.of(9, 0), LocalTime.of(12, 0));
        DisponibilidadDiariaRequest disp = new DisponibilidadDiariaRequest(DayOfWeek.MONDAY, true, List.of(franja));
        List<DisponibilidadDiariaRequest> requestBody = List.of(disp);

        given(updateDisponibilityUseCase.actualizarDisponibilidad(any(UpdateDisponibilityUseCase.UpdateDisponibilityCommand.class)))
            .willReturn(true);

        mockMvc.perform(put("/admin/{guiaId}/disponibilidad", guiaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debería retornar 404 Not Found si se actualiza disponibilidad de un guía que no existe")
    void deberiaRetornarNotFoundAlActualizarDisponibilidad() throws Exception {
        String guiaId = UUID.randomUUID().toString();
        List<DisponibilidadDiariaRequest> requestBody = List.of();

        given(updateDisponibilityUseCase.actualizarDisponibilidad(any(UpdateDisponibilityUseCase.UpdateDisponibilityCommand.class)))
            .willReturn(false);

        mockMvc.perform(put("/admin/{guiaId}/disponibilidad", guiaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Debería eliminar un guía (DELETE /admin/{id}) y retornar 204 No Content")
    void deberiaEliminarGuia() throws Exception {
        String guiaId = UUID.randomUUID().toString();
        
        mockMvc.perform(delete("/admin/{id}", guiaId))
            .andExpect(status().isNoContent());
    }
}