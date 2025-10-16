package com.microservicio.guias.infrastructure.adapter.input.rest;
import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.input.DeleteGuiaUseCase;
import com.microservicio.guias.application.port.input.UpdateDisponibilityUseCase;
import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.DisponibilidadDiariaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.UpdateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.mapper.GuiaApiMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin") // Asumiendo que simplificamos la ruta base
public class GuiaAdminCommandController {

    private final CreateGuiaUseCase createGuiaUseCase;
    private final UpdateGuiaUseCase updateGuiaUseCase;
    private final DeleteGuiaUseCase deleteGuiaUseCase;
    private final UpdateDisponibilityUseCase actualizarDisponibilidadUseCase; // NUEVO
    private final GuiaApiMapper mapper;

    // CONSTRUCTOR ACTUALIZADO para recibir el nuevo caso de uso
    public GuiaAdminCommandController(CreateGuiaUseCase c, UpdateGuiaUseCase u, DeleteGuiaUseCase d, UpdateDisponibilityUseCase adu, GuiaApiMapper m) {
        this.createGuiaUseCase = c;
        this.updateGuiaUseCase = u;
        this.deleteGuiaUseCase = d;
        this.actualizarDisponibilidadUseCase = adu;
        this.mapper = m;
    }

    // ENDPOINT EXISTENTE: Crear un guía
    @PostMapping
    public ResponseEntity<Void> createGuia(@RequestBody CreateGuiaRequest request) {
        var command = mapper.toCommand(request);
        var nuevoGuia = createGuiaUseCase.createGuia(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoGuia.getId().value().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // ENDPOINT EXISTENTE: Actualizar datos básicos de un guía
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGuia(@PathVariable String id, @RequestBody UpdateGuiaRequest request) {
        var command = mapper.toCommand(request, id);
        boolean updated = updateGuiaUseCase.updateGuia(command);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ENDPOINT EXISTENTE: Eliminar un guía
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuia(@PathVariable String id) {
        deleteGuiaUseCase.deleteGuia(GuiaId.fromString(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * NUEVO ENDPOINT para actualizar la disponibilidad semanal completa de un guía.
     */
    @PutMapping("/{guiaId}/disponibilidad")
    public ResponseEntity<Void> actualizarDisponibilidad(
        @PathVariable String guiaId,
        @RequestBody List<DisponibilidadDiariaRequest> request
    ) {
        // Convertimos los DTOs de la petición a objetos del Dominio
        List<DisponibilidadDiaria> disponibilidad = request.stream().map(dto ->
            new DisponibilidadDiaria(
                dto.dia(),
                dto.disponible(),
                dto.franjas() != null ? dto.franjas().stream().map(f ->
                    new FranjaHoraria(f.horaInicio(), f.horaFin())
                ).collect(Collectors.toList()) : Collections.emptyList()
            )
        ).collect(Collectors.toList());

        var command = new UpdateDisponibilityUseCase.UpdateDisponibilityCommand(
            GuiaId.fromString(guiaId),
            disponibilidad
        );

        boolean success = actualizarDisponibilidadUseCase.actualizarDisponibilidad(command);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}