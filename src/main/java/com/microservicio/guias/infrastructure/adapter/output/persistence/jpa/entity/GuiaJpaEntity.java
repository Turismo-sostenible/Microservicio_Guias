package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "guias")
@Data
public class GuiaJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoGuiaJpa estado;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DisponibilidadDiariaEntity> disponibilidadSemanal;

    public enum EstadoGuiaJpa {
        ACTIVO,
        INACTIVO
    }
}