package com.microservicio.guias.application.port.output;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.domain.model.Horario;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida para operaciones de lectura (Consultas).
 * Define el contrato para obtener información de los guías.
 */
public interface GuiaQueryRepository {
    // Para la sincronización del modelo de lectura
    Guia save(Guia guia);
    void deleteById(GuiaId id);

    // Para las consultas de la API
    Optional<Guia> findById(GuiaId id);
    List<Guia> findAll();
    List<Horario> findHorariosByGuiaId(GuiaId id);
}