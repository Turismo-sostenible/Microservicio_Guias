package com.microservicio.guias.infrastructure.adapter.input.rest.mapper;
import com.microservicio.guias.application.port.input.CreateGuiaUseCase;
import com.microservicio.guias.application.port.input.UpdateGuiaUseCase;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.CreateGuiaRequest;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.GuiaResponse;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.HorarioResponse;
import com.microservicio.guias.infrastructure.adapter.input.rest.dto.UpdateGuiaRequest;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.domain.model.GuiaId;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class GuiaApiMapper {

    public CreateGuiaUseCase.CreateGuiaCommand toCommand(CreateGuiaRequest request) {
        return new CreateGuiaUseCase.CreateGuiaCommand(request.nombre(), request.email(), request.telefono());
    }

    public UpdateGuiaUseCase.UpdateGuiaCommand toCommand(UpdateGuiaRequest request, String id) {
        return new UpdateGuiaUseCase.UpdateGuiaCommand(GuiaId.fromString(id), request.nombre(), request.email(), request.telefono());
    }
    
    public GuiaResponse toResponse(Guia guia) {
        return new GuiaResponse(
                guia.getId().value(),
                guia.getNombre(),
                guia.getEmail(),
                guia.getTelefono(),
                guia.getEstado().name(),
                guia.getHorarios().stream()
                        .map(h -> new HorarioResponse(h.fechaHoraInicio(), h.fechaHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}