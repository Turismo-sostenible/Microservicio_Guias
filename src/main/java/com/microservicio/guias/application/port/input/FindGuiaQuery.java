package com.microservicio.guias.application.port.input;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;

import java.util.List;
import java.util.Optional;

public interface FindGuiaQuery {
    Optional<Guia> findById(GuiaId id);
    List<Guia> findAll();
}