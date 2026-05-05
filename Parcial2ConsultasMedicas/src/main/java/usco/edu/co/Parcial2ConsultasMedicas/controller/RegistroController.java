package usco.edu.co.Parcial2ConsultasMedicas.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import usco.edu.co.Parcial2ConsultasMedicas.dto.PacienteRegistroRequest;
import usco.edu.co.Parcial2ConsultasMedicas.service.PacienteService;

@Controller
public class RegistroController {

    private final PacienteService pacienteService;

    public RegistroController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        if (!model.containsAttribute("pacienteRegistroRequest")) {
            model.addAttribute("pacienteRegistroRequest", new PacienteRegistroRequest());
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid PacienteRegistroRequest pacienteRegistroRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }

        try {
            pacienteService.registrar(pacienteRegistroRequest);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorGeneral", exception.getMessage());
            return "registro";
        }

        redirectAttributes.addAttribute("registrado", true);
        return "redirect:/login";
    }
}
