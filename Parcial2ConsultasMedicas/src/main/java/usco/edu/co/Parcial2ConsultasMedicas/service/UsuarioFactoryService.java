package usco.edu.co.Parcial2ConsultasMedicas.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usco.edu.co.Parcial2ConsultasMedicas.model.Rol;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;
import usco.edu.co.Parcial2ConsultasMedicas.repository.RolRepository;
import usco.edu.co.Parcial2ConsultasMedicas.repository.UsuarioRepository;

@Service
public class UsuarioFactoryService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioFactoryService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario crearUsuario(String username, String password, String rolNombre) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Rol rol = rolRepository.findByName(rolNombre)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + rolNombre));

        Usuario usuario = new Usuario();
        usuario.setUsername(username.trim());
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEnabled(true);
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }
}

