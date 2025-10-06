package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.repository;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.entity.GuiaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataGuiaJpaRepository extends JpaRepository<GuiaJpaEntity, UUID> {}