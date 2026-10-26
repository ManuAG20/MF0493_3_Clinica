package es.hazerta.clinica.service;

import es.hazerta.clinica.exception.RecursoNoEncontradoException;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio de Paciente: contiene la lógica de negocio y la validación.
 * El controlador no habla directamente con el repositorio.
 */
@Service
@Transactional(readOnly = true)
public class PacienteService {

    private static final String PATRON_EMAIL = "^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$";
    private static final String PATRON_TELEFONO = "^[0-9+ -]{6,30}$";

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listar() {
        return pacienteRepository.findAllByOrderByNombreAsc();
    }

    public long contar() {
        return pacienteRepository.count();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el paciente con id " + id));
    }

    /**
     * Valida los datos del formulario. Devuelve un mapa campo -> mensaje.
     * Un mapa vacío significa que todo es correcto.
     */
    public Map<String, String> validar(Paciente paciente) {
        Map<String, String> errores = new LinkedHashMap<>();

        String nombre = paciente.getNombre() == null ? "" : paciente.getNombre().trim();
        if (nombre.length() < 2) {
            errores.put("nombre", "El nombre debe tener al menos 2 caracteres.");
        } else if (nombre.length() > 100) {
            errores.put("nombre", "El nombre no puede superar los 100 caracteres.");
        }

        String email = paciente.getEmail() == null ? "" : paciente.getEmail().trim();
        if (email.isEmpty()) {
            errores.put("email", "El email es obligatorio.");
        } else if (!email.matches(PATRON_EMAIL)) {
            errores.put("email", "El email no tiene un formato válido (ejemplo@dominio.com).");
        } else {
            boolean duplicado = paciente.getId() == null
                    ? pacienteRepository.existsByEmailIgnoreCase(email)
                    : pacienteRepository.existsByEmailIgnoreCaseAndIdNot(email, paciente.getId());
            if (duplicado) {
                errores.put("email", "Ya existe otro paciente con ese email.");
            }
        }

        String telefono = paciente.getTelefono() == null ? "" : paciente.getTelefono().trim();
        if (!telefono.isEmpty() && !telefono.matches(PATRON_TELEFONO)) {
            errores.put("telefono", "El teléfono solo admite números, espacios, + y - (6 a 30 caracteres).");
        }

        return errores;
    }

    @Transactional
    public Paciente guardar(Paciente paciente) {
        paciente.setNombre(paciente.getNombre().trim());
        paciente.setEmail(paciente.getEmail().trim());
        if (paciente.getTelefono() != null) {
            paciente.setTelefono(paciente.getTelefono().trim());
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        pacienteRepository.delete(paciente);
    }
}
