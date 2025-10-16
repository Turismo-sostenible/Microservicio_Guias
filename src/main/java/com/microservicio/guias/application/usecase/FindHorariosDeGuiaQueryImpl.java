package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.FindHorariosDeGuiaQuery;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.GuiaId;

import java.util.List;

public class FindHorariosDeGuiaQueryImpl implements FindHorariosDeGuiaQuery {
    
    private final GuiaQueryRepository guiaQueryRepository;

    public FindHorariosDeGuiaQueryImpl(GuiaQueryRepository guiaQueryRepository) {
        this.guiaQueryRepository = guiaQueryRepository;
    }

    @Override
    public List<DisponibilidadDiaria> findHorariosByGuiaId(GuiaId id) {
        return guiaQueryRepository.findHorariosByGuiaId(id);
    }
}