package com.microservicio.guias.domain.model;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Value Object que representa el horario de disponibilidad de un guía.
 * Es inmutable y valida que la fecha de inicio no sea posterior a la de fin.
 */
public record Horario(
    LocalDateTime fechaHoraInicio,
    LocalDateTime fechaHoraFin
) implements Serializable {

    public Horario {
        // Regla de negocio: La fecha de inicio no puede ser posterior a la fecha de fin.
        if (fechaHoraInicio.isAfter(fechaHoraFin)) {
            throw new IllegalArgumentException("La fecha y hora de inicio no puede ser posterior a la de fin.");
        }
    }

    /**
     * Lógica de negocio para verificar si este horario se solapa con otro.
     * @param otroHorario El otro horario a comparar.
     * @return true si hay solapamiento, false en caso contrario.
     */
    public boolean seSolapaCon(Horario otroHorario) {
        // Dos intervalos [A, B] y [C, D] se solapan si A < D y C < B.
        return this.fechaHoraInicio.isBefore(otroHorario.fechaHoraFin) &&
               otroHorario.fechaHoraInicio.isBefore(this.fechaHoraFin);
    }
}