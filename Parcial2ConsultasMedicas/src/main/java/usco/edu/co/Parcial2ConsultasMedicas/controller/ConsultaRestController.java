package usco.edu.co.Parcial2ConsultasMedicas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import usco.edu.co.Parcial2ConsultasMedicas.dto.ConsultaRequest;
import usco.edu.co.Parcial2ConsultasMedicas.dto.HorarioRequest;
import usco.edu.co.Parcial2ConsultasMedicas.model.Consulta;
import usco.edu.co.Parcial2ConsultasMedicas.service.ConsultaService;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas medicas", description = "Servicios web para gestionar consultas del centro medico")
public class ConsultaRestController {

    private final ConsultaService consultaService;

    public ConsultaRestController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Listar consultas", description = "El administrador consulta todas las citas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public List<Consulta> listar() {
        return consultaService.listarTodas();
    }

    @GetMapping("/mis-medico")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Listar consultas del medico", description = "El medico solo visualiza las consultas asignadas a su usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado del medico obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public List<Consulta> listarMedico(Principal principal) {
        return consultaService.listarPorMedico(principal.getName());
    }

    @GetMapping("/mis-paciente")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Listar consultas del paciente", description = "El paciente solo visualiza las consultas asignadas a su usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado del paciente obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public List<Consulta> listarPaciente(Principal principal) {
        return consultaService.listarPorPaciente(principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear consulta", description = "Registra paciente, motivo, consultorio, horario, medico y paciente asociado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o cruce de horario"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public Consulta crear(@Valid @RequestBody ConsultaRequest request) {
        return consultaService.crear(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar consulta", description = "Modifica todos los datos de una consulta existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o cruce de horario"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public Consulta actualizar(@Parameter(description = "ID de la consulta") @PathVariable Long id,
                               @Valid @RequestBody ConsultaRequest request) {
        return consultaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar consulta", description = "Elimina una consulta registrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public void eliminar(@Parameter(description = "ID de la consulta") @PathVariable Long id) {
        consultaService.eliminar(id);
    }

    @PutMapping("/{id}/horario")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Actualizar horario como paciente", description = "Permite al paciente cambiar el horario solo de citas asignadas a su usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Horario invalido o cruce de horario"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public Consulta actualizarHorario(@Parameter(description = "ID de la consulta") @PathVariable Long id,
                                      @Valid @RequestBody HorarioRequest request,
                                      Principal principal) {
        return consultaService.actualizarHorarioComoPaciente(id, request, principal.getName());
    }
}

