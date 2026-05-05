package usco.edu.co.Parcial2ConsultasMedicas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import usco.edu.co.Parcial2ConsultasMedicas.model.Paciente;
import usco.edu.co.Parcial2ConsultasMedicas.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Servicios web para buscar pacientes registrados")
public class PacienteRestController {

    private final PacienteService pacienteService;

    public PacienteRestController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Buscar paciente por documento", description = "Permite confirmar que el paciente existe antes de registrar una consulta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public Paciente buscar(@RequestParam String documento) {
        return pacienteService.buscarPorDocumento(documento);
    }
}

