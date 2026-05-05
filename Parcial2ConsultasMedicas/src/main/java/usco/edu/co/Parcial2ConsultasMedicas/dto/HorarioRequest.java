package usco.edu.co.Parcial2ConsultasMedicas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Datos para actualizar solamente el horario de una cita")
public class HorarioRequest {

    @Schema(description = "Hora inicial en formato HH:mm", example = "10:00")
    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
    private String horaInicio;

    @Schema(description = "Hora final en formato HH:mm", example = "11:00")
    @NotBlank(message = "La hora de finalizacion es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de finalizacion debe tener formato HH:mm")
    private String horaFin;


}
