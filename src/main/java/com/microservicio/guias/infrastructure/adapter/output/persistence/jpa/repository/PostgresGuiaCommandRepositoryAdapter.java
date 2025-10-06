package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.repository;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper.GuiaJpaMapper;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class PostgresGuiaCommandRepositoryAdapter implements GuiaCommandRepository {

    private final SpringDataGuiaJpaRepository jpaRepository;
    private final GuiaJpaMapper mapper;

    public PostgresGuiaCommandRepositoryAdapter(SpringDataGuiaJpaRepository jpaRepository, GuiaJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Guia save(Guia guia) {
        var jpaEntity = mapper.toJpaEntity(guia);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Guia> findById(GuiaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomainEntity);
    }

    @Override
    public void deleteById(GuiaId id) {
        jpaRepository.deleteById(id.value());
    }
}