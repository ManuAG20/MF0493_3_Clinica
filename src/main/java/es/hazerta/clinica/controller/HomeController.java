package es.hazerta.clinica.controller;

import es.hazerta.clinica.service.CitaService;
import es.hazerta.clinica.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de inicio. Muestra los accesos a pacientes y citas.
 */
@Controller
public class HomeController {

    private final PacienteService pacienteService;
    private final CitaService citaService;

    public HomeController(PacienteService pacienteService, CitaService citaService) {
        this.pacienteService = pacienteService;
        this.citaService = citaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("numPacientes", pacienteService.contar());
        model.addAttribute("numCitas", citaService.contar());
        return "index";
    }
}
