package es.hazerta.clinica;

import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración del CRUD de pacientes usando MockMvc,
 * que simula las peticiones de un navegador.
 */
@SpringBootTest(properties = {
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "spring.main.banner-mode=off"
})
@AutoConfigureMockMvc
@Transactional
class PacienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @DisplayName("El listado muestra los pacientes de ejemplo")
    void listadoMuestraPacientes() throws Exception {
        mvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ana García")))
                .andExpect(content().string(containsString("Luis Martínez")));
    }

    @Test
    @DisplayName("Se puede crear un paciente con datos válidos")
    void crearPacienteValido() throws Exception {
        long antes = pacienteRepository.count();

        mvc.perform(post("/pacientes/guardar")
                        .param("nombre", "Pedro Ruiz")
                        .param("email", "pedro.ruiz@example.com")
                        .param("telefono", "699888777"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));

        assertEquals(antes + 1, pacienteRepository.count());
    }

    @Test
    @DisplayName("La validación impide guardar un paciente sin nombre y con email incorrecto")
    void validacionImpideGuardarDatosIncorrectos() throws Exception {
        long antes = pacienteRepository.count();

        mvc.perform(post("/pacientes/guardar")
                        .param("nombre", "")
                        .param("email", "correo-malo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("El nombre debe tener al menos 2 caracteres")))
                .andExpect(content().string(containsString("El email no tiene un formato válido")));

        assertEquals(antes, pacienteRepository.count(), "No debe guardarse ningún paciente");
    }

    @Test
    @DisplayName("Se puede editar un paciente existente")
    void editarPaciente() throws Exception {
        Paciente paciente = pacienteRepository.findAllByOrderByNombreAsc().get(0);

        mvc.perform(post("/pacientes/guardar")
                        .param("id", paciente.getId().toString())
                        .param("nombre", "Ana García Actualizada")
                        .param("email", paciente.getEmail())
                        .param("telefono", "600111999"))
                .andExpect(status().is3xxRedirection());

        Paciente actualizado = pacienteRepository.findById(paciente.getId()).orElseThrow();
        assertEquals("Ana García Actualizada", actualizado.getNombre());
        assertEquals("600111999", actualizado.getTelefono());
    }

    @Test
    @DisplayName("Se puede borrar un paciente")
    void borrarPaciente() throws Exception {
        Paciente paciente = pacienteRepository.findAllByOrderByNombreAsc().get(0);
        long antes = pacienteRepository.count();

        mvc.perform(post("/pacientes/borrar/" + paciente.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pacientes"));

        assertEquals(antes - 1, pacienteRepository.count());
        assertFalse(pacienteRepository.findById(paciente.getId()).isPresent());
    }
}
