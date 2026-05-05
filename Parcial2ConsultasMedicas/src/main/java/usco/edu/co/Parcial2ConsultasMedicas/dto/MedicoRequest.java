package usco.edu.co.Parcial2ConsultasMedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 60, message = "El apellido no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El apellido solo debe contener letras y espacios")
    private String apellido;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 60, message = "La especialidad no debe superar 60 caracteres")
    @Pattern(regexp = "^(Medicina general|Pediatria|Dermatologia|Cardiologia|Odontologia|Ginecologia|Ortopedia)$", message = "Seleccione una especialidad valida")
    private String especialidad;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 30, message = "El usuario no debe superar 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El usuario solo debe contener letras y numeros")
    private String username;

    @Size(max = 60, message = "La contrasena no debe superar 60 caracteres")
    private String password;


}
