package com.microservicio.guias.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GUIAS_EVENTS_EXCHANGE = "guias.events.exchange";
    public static final String SYNC_READ_MODEL_QUEUE = "guias.sync.read_model.queue";
    public static final String GUIAS_EVENTS_ROUTING_KEY = "guias.event.#";

    @Bean
    public TopicExchange guiasEventsExchange() {
        return new TopicExchange(GUIAS_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue syncReadModelQueue() {
        return new Queue(SYNC_READ_MODEL_QUEUE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(GUIAS_EVENTS_ROUTING_KEY);
    }

    /**
     * Define que los mensajes se convertirán a y desde JSON.
     * Esto permite enviar nuestros objetos de evento (Ej. GuiaCreadoEvent) directamente.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}