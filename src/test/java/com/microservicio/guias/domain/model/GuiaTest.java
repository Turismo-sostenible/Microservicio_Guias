package com.microservicio.guias.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GuiaTest {

    private String nombre;
    private String email;
    private String telefono;

    @BeforeEach
    void setUp() {
        nombre = "Carlos Test";
        email = "carlos.test@example.com";
        telefono = "123456789";
    }

    @Test
    @DisplayName("Debería crear un Guía con estado ACTIVO por defecto")
    void deberiaCrearUnGuiaConEstadoActivoPorDefecto() {
        Guia nuevoGuia = Guia.crear(nombre, email, telefono);

        assertNotNull(nuevoGuia.getId(), "El ID no debería ser nulo");
        assertEquals(nombre, nuevoGuia.getNombre());
        assertEquals(email, nuevoGuia.getEmail());
        assertEquals(telefono, nuevoGuia.getTelefono());
        assertEquals(Guia.EstadoGuia.ACTIVO, nuevoGuia.getEstado());
        assertTrue(nuevoGuia.getDisponibilidadSemanal().isEmpty(), "La disponibilidad semanal debería estar vacía");
    }

    @Test
    @DisplayName("Debería actualizar los datos básicos del Guía")
    void deberiaActualizarDatos() {
        Guia guia = Guia.crear(nombre, email, telefono);
        String nuevoNombre = "Carlos Actualizado";
        String nuevoEmail = "nuevo.email@example.com";
        String nuevoTelefono = "987654321";

        guia.actualizarDatos(nuevoNombre, nuevoEmail, nuevoTelefono);

        assertEquals(nuevoNombre, guia.getNombre());
        assertEquals(nuevoEmail, guia.getEmail());
        assertEquals(nuevoTelefono, guia.getTelefono());
    }

    @Test
    @DisplayName("Debería desactivar un Guía que está ACTIVO")
    void deberiaDesactivarGuiaActivo() {
        Guia guia = Guia.crear(nombre, email, telefono);
        assertEquals(Guia.EstadoGuia.ACTIVO, guia.getEstado());

        guia.desactivar();

        assertEquals(Guia.EstadoGuia.INACTIVO, guia.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar IllegalStateException al intentar desactivar un Guía ya INACTIVO")
    void deberiaLanzarExcepcionAlDesactivarGuiaInactivo() {
        Guia guia = Guia.crear(nombre, email, telefono);
        guia.desactivar();

        assertThrows(IllegalStateException.class, guia::desactivar);
    }

    @Test
    @DisplayName("Debería activar un Guía que está INACTIVO")
    void deberiaActivarGuiaInactivo() {
        Guia guia = Guia.crear(nombre, email, telefono);
        guia.desactivar();
        assertEquals(Guia.EstadoGuia.INACTIVO, guia.getEstado());

        guia.activar();

        assertEquals(Guia.EstadoGuia.ACTIVO, guia.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar IllegalStateException al intentar activar un Guía ya ACTIVO")
    void deberiaLanzarExcepcionAlActivarGuiaActivo() {
        Guia guia = Guia.crear(nombre, email, telefono);

        assertThrows(IllegalStateException.class, guia::activar);
    }

    @Test
    @DisplayName("Debería actualizar la lista de disponibilidad semanal")
    void deberiaActualizarDisponibilidad() {
        Guia guia = Guia.crear(nombre, email, telefono);
        DisponibilidadDiaria lunes = new DisponibilidadDiaria(DayOfWeek.MONDAY, false, List.of());
        List<DisponibilidadDiaria> nuevaDisponibilidad = List.of(lunes);

        guia.actualizarDisponibilidad(nuevaDisponibilidad);

        assertEquals(1, guia.getDisponibilidadSemanal().size());
        assertEquals(DayOfWeek.MONDAY, guia.getDisponibilidadSemanal().get(0).dia());
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException si los datos al crear son nulos")
    void deberiaLanzarNullPointerExceptionAlCrear() {
        assertThrows(NullPointerException.class, () -> Guia.crear(null, email, telefono));
        assertThrows(NullPointerException.class, () -> Guia.crear(nombre, null, telefono));
    }
}