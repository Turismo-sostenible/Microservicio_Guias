package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDocument {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
}