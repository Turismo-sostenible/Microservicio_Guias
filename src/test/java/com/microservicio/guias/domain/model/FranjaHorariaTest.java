package com.microservicio.guias.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class FranjaHorariaTest {

    @Test
    @DisplayName("Debería crear una Franja Horaria válida")
    void deberiaCrearFranjaValida() {
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(10, 0);
        FranjaHoraria franja = new FranjaHoraria(inicio, fin);

        assertEquals(inicio, franja.horaInicio());
        assertEquals(fin, franja.horaFin());
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si hora de inicio es posterior a hora de fin")
    void deberiaLanzarExcepcionSiInicioEsDespuesDeFin() {
        LocalTime inicio = LocalTime.of(11, 0);
        LocalTime fin = LocalTime.of(10, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            new FranjaHoraria(inicio, fin);
        });
    }

    @Test
    @DisplayName("Debería permitir crear una franja donde inicio y fin son iguales")
    void deberiaPermitirHorasIguales() {
        LocalTime tiempo = LocalTime.of(10, 0);
        assertDoesNotThrow(() -> {
            new FranjaHoraria(tiempo, tiempo);
        });
    }
}