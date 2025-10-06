package com.microservicio.guias.infrastructure.config;
import com.microservicio.guias.application.port.input.*;
import com.microservicio.guias.application.port.output.GuiaCommandRepository;
import com.microservicio.guias.application.port.output.GuiaQueryRepository;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // Command Use Cases
    @Bean
    public CreateGuiaUseCase createGuiaUseCase(GuiaCommandRepository commandRepository, EventPublisher eventPublisher) {
        return new CreateGuiaUseCaseImpl(commandRepository, eventPublisher);
    }

    @Bean
    public UpdateGuiaUseCase updateGuiaUseCase(GuiaCommandRepository commandRepository, EventPublisher eventPublisher) {
        return new UpdateGuiaUseCaseImpl(commandRepository, eventPublisher);
    }

    @Bean
    public DeleteGuiaUseCase deleteGuiaUseCase(GuiaCommandRepository commandRepository, EventPublisher eventPublisher) {
        return new DeleteGuiaUseCaseImpl(commandRepository, eventPublisher);
    }

    // Query Use Cases
    @Bean
    public FindGuiaQuery findGuiaQuery(GuiaQueryRepository queryRepository) {
        return new FindGuiaQueryImpl(queryRepository);
    }
    
    @Bean
    public FindHorariosDeGuiaQuery findHorariosDeGuiaQuery(GuiaQueryRepository queryRepository) {
        return new FindHorariosDeGuiaQueryImpl(queryRepository);
    }

    // Synchronization Use Case
    @Bean
    public SynchronizeGuiaReadModelUseCase synchronizeGuiaReadModelUseCase(GuiaCommandRepository commandRepo, GuiaQueryRepository queryRepo) {
        return new SynchronizeGuiaReadModelUseCaseImpl(commandRepo, queryRepo);
    }
}