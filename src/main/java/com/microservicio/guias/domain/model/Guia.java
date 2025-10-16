package com.microservicio.guias.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad principal y Raíz del Agregado 'Guia'.
 */
public class Guia {

    private final GuiaId id;
    private String nombre;
    private String email;
    private String telefono;
    private EstadoGuia estado;
    
    private List<DisponibilidadDiaria> disponibilidadSemanal;

    private Guia(GuiaId id, String nombre, String email, String telefono) {
        this.id = Objects.requireNonNull(id, "El ID del guía no puede ser nulo.");
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo.");
        this.telefono = telefono;
        this.estado = EstadoGuia.ACTIVO;
        this.disponibilidadSemanal = new ArrayList<>(); // Se inicializa la nueva lista
    }

    public static Guia crear(String nombre, String email, String telefono) {
        return new Guia(GuiaId.generate(), nombre, email, telefono);
    }

    // --- Lógica de Negocio ---

    
    public void actualizarDisponibilidad(List<DisponibilidadDiaria> nuevaDisponibilidad) {
        Objects.requireNonNull(nuevaDisponibilidad, "La lista de disponibilidad no puede ser nula.");
        this.disponibilidadSemanal = new ArrayList<>(nuevaDisponibilidad);
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
    public GuiaId getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public EstadoGuia getEstado() { return estado; }

    public List<DisponibilidadDiaria> getDisponibilidadSemanal() {
        return Collections.unmodifiableList(disponibilidadSemanal);
    }
    
    public enum EstadoGuia { ACTIVO, INACTIVO }

    public static Guia reconstitute(GuiaId id, String nombre, String email, String telefono, EstadoGuia estado, List<DisponibilidadDiaria> disponibilidad) {
        Guia guia = new Guia(id, nombre, email, telefono);
        guia.estado = estado;
        if (disponibilidad != null) {
            guia.disponibilidadSemanal.addAll(disponibilidad);
        }
        return guia;
    }
}