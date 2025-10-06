package com.microservicio.guias.domain.model;
import java.io.Serializable;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un Guía.
 * Usamos 'record' para obtener inmutabilidad, equals(), hashCode() y toString() automáticamente.
 */
public record GuiaId(UUID value) implements Serializable {

    /**
     * Factory method para generar un nuevo ID de Guía aleatorio.
     * @return una nueva instancia de GuiaId.
     */
    public static GuiaId generate() {
        return new GuiaId(UUID.randomUUID());
    }

    /**
     * Factory method para crear un GuiaId a partir de un String.
     * @param uuid El String que representa el UUID.
     * @return una instancia de GuiaId.
     * @throws IllegalArgumentException si el String no es un UUID válido.
     */
    public static GuiaId fromString(String uuid) {
        return new GuiaId(UUID.fromString(uuid));
    }
}