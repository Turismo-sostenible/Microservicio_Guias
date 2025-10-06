package com.microservicio.guias.application.port.output;
/**
 * Puerto de Salida para publicar eventos de dominio.
 * Desacopla la lógica de negocio de la notificación de eventos.
 */
public interface EventPublisher {
    void publish(Object event);
}