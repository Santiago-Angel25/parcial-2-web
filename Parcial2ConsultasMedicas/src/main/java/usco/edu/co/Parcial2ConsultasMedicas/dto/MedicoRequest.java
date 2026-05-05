package usco.edu.co.Parcial2ConsultasMedicas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para crear o actualizar un medico")
public class MedicoRequest {

    @Schema(description = "Nombres del medico", example = "Ana")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @Schema(description = "Apellidos del medico", example = "Gomez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 60, message = "El apellido no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El apellido solo debe contener letras y espacios")
    private String apellido;

    @Schema(description = "Especialidad medica permitida", example = "Medicina general", allowableValues = {
            "Medicina general", "Pediatria", "Dermatologia", "Cardiologia", "Odontologia", "Ginecologia", "Ortopedia"
    })
    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 60, message = "La especialidad no debe superar 60 caracteres")
    @Pattern(regexp = "^(Medicina general|Pediatria|Dermatologia|Cardiologia|Odontologia|Ginecologia|Ortopedia)$", message = "Seleccione una especialidad valida")
    private String especialidad;

    @Schema(description = "Usuario de acceso del medico", example = "anagomez")
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 30, message = "El usuario no debe superar 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El usuario solo debe contener letras y numeros")
    private String username;

    @Schema(description = "Contrasena inicial del medico. Obligatoria al crear.", example = "anagomez")
    @Size(max = 60, message = "La contrasena no debe superar 60 caracteres")
    private String password;


}
