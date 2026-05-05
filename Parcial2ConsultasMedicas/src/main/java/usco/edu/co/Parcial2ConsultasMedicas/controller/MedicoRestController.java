package usco.edu.co.Parcial2ConsultasMedicas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import usco.edu.co.Parcial2ConsultasMedicas.dto.MedicoRequest;
import usco.edu.co.Parcial2ConsultasMedicas.model.Medico;
import usco.edu.co.Parcial2ConsultasMedicas.service.MedicoService;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Servicios web para que el administrador gestione medicos")
public class MedicoRestController {

    private final MedicoService medicoService;

    public MedicoRestController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Listar medicos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public List<Medico> listar() {
        return medicoService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear medico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medico creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public Medico crear(@Valid @RequestBody MedicoRequest request) {
        return medicoService.crear(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar medico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medico actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public Medico actualizar(@PathVariable Long id, @Valid @RequestBody MedicoRequest request) {
        return medicoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar medico")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medico eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos")
    })
    public void eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
    }
}

