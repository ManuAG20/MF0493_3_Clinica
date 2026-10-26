package es.hazerta.clinica;

import es.hazerta.clinica.repository.CitaRepository;
import es.hazerta.clinica.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integración: arranca la aplicación completa y comprueba que la
 * base de datos H2 se inicializa con schema.sql y data.sql.
 */
@SpringBootTest(properties = {
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "spring.main.banner-mode=off"
})
class ClinicaApplicationTests {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Test
    @DisplayName("La aplicación arranca y carga los datos de ejemplo")
    void cargaDatosDeEjemplo() {
        assertEquals(3, pacienteRepository.count(), "Deben existir 3 pacientes de ejemplo");
        assertEquals(5, citaRepository.count(), "Deben existir 5 citas de ejemplo");
    }

    @Test
    @DisplayName("Cada cita está relacionada con un paciente")
    void lasCitasTienenPaciente() {
        assertTrue(citaRepository.findAll().stream()
                .allMatch(cita -> cita.getPaciente() != null),
                "Todas las citas deben tener un paciente asociado");
    }
}
