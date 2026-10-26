package es.hazerta.clinica;

import es.hazerta.clinica.exception.RecursoNoEncontradoException;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.PacienteRepository;
import es.hazerta.clinica.service.PacienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la lógica de negocio de Paciente con un repositorio simulado.
 */
@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("Buscar un paciente inexistente lanza RecursoNoEncontradoException")
    void buscarPorIdInexistenteLanzaExcepcion() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> pacienteService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Un email con formato incorrecto se rechaza")
    void validaEmailIncorrecto() {
        Paciente paciente = new Paciente("Ana", "correo-malo", "600111222");

        Map<String, String> errores = pacienteService.validar(paciente);

        assertTrue(errores.containsKey("email"));
    }

    @Test
    @DisplayName("Un email repetido se rechaza al crear un paciente")
    void validaEmailDuplicado() {
        Paciente paciente = new Paciente("Ana", "ana@example.com", "600111222");
        when(pacienteRepository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(true);

        Map<String, String> errores = pacienteService.validar(paciente);

        assertTrue(errores.containsKey("email"));
        assertTrue(errores.get("email").contains("Ya existe"));
    }

    @Test
    @DisplayName("Un paciente con datos correctos no genera errores")
    void validaPacienteCorrecto() {
        Paciente paciente = new Paciente("Ana García", "ana@example.com", "600111222");
        when(pacienteRepository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(false);

        Map<String, String> errores = pacienteService.validar(paciente);

        assertTrue(errores.isEmpty());
    }

    @Test
    @DisplayName("Al guardar se eliminan los espacios sobrantes")
    void guardarRecortaEspacios() {
        Paciente paciente = new Paciente("  Ana  ", "  ana@example.com  ", " 600111222 ");
        when(pacienteRepository.save(any(Paciente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Paciente guardado = pacienteService.guardar(paciente);

        assertEquals("Ana", guardado.getNombre());
        assertEquals("ana@example.com", guardado.getEmail());
        assertEquals("600111222", guardado.getTelefono());
        verify(pacienteRepository).save(paciente);
    }

    @Test
    @DisplayName("Un nombre demasiado corto se rechaza")
    void validaNombreCorto() {
        Paciente paciente = new Paciente("A", "ana@example.com", null);

        Map<String, String> errores = pacienteService.validar(paciente);

        assertTrue(errores.containsKey("nombre"));
    }

    @Test
    @DisplayName("Eliminar un paciente inexistente no llama a delete")
    void eliminarInexistenteNoBorra() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> pacienteService.eliminar(99L));
        verify(pacienteRepository, never()).delete(any(Paciente.class));
    }
}
