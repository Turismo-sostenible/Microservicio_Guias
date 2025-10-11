
package com.microservicio.guias.infrastructure.adapter.input.event;
import com.microservicio.guias.domain.event.GuiaCreadoEvent;
import com.microservicio.guias.application.port.input.SynchronizeGuiaReadModelUseCase;
import com.microservicio.guias.domain.event.GuiaActualizadoEvent;
import com.microservicio.guias.domain.event.GuiaEliminadoEvent;
import com.microservicio.guias.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventListener {

    private final SynchronizeGuiaReadModelUseCase synchronizeGuiaReadModelUseCase;

    public RabbitMQEventListener(SynchronizeGuiaReadModelUseCase synchronizeGuiaReadModelUseCase) {
        this.synchronizeGuiaReadModelUseCase = synchronizeGuiaReadModelUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.SYNC_READ_MODEL_QUEUE)
    public void handleGuiaCreado(GuiaCreadoEvent event) {
        // Spring AMQP deserializa el mensaje JSON de vuelta al objeto correcto automáticamente
        synchronizeGuiaReadModelUseCase.handleGuiaCreado(event);
    }

    @RabbitListener(queues = RabbitMQConfig.SYNC_READ_MODEL_QUEUE)
    public void handleGuiaActualizado(GuiaActualizadoEvent event) {
        synchronizeGuiaReadModelUseCase.handleGuiaActualizado(event);
    }
    
    @RabbitListener(queues = RabbitMQConfig.SYNC_READ_MODEL_QUEUE)
    public void handleGuiaEliminado(GuiaEliminadoEvent event) {
        synchronizeGuiaReadModelUseCase.handleGuiaEliminado(event);
    }
}