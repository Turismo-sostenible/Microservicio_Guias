package com.microservicio.guias.infrastructure.adapter.output.messaging;
import com.microservicio.guias.application.port.output.EventPublisher;
import com.microservicio.guias.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisherAdapter implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        // Construimos una "routing key" dinámica, ej: "guias.event.GuiaCreadoEvent"
        String routingKey = "guias.event." + event.getClass().getSimpleName();
        
        // Enviamos el evento al exchange con la routing key. RabbitMQ se encarga del resto.
        rabbitTemplate.convertAndSend(RabbitMQConfig.GUIAS_EVENTS_EXCHANGE, routingKey, event);
    }
}