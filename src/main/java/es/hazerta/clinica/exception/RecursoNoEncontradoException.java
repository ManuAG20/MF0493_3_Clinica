package es.hazerta.clinica.exception;

/**
 * Se lanza cuando se pide un paciente o una cita que no existe.
 * Se traduce en una página 404 amable para el usuario.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
