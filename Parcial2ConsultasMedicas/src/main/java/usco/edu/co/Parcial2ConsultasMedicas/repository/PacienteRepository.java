package usco.edu.co.Parcial2ConsultasMedicas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.model.Paciente;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUsuario(Usuario usuario);
}

