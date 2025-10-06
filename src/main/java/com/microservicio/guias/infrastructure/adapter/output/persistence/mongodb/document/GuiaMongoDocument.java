package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "guias_read_model")
@Data
public class GuiaMongoDocument {
    @Id
    private String id; // UUID como String
    private String nombre;
    private String email;
    private String telefono;
    private String estado;
    private List<HorarioDocument> horarios;
}