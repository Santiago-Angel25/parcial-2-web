package usco.edu.co.Parcial2ConsultasMedicas.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usco.edu.co.Parcial2ConsultasMedicas.dto.PacienteRegistroRequest;
import usco.edu.co.Parcial2ConsultasMedicas.model.Paciente;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;
import usco.edu.co.Parcial2ConsultasMedicas.repository.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioFactoryService usuarioFactoryService;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioFactoryService usuarioFactoryService) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioFactoryService = usuarioFactoryService;
    }

    public Paciente buscarPorDocumento(String documento) {
        return pacienteRepository.findByDocumento(documento)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));
    }

    @Transactional
    public Paciente registrar(PacienteRegistroRequest request) {
        if (pacienteRepository.existsByDocumento(request.getDocumento())) {
            throw new IllegalArgumentException("Ya existe un paciente con ese documento");
        }

        Usuario usuario = usuarioFactoryService.crearUsuario(request.getUsername(), request.getPassword(), "PACIENTE");
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre().trim());
        paciente.setApellido(request.getApellido().trim());
        paciente.setDocumento(request.getDocumento().trim());
        paciente.setUsuario(usuario);
        return pacienteRepository.save(paciente);
    }
}
