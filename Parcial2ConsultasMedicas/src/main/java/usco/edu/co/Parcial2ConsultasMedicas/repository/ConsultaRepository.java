package usco.edu.co.Parcial2ConsultasMedicas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.model.Consulta;
import usco.edu.co.Parcial2ConsultasMedicas.model.Medico;
import usco.edu.co.Parcial2ConsultasMedicas.model.Paciente;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedico(Medico medico);
    List<Consulta> findByPaciente(Paciente paciente);
}

