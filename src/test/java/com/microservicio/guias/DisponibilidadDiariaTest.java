package com.microservicio.guias;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadDiariaTest {

    @Test
    @DisplayName("Debería crear disponibilidad si está disponible y tiene franjas")
    void deberiaCrearDisponibilidadConFranjas() {
        FranjaHoraria franja = new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(12, 0));
        DisponibilidadDiaria disp = new DisponibilidadDiaria(DayOfWeek.MONDAY, true, List.of(franja));

        assertEquals(DayOfWeek.MONDAY, disp.dia());
        assertTrue(disp.disponible());
        assertEquals(1, disp.franjas().size());
    }

    @Test
    @DisplayName("Debería crear disponibilidad si no está disponible y no tiene franjas")
    void deberiaCrearDisponibilidadNoDisponible() {
        DisponibilidadDiaria disp = new DisponibilidadDiaria(DayOfWeek.TUESDAY, false, List.of());

        assertEquals(DayOfWeek.TUESDAY, disp.dia());
        assertFalse(disp.disponible());
        assertTrue(disp.franjas().isEmpty());
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si no está disponible pero tiene franjas")
    void deberiaLanzarExcepcionSiNoDisponibleYConFranjas() {
        FranjaHoraria franja = new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(12, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            new DisponibilidadDiaria(DayOfWeek.WEDNESDAY, false, List.of(franja));
        });
    }
}
