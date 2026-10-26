package es.hazerta.clinica.repository;

import es.hazerta.clinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Paciente. Al extender JpaRepository ya tenemos el CRUD
 * completo (findAll, findById, save, deleteById, count...) sin escribir SQL.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findAllByOrderByNombreAsc();

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
