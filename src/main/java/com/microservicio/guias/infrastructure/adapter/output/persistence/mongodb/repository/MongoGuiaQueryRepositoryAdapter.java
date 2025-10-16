package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.repository;

import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.mapper.GuiaMongoMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoGuiaQueryRepositoryAdapter implements GuiaQueryRepository {

    private final SpringDataGuiaMongoRepository mongoRepository;
    private final GuiaMongoMapper mapper;

    public MongoGuiaQueryRepositoryAdapter(SpringDataGuiaMongoRepository mongoRepository, GuiaMongoMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Guia save(Guia guia) {
        GuiaMongoDocument doc = mapper.toDocument(guia);
        mongoRepository.save(doc);
        return guia;
    }

    @Override
    public void deleteById(GuiaId id) {
        mongoRepository.deleteById(id.value().toString());
    }

    @Override
    public Optional<Guia> findById(GuiaId id) {
        return mongoRepository.findById(id.value().toString())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Guia> findAll() {
        return mongoRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DisponibilidadDiaria> findHorariosByGuiaId(GuiaId id) {
        return findById(id)
                .map(Guia::getDisponibilidadSemanal)
                .orElse(Collections.emptyList());
    }
}