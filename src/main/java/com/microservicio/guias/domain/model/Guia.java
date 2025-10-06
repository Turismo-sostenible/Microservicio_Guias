package com.microservicio.guias.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad principal y Raíz del Agregado 'Guia'.
 * Contiene la lógica de negocio para gestionar la información y horarios de un
 * guía.
 */
public class Guia {

    private final GuiaId id;
    private String nombre;
    private String email;
    private String telefono;
    private EstadoGuia estado;
    private final List<Horario> horarios;
    

    private Guia(GuiaId id, String nombre, String email, String telefono) {
        this.id = Objects.requireNonNull(id, "El ID del guía no puede ser nulo.");
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo.");
        this.telefono = telefono;
        this.estado = EstadoGuia.ACTIVO; // Por defecto, un guía nuevo está activo.
        this.horarios = new ArrayList<>();
    }

    /**
     * Factory method para reconstruir un Guía desde la persistencia.
     * Este método confía en que los datos que vienen de la BD son válidos.
     */
    public static Guia reconstitute(GuiaId id, String nombre, String email, String telefono, EstadoGuia estado,
            List<Horario> horarios) {
        Guia guia = new Guia(id, nombre, email, telefono);
        guia.estado = estado;
        guia.horarios.addAll(horarios);
        return guia;
    }

    /**
     * Factory method para la creación de un nuevo Guía, asegurando un estado
     * inicial válido.
     */
    public static Guia crear(String nombre, String email, String telefono) {
        // Aquí se pueden añadir más validaciones si es necesario (ej. formato de email)
        return new Guia(GuiaId.generate(), nombre, email, telefono);
    }

    // --- Lógica de Negocio ---

    public void agregarHorario(Horario nuevoHorario) {
        if (this.estado == EstadoGuia.INACTIVO) {
            throw new IllegalStateException("No se puede agregar un horario a un guía inactivo.");
        }

        // Regla de negocio: No permitir horarios que se solapen.
        boolean seSolapa = horarios.stream().anyMatch(h -> h.seSolapaCon(nuevoHorario));
        if (seSolapa) {
            throw new IllegalArgumentException("El nuevo horario se solapa con uno existente.");
        }
        this.horarios.add(nuevoHorario);
    }

    public void actualizarDatos(String nuevoNombre, String nuevoEmail, String nuevoTelefono) {
        this.nombre = Objects.requireNonNull(nuevoNombre, "El nombre no puede ser nulo.");
        this.email = Objects.requireNonNull(nuevoEmail, "El email no puede ser nulo.");
        this.telefono = nuevoTelefono;
    }

    public void desactivar() {
        if (this.estado == EstadoGuia.INACTIVO) {
            throw new IllegalStateException("El guía ya se encuentra inactivo.");
        }
        this.estado = EstadoGuia.INACTIVO;
    }

    public void activar() {
        if (this.estado == EstadoGuia.ACTIVO) {
            throw new IllegalStateException("El guía ya se encuentra activo.");
        }
        this.estado = EstadoGuia.ACTIVO;
    }

    // --- Getters ---
    // No hay setters públicos para proteger la consistencia del objeto.
    // Las modificaciones se hacen a través de métodos con lógica de negocio.

    public GuiaId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public EstadoGuia getEstado() {
        return estado;
    }

    /**
     * Devuelve una copia inmutable de la lista de horarios para proteger la
     * encapsulación.
     */
    public List<Horario> getHorarios() {
        return Collections.unmodifiableList(horarios);
    }
}