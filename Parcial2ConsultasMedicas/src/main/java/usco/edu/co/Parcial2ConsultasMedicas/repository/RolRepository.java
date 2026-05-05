package usco.edu.co.Parcial2ConsultasMedicas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByName(String name);
}

