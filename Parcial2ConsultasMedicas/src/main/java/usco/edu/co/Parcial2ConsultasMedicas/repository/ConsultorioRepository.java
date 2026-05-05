package usco.edu.co.Parcial2ConsultasMedicas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.model.Consultorio;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    Optional<Consultorio> findByNumero(Integer numero);
}

