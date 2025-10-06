package com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.repository;

import com.microservicio.guias.infrastructure.adapter.output.persistence.mongodb.document.GuiaMongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataGuiaMongoRepository extends MongoRepository<GuiaMongoDocument, String> {}