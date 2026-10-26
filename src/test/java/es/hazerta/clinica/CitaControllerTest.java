package es.hazerta.clinica;

import es.hazerta.clinica.model.Cita;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.CitaRepository;
import es.hazerta.clinica.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración del CRUD de citas y de la relación con pacientes.
 */
@SpringBootTest(properties = {
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "spring.main.banner-mode=off"
})
@AutoConfigureMockMvc
@Transactional
class CitaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @DisplayName("El listado de citas muestra motivo y paciente")
    void listadoMuestraCitas() throws Exception {
        mvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Revisión anual")))
                .andExpect(content().string(containsString("Ana García")));
    }

    @Test
    @DisplayName("Se puede crear una cita asociada a un paciente")
    void crearCitaValida() throws Exception {
        Paciente paciente = pacienteRepository.findAllByOrderByNombreAsc().get(0);
        long antes = citaRepository.count();
        String fecha = LocalDate.now().plusDays(15).toString();

        mvc.perform(post("/citas/guardar")
                        .param("fecha", fecha)
                        .param("motivo", "Consulta de revisión")
                        .param("pacienteId", paciente.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/citas"));

        assertEquals(antes + 1, citaRepository.count());
    }

    @Test
    @DisplayName("La validación impide guardar una cita sin motivo ni paciente")
    void validacionImpideGuardarCitaIncompleta() throws Exception {
        long antes = citaRepository.count();

        mvc.perform(post("/citas/guardar")
                        .param("fecha", "")
                        .param("motivo", "")
                        .param("pacienteId", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("La fecha es obligatoria")))
                .andExpect(content().string(containsString("El motivo debe tener al menos 3 caracteres")))
                .andExpect(content().string(containsString("Debes seleccionar un paciente")));

        assertEquals(antes, citaRepository.count(), "No debe guardarse ninguna cita");
    }

    @Test
    @DisplayName("Se puede borrar una cita")
    void borrarCita() throws Exception {
        Cita cita = citaRepository.findAllByOrderByFechaAsc().get(0);
        long antes = citaRepository.count();

        mvc.perform(post("/citas/borrar/" + cita.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/citas"));

        assertEquals(antes - 1, citaRepository.count());
        assertFalse(citaRepository.findById(cita.getId()).isPresent());
    }
}
