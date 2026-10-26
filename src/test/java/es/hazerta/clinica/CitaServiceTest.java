package es.hazerta.clinica;

import es.hazerta.clinica.exception.RecursoNoEncontradoException;
import es.hazerta.clinica.model.Cita;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.CitaRepository;
import es.hazerta.clinica.repository.PacienteRepository;
import es.hazerta.clinica.service.CitaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la lógica de negocio de Cita con repositorios simulados.
 */
@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private CitaService citaService;

    @Test
    @DisplayName("Una cita sin fecha, sin motivo y sin paciente genera tres errores")
    void validaCamposObligatorios() {
        Cita cita = new Cita();

        Map<String, String> errores = citaService.validar(cita, null);

        assertTrue(errores.containsKey("fecha"));
        assertTrue(errores.containsKey("motivo"));
        assertTrue(errores.containsKey("paciente"));
        assertEquals(3, errores.size());
    }

    @Test
    @DisplayName("Guardar una cita asocia el paciente existente")
    void guardarAsociaPaciente() {
        Paciente paciente = new Paciente("Ana", "ana@example.com", "600111222");
        paciente.setId(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        Cita cita = new Cita(LocalDate.of(2026, 12, 1), "  Revisión  ", null);
        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cita guardada = citaService.guardar(cita, 1L);

        assertSame(paciente, guardada.getPaciente());
        assertEquals("Revisión", guardada.getMotivo());
        verify(citaRepository).save(cita);
    }

    @Test
    @DisplayName("Guardar una cita con un paciente inexistente lanza excepción")
    void guardarConPacienteInexistenteLanzaExcepcion() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());
        Cita cita = new Cita(LocalDate.of(2026, 12, 1), "Revisión", null);

        assertThrows(RecursoNoEncontradoException.class,
                () -> citaService.guardar(cita, 99L));
    }

    @Test
    @DisplayName("Buscar una cita inexistente lanza RecursoNoEncontradoException")
    void buscarPorIdInexistenteLanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> citaService.buscarPorId(99L));
    }
}
