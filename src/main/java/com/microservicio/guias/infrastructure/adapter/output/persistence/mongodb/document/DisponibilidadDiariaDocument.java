package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document;
import lombok.Data;
import java.util.List;

@Data
public class DisponibilidadDiariaDocument {
    private String dia; // Guardamos el día como String
    private boolean disponible;
    private List<FranjaHorariaDocument> franjas;
}