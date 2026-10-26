package es.hazerta.clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Gestión centralizada de errores: cualquier controlador que lance una
 * excepción mostrará una página de error controlada en lugar de un stacktrace.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ModelAndView recursoNoEncontrado(RecursoNoEncontradoException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("mensaje", ex.getMessage());
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView errorInesperado(Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("mensaje", "Ha ocurrido un error inesperado. Inténtalo de nuevo.");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
