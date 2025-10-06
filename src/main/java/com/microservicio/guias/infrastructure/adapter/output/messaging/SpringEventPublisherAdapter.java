package com.microservicio.guias.infrastructure.adapter.output.messaging;
import com.microservicio.guias.application.port.output.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Implementación del puerto EventPublisher usando el bus de eventos interno de Spring.
 * Para sistemas más complejos, esto podría ser un cliente de Kafka o RabbitMQ.
 */
@Component
public class SpringEventPublisherAdapter implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Object event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}