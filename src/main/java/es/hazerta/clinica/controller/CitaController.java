package es.hazerta.clinica.controller;

import es.hazerta.clinica.model.Cita;
import es.hazerta.clinica.model.Paciente;
import es.hazerta.clinica.service.CitaService;
import es.hazerta.clinica.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Controlador de Cita. Atiende las rutas de listar, crear, editar y borrar.
 */
@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final PacienteService pacienteService;

    public CitaController(CitaService citaService, PacienteService pacienteService) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("citas", citaService.listar());
        return "citas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("pacientes", pacienteService.listar());
        return "citas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cita", citaService.buscarPorId(id));
        model.addAttribute("pacientes", pacienteService.listar());
        return "citas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Long id,
                          @RequestParam(required = false) String fecha,
                          @RequestParam(required = false) String motivo,
                          @RequestParam(required = false) Long pacienteId,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        Cita cita = new Cita();
        cita.setId(id);
        cita.setMotivo(motivo);

        if (pacienteId != null) {
            Paciente paciente = new Paciente();
            paciente.setId(pacienteId);
            cita.setPaciente(paciente);
        }

        Map<String, String> errores = new java.util.LinkedHashMap<>(citaService.validar(cita, pacienteId));

        if (fecha != null && !fecha.isBlank() && errores.containsKey("fecha")) {
            try {
                cita.setFecha(LocalDate.parse(fecha));
                errores.remove("fecha");
            } catch (DateTimeParseException ex) {
                errores.put("fecha", "La fecha no tiene un formato válido.");
            }
        }

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("cita", cita);
            model.addAttribute("pacientes", pacienteService.listar());
            return "citas/formulario";
        }

        citaService.guardar(cita, pacienteId);
        redirectAttributes.addFlashAttribute("mensaje", "Cita guardada correctamente.");
        return "redirect:/citas";
    }

    @PostMapping("/borrar/{id}")
    public String borrar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        citaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cita eliminada correctamente.");
        return "redirect:/citas";
    }
}
