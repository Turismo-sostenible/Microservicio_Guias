package com.microservicio.guias.infrastructure.adapter.input.rest;
import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.input.DeleteGuiaUseCase;
import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.UpdateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.mapper.GuiaApiMapper;

import com.microservicio.guias.domain.model.GuiaId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/admin")
public class GuiaAdminCommandController {

    private final CreateGuiaUseCase createGuiaUseCase;
    private final UpdateGuiaUseCase updateGuiaUseCase;
    private final DeleteGuiaUseCase deleteGuiaUseCase;
    private final GuiaApiMapper mapper;

    public GuiaAdminCommandController(CreateGuiaUseCase c, UpdateGuiaUseCase u, DeleteGuiaUseCase d, GuiaApiMapper m) {
        this.createGuiaUseCase = c;
        this.updateGuiaUseCase = u;
        this.deleteGuiaUseCase = d;
        this.mapper = m;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGuia(@PathVariable String id, @RequestBody UpdateGuiaRequest request) {
        var command = mapper.toCommand(request, id);
        boolean updated = updateGuiaUseCase.updateGuia(command);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuia(@PathVariable String id) {
        deleteGuiaUseCase.deleteGuia(GuiaId.fromString(id));
        return ResponseEntity.noContent().build();
    }
}