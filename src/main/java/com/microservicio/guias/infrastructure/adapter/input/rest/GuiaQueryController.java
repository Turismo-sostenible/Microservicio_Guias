package com.microservicio.guias.infrastructure.adapter.input.rest;
import com.microservicio.guias.application.port.input.FindGuiaQuery;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.GuiaResponse;
import com.microservicio.guias.infrastructure.adapter.input.rest.mapper.GuiaApiMapper;
import com.microservicio.guias.domain.model.GuiaId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/guias")
public class GuiaQueryController {
    
    private final FindGuiaQuery findGuiaQuery;
    private final GuiaApiMapper mapper;

    public GuiaQueryController(FindGuiaQuery findGuiaQuery, GuiaApiMapper mapper) {
        this.findGuiaQuery = findGuiaQuery;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<GuiaResponse>> getAllGuias() {
        List<GuiaResponse> response = findGuiaQuery.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GuiaResponse> getGuiaById(@PathVariable String id) {
        return findGuiaQuery.findById(GuiaId.fromString(id))
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}