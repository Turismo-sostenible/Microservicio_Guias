package com.microservicio.guias.application.usecase;
import com.microservicio.guias.application.port.input.FindGuiaQuery;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import java.util.List;
import java.util.Optional;

public class FindGuiaQueryImpl implements FindGuiaQuery {

    private final GuiaQueryRepository guiaQueryRepository;

    public FindGuiaQueryImpl(GuiaQueryRepository guiaQueryRepository) {
        this.guiaQueryRepository = guiaQueryRepository;
    }

    @Override
    public Optional<Guia> findById(GuiaId id) {
        return guiaQueryRepository.findById(id);
    }

    @Override
    public List<Guia> findAll() {
        return guiaQueryRepository.findAll();
    }
}