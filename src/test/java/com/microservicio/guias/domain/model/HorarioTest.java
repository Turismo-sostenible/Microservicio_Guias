package com.microservicio.guias.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HorarioTest {

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si fecha de inicio es posterior a fecha de fin")
    void deberiaLanzarExcepcionSiInicioEsDespuesDeFin() {
        LocalDateTime fin = LocalDateTime.of(2025, 10, 20, 10, 0);
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 20, 11, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            new Horario(inicio, fin);
        });
    }

    @Test
    @DisplayName("Debería detectar solapamiento (A < D y C < B)")
    void deberiaDetectarSolapamiento() {
        Horario h1 = new Horario(
            LocalDateTime.of(2025, 1, 1, 10, 0),
            LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        Horario h2_solapaFin = new Horario(
            LocalDateTime.of(2025, 1, 1, 11, 0),
            LocalDateTime.of(2025, 1, 1, 13, 0)
        );

        Horario h3_solapaInicio = new Horario(
            LocalDateTime.of(2025, 1, 1, 9, 0),
            LocalDateTime.of(2025, 1, 1, 11, 0)
        );

        Horario h4_dentro = new Horario(
            LocalDateTime.of(2025, 1, 1, 10, 30),
            LocalDateTime.of(2025, 1, 1, 11, 30)
        );

        Horario h5_contiene = new Horario(
            LocalDateTime.of(2025, 1, 1, 9, 0),
            LocalDateTime.of(2025, 1, 1, 13, 0)
        );
        
        Horario h6_tocaFin = new Horario(
            LocalDateTime.of(2025, 1, 1, 12, 0),
            LocalDateTime.of(2025, 1, 1, 13, 0)
        );

        assertTrue(h1.seSolapaCon(h2_solapaFin));
        assertTrue(h2_solapaFin.seSolapaCon(h1));
        assertTrue(h1.seSolapaCon(h3_solapaInicio));
        assertTrue(h3_solapaInicio.seSolapaCon(h1));
        assertTrue(h1.seSolapaCon(h4_dentro));
        assertTrue(h4_dentro.seSolapaCon(h1));
        assertTrue(h1.seSolapaCon(h5_contiene));
        assertTrue(h5_contiene.seSolapaCon(h1));
        
        assertFalse(h1.seSolapaCon(h6_tocaFin), "Tocar el borde no es solapamiento");
        assertFalse(h6_tocaFin.seSolapaCon(h1), "Tocar el borde no es solapamiento");
    }

    @Test
    @DisplayName("No debería detectar solapamiento si no existe")
    void noDeberiaDetectarSolapamiento() {
        Horario h1 = new Horario(
            LocalDateTime.of(2025, 1, 1, 10, 0),
            LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        Horario h_antes = new Horario(
            LocalDateTime.of(2025, 1, 1, 8, 0),
            LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        
        Horario h_despues = new Horario(
            LocalDateTime.of(2025, 1, 1, 12, 0),
            LocalDateTime.of(2025, 1, 1, 14, 0)
        );
        
        assertFalse(h1.seSolapaCon(h_antes));
        assertFalse(h_antes.seSolapaCon(h1));
        assertFalse(h1.seSolapaCon(h_despues));
        assertFalse(h_despues.seSolapaCon(h1));
    }
}