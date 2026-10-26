package es.hazerta.clinica.repository;

import es.hazerta.clinica.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Cita. Al extender JpaRepository ya tenemos el CRUD
 * completo (findAll, findById, save, deleteById, count...) sin escribir SQL.
 */
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findAllByOrderByFechaAsc();

    List<Cita> findByPacienteIdOrderByFechaAsc(Long pacienteId);
}
