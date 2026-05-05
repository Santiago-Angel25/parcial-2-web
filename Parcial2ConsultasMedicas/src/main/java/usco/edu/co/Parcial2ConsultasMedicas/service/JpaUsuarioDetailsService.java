package usco.edu.co.Parcial2ConsultasMedicas.service;

import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;
import usco.edu.co.Parcial2ConsultasMedicas.repository.UsuarioRepository;

@Service
public class JpaUsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JpaUsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isEnabled(),
                true,
                true,
                true,
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getName()))
                        .collect(Collectors.toSet())
        );
    }
}
