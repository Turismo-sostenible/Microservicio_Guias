package com.microservicio.guias.application.port.output;

import java.util.Optional;

import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;

/**
 * Puerto de Salida para operaciones de escritura (Comandos) en la base de datos.
 * Define el contrato para persistir cambios en el agregado Guia.
 */
public interface GuiaCommandRepository {
    Guia save(Guia guia);
    Optional<Guia> findById(GuiaId id); // Necesario para actualizar
    void deleteById(GuiaId id);
}