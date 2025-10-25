package com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.repository;

import com.microservicio.guias.BaseIntegrationTest;
import com.microservicio.guias.domain.model.DisponibilidadDiaria;
import com.microservicio.guias.domain.model.FranjaHoraria;
import com.microservicio.guias.domain.model.Guia;
import com.microservicio.guias.infrastructure.adapter.output.persistence.jpa.mapper.GuiaJpaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// ¡Se eliminaron @DataJpaTest, @ActiveProfiles y @AutoConfigureTestDatabase!
@Import({PostgresGuiaCommandRepositoryAdapter.class, GuiaJpaMapper.class})
class PostgresGuiaCommandRepositoryAdapterIT extends BaseIntegrationTest {

    @Autowired
    private PostgresGuiaCommandRepositoryAdapter adapter;

    @Autowired
    private SpringDataGuiaJpaRepository jpaRepository;

    @Test
    @DisplayName("Debería guardar un Guía con disponibilidad en la BD Postgres")
    void deberiaGuardarYRecuperarGuia() {
        Guia guia = Guia.crear("Guia Postgres", "pg@test.com", "555");
        List<DisponibilidadDiaria> disponibilidad = List.of(
            new DisponibilidadDiaria(
                DayOfWeek.MONDAY,
                true,
                List.of(new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(12, 0)))
            )
        );
        guia.actualizarDisponibilidad(disponibilidad);

        adapter.save(guia);
        
        Optional<Guia> guiaEncontradoOpt = adapter.findById(guia.getId());

        assertTrue(guiaEncontradoOpt.isPresent());
        Guia guiaEncontrado = guiaEncontradoOpt.get();
        
        assertEquals("Guia Postgres", guiaEncontrado.getNombre());
        assertEquals(1, guiaEncontrado.getDisponibilidadSemanal().size());
        assertEquals(DayOfWeek.MONDAY, guiaEncontrado.getDisponibilidadSemanal().get(0).dia());
        assertEquals(LocalTime.of(9, 0), guiaEncontrado.getDisponibilidadSemanal().get(0).franjas().get(0).horaInicio());

        assertEquals(1, jpaRepository.count());
    }

    @Test
    @DisplayName("Debería eliminar un Guía de la BD Postgres")
    void deberiaEliminarGuia() {
        Guia guia = Guia.crear("Para Borrar", "borrar@test.com", "000");
        adapter.save(guia);
        assertEquals(1, jpaRepository.count());

        adapter.deleteById(guia.getId());

        Optional<Guia> guiaEncontradoOpt = adapter.findById(guia.getId());
        assertFalse(guiaEncontradoOpt.isPresent());
        assertEquals(0, jpaRepository.count());
    }
}