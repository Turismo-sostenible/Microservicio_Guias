package com.microservicio.guias.infrastructure.adapter.input.rest;

import com.microservicio.guias.application.port.input.FindGuiaQuery;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.GuiaResponse;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuiaQueryController.class)
@Import(SecurityConfig.class)
class GuiaQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindGuiaQuery findGuiaQuery;

    @MockBean
    private GuiaApiMapper mapper;

    @Test
    @DisplayName("Debería obtener todos los guías (GET /) y retornar 200 OK")
    void deberiaObtenerTodosLosGuias() throws Exception {
        Guia guia = Guia.crear("Guia 1", "g1@test.com", "111");
        GuiaResponse response = new GuiaResponse(guia.getId().value(), guia.getNombre(), guia.getEmail(), guia.getTelefono(), guia.getEstado().name(), List.of());

        given(findGuiaQuery.findAll()).willReturn(List.of(guia));
        given(mapper.toResponse(guia)).willReturn(response);

        mockMvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].nombre").value("Guia 1"));
    }

    @Test
    @DisplayName("Debería obtener un guía por ID (GET /{id}) y retornar 200 OK")
    void deberiaObtenerGuiaPorId() throws Exception {
        Guia guia = Guia.crear("Guia 1", "g1@test.com", "111");
        GuiaResponse response = new GuiaResponse(guia.getId().value(), guia.getNombre(), guia.getEmail(), guia.getTelefono(), guia.getEstado().name(), List.of());

        given(findGuiaQuery.findById(any(GuiaId.class))).willReturn(Optional.of(guia));
        given(mapper.toResponse(guia)).willReturn(response);

        mockMvc.perform(get("/{id}", guia.getId().value())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(guia.getId().value().toString()))
            .andExpect(jsonPath("$.nombre").value("Guia 1"));
    }

    @Test
    @DisplayName("Debería retornar 404 Not Found si el guía (GET /{id}) no existe")
    void deberiaRetornarNotFoundSiNoExiste() throws Exception {
        given(findGuiaQuery.findById(any(GuiaId.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}