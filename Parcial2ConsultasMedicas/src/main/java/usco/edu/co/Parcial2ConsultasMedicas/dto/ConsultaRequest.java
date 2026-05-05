package usco.edu.co.Parcial2ConsultasMedicas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para crear o actualizar una consulta medica")
public class ConsultaRequest {

    @Schema(description = "Nombre completo del paciente encontrado", example = "Juan Perez")
    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 40, message = "El nombre no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre debe ser alfanumerico")
    private String nombrePaciente;

    @Schema(description = "Motivo principal de la consulta", example = "Control general")
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no debe superar 100 caracteres")
    private String motivoConsulta;

    @Schema(description = "Numero de consultorio seleccionado", example = "1")
    @NotNull(message = "El consultorio es obligatorio")
    private Integer numeroConsultorio;

    @Schema(description = "Hora inicial en formato HH:mm", example = "07:00")
    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
    private String horaInicio;

    @Schema(description = "Hora final en formato HH:mm", example = "08:00")
    @NotBlank(message = "La hora de finalizacion es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de finalizacion debe tener formato HH:mm")
    private String horaFin;

    @Schema(description = "Identificador del medico asignado", example = "1")
    @NotNull(message = "Seleccione un medico")
    private Long medicoId;

    @Schema(description = "Identificador del paciente registrado", example = "1")
    @NotNull(message = "Seleccione un paciente")
    private Long pacienteId;


}
