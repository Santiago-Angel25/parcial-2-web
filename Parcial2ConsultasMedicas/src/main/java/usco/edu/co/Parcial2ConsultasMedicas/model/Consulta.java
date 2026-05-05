package usco.edu.co.Parcial2ConsultasMedicas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombrePaciente;

    private String motivoConsulta;

    @ManyToOne
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    public Integer getNumeroConsultorio() {
        return consultorio == null ? null : consultorio.getNumero();
    }
}
