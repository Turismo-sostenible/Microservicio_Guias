package com.microservicio.guias.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuiaTest {

    @Test
    void deberiaCrearUnGuiaConEstadoActivoPorDefecto() {
        // Arrange (Preparación)
        String nombre = "Carlos Test";
        String email = "carlos.test@example.com";
        String telefono = "123456789";

        // Act (Actuación)
        Guia nuevoGuia = Guia.crear(nombre, email, telefono);

        // Assert (Verificación)
        assertNotNull(nuevoGuia.getId(), "El ID no debería ser nulo");
        assertEquals(nombre, nuevoGuia.getNombre(), "El nombre no coincide");
        assertEquals(email, nuevoGuia.getEmail(), "El email no coincide");
        assertEquals(EstadoGuia.ACTIVO, nuevoGuia.getEstado(), "El estado inicial debería ser ACTIVO");
        assertTrue(nuevoGuia.getHorarios().isEmpty(), "La lista de horarios debería estar vacía inicialmente");
    }

    @Test
    void noDeberiaPermitirAgregarHorariosSolapados() {
        // Arrange
        Guia guia = Guia.crear("Ana Prueba", "ana.prueba@example.com", "987654321");
        Horario horarioExistente = new Horario(
            java.time.LocalDateTime.of(2025, 10, 12, 9, 0),
            java.time.LocalDateTime.of(2025, 10, 12, 11, 0)
        );
        guia.agregarHorario(horarioExistente);

        Horario horarioSolapado = new Horario(
            java.time.LocalDateTime.of(2025, 10, 12, 10, 0), // Empieza antes de que el otro termine
            java.time.LocalDateTime.of(2025, 10, 12, 12, 0)
        );

        // Act & Assert
        // Verificamos que al intentar agregar el horario solapado, se lance la excepción esperada.
        assertThrows(IllegalArgumentException.class, () -> {
            guia.agregarHorario(horarioSolapado);
        }, "Debería lanzar una excepción por horarios solapados");
    }
}