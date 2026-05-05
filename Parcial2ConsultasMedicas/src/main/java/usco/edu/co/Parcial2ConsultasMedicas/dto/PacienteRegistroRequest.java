package usco.edu.co.Parcial2ConsultasMedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PacienteRegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 60, message = "El apellido no debe superar 60 caracteres")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "El apellido solo debe contener letras y espacios")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no debe superar 20 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El documento solo debe contener numeros")
    private String documento;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 30, message = "El usuario no debe superar 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El usuario solo debe contener letras y numeros")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(max = 60, message = "La contrasena no debe superar 60 caracteres")
    private String password;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
