package es.hazerta.clinica.service;

import es.hazerta.clinica.exception.RecursoNoEncontradoException;
import es.hazerta.clinica.model.Cita;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.repository.CitaRepository;
import es.hazerta.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio de Cita: lógica de negocio, validación y asociación
 * de cada cita con su paciente.
 */
@Service
@Transactional(readOnly = true)
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;

    public CitaService(CitaRepository citaRepository, PacienteRepository pacienteRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Cita> listar() {
        return citaRepository.findAllByOrderByFechaAsc();
    }

    public long contar() {
        return citaRepository.count();
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la cita con id " + id));
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteIdOrderByFechaAsc(pacienteId);
    }

    public Map<String, String> validar(Cita cita, Long pacienteId) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (cita.getFecha() == null) {
            errores.put("fecha", "La fecha es obligatoria.");
        }

        String motivo = cita.getMotivo() == null ? "" : cita.getMotivo().trim();
        if (motivo.length() < 3) {
            errores.put("motivo", "El motivo debe tener al menos 3 caracteres.");
        } else if (motivo.length() > 200) {
            errores.put("motivo", "El motivo no puede superar los 200 caracteres.");
        }

        if (pacienteId == null) {
            errores.put("paciente", "Debes seleccionar un paciente.");
        } else if (!pacienteRepository.existsById(pacienteId)) {
            errores.put("paciente", "El paciente seleccionado no existe.");
        }

        return errores;
    }

    @Transactional
    public Cita guardar(Cita cita, Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el paciente con id " + pacienteId));
        cita.setPaciente(paciente);
        cita.setMotivo(cita.getMotivo().trim());
        return citaRepository.save(cita);
    }

    @Transactional
    public void eliminar(Long id) {
        Cita cita = buscarPorId(id);
        citaRepository.delete(cita);
    }
}
