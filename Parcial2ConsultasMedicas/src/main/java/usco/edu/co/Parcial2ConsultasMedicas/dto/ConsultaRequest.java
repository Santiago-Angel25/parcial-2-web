package usco.edu.co.Parcial2ConsultasMedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ConsultaRequest {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 40, message = "El nombre no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre debe ser alfanumerico")
    private String nombrePaciente;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no debe superar 100 caracteres")
    private String motivoConsulta;

    @NotNull(message = "El consultorio es obligatorio")
    private Integer numeroConsultorio;

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
    private String horaInicio;

    @NotBlank(message = "La hora de finalizacion es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de finalizacion debe tener formato HH:mm")
    private String horaFin;

    @NotNull(message = "Seleccione un medico")
    private Long medicoId;

    @NotNull(message = "Seleccione un paciente")
    private Long pacienteId;

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public Integer getNumeroConsultorio() {
        return numeroConsultorio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }
}

