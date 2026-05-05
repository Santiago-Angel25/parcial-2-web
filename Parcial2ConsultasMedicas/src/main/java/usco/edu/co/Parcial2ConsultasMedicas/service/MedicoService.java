package usco.edu.co.Parcial2ConsultasMedicas.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usco.edu.co.Parcial2ConsultasMedicas.dto.MedicoRequest;
import usco.edu.co.Parcial2ConsultasMedicas.model.Medico;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;
import usco.edu.co.Parcial2ConsultasMedicas.repository.MedicoRepository;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioFactoryService usuarioFactoryService;

    public MedicoService(MedicoRepository medicoRepository, UsuarioFactoryService usuarioFactoryService) {
        this.medicoRepository = medicoRepository;
        this.usuarioFactoryService = usuarioFactoryService;
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Medico buscar(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medico no encontrado"));
    }

    @Transactional
    public Medico crear(MedicoRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria para crear el medico");
        }

        Usuario usuario = usuarioFactoryService.crearUsuario(request.getUsername(), request.getPassword(), "MEDICO");
        Medico medico = new Medico();
        medico.setUsuario(usuario);
        asignarDatos(medico, request);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico actualizar(Long id, MedicoRequest request) {
        Medico medico = buscar(id);
        asignarDatos(medico, request);
        return medicoRepository.save(medico);
    }

    @Transactional
    public void eliminar(Long id) {
        medicoRepository.delete(buscar(id));
    }

    private void asignarDatos(Medico medico, MedicoRequest request) {
        medico.setNombre(request.getNombre().trim());
        medico.setApellido(request.getApellido().trim());
        medico.setEspecialidad(request.getEspecialidad().trim());
    }
}
