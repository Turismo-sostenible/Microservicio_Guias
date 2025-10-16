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
        assertTrue(nuevoGuia.getDisponibilidadSemanal().isEmpty(), "La lista de disponibilidad semanal debería estar vacía inicialmente");
    }
}