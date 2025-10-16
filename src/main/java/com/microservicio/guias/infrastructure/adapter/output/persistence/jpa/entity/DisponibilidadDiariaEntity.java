package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "disponibilidad_diaria")
@Data
public class DisponibilidadDiariaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_id", nullable = false)
    private GuiaJpaEntity guia;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dia;

    private boolean disponible;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "disponibilidad_franjas", joinColumns = @JoinColumn(name = "disponibilidad_id"))
    private List<FranjaHorariaEmbeddable> franjas;
}