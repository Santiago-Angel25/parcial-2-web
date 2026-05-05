package usco.edu.co.Parcial2ConsultasMedicas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.model.Medico;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuario(Usuario usuario);
}

