package usco.edu.co.Parcial2ConsultasMedicas.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import usco.edu.co.Parcial2ConsultasMedicas.repository.MedicoRepository;
import usco.edu.co.Parcial2ConsultasMedicas.repository.PacienteRepository;
import usco.edu.co.Parcial2ConsultasMedicas.service.ConsultaService;

@Controller
public class HomeController {

    private final ConsultaService consultaService;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public HomeController(ConsultaService consultaService, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaService = consultaService;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/administrador")
    public String administrador(Model model) {
        model.addAttribute("consultas", consultaService.listarTodas());
        model.addAttribute("medicos", medicoRepository.findAll());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("totalConsultas", consultaService.listarTodas().size());
        model.addAttribute("totalMedicos", medicoRepository.count());
        return "administrador";
    }

    @GetMapping("/medico")
    public String medico(Model model, Principal principal) {
        model.addAttribute("consultas", consultaService.listarPorMedico(principal.getName()));
        return "medico";
    }

    @GetMapping("/paciente")
    public String paciente(Model model, Principal principal) {
        model.addAttribute("consultas", consultaService.listarPorPaciente(principal.getName()));
        return "paciente";
    }

    @GetMapping("/sin-permiso")
    public String sinPermiso() {
        return "sin-permiso";
    }
}
