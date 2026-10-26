package es.hazerta.clinica.controller;

import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Controlador de Paciente. Atiende las rutas de listar, crear, editar y borrar.
 */
@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteService.listar());
        return "pacientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", pacienteService.buscarPorId(id));
        return "pacientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Paciente paciente, Model model,
                          RedirectAttributes redirectAttributes) {
        Map<String, String> errores = pacienteService.validar(paciente);
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            return "pacientes/formulario";
        }
        pacienteService.guardar(paciente);
        redirectAttributes.addFlashAttribute("mensaje", "Paciente guardado correctamente.");
        return "redirect:/pacientes";
    }

    @PostMapping("/borrar/{id}")
    public String borrar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pacienteService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Paciente eliminado correctamente.");
        return "redirect:/pacientes";
    }
}
