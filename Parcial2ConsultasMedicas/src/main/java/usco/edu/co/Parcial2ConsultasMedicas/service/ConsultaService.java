package usco.edu.co.Parcial2ConsultasMedicas.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usco.edu.co.Parcial2ConsultasMedicas.dto.ConsultaRequest;
import usco.edu.co.Parcial2ConsultasMedicas.dto.HorarioRequest;
import usco.edu.co.Parcial2ConsultasMedicas.model.Consulta;
import usco.edu.co.Parcial2ConsultasMedicas.model.Medico;
import usco.edu.co.Parcial2ConsultasMedicas.model.Paciente;
import usco.edu.co.Parcial2ConsultasMedicas.model.Usuario;
import usco.edu.co.Parcial2ConsultasMedicas.repository.ConsultaRepository;
import usco.edu.co.Parcial2ConsultasMedicas.repository.MedicoRepository;
import usco.edu.co.Parcial2ConsultasMedicas.repository.PacienteRepository;
import usco.edu.co.Parcial2ConsultasMedicas.repository.UsuarioRepository;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> listarPorMedico(String username) {
        return consultaRepository.findByMedico(medicoActual(username));
    }

    public List<Consulta> listarPorPaciente(String username) {
        return consultaRepository.findByPaciente(pacienteActual(username));
    }

    public Consulta buscar(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada"));
    }

    @Transactional
    public Consulta crear(ConsultaRequest request) {
        Consulta consulta = new Consulta();
        asignarDatos(consulta, request);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta actualizar(Long id, ConsultaRequest request) {
        Consulta consulta = buscar(id);
        asignarDatos(consulta, request);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public void eliminar(Long id) {
        consultaRepository.delete(buscar(id));
    }

    @Transactional
    public Consulta actualizarHorarioComoPaciente(Long id, HorarioRequest request, String username) {
        Consulta consulta = buscar(id);
        Paciente paciente = pacienteActual(username);

        if (!consulta.getPaciente().getId().equals(paciente.getId())) {
            throw new AccessDeniedException("Solo puede actualizar citas asignadas a su usuario");
        }

        LocalTime horaInicio = LocalTime.parse(request.getHoraInicio());
        LocalTime horaFin = LocalTime.parse(request.getHoraFin());
        validarHorario(horaInicio, horaFin);
        validarCruces(id, consulta.getNumeroConsultorio(), consulta.getMedico(), horaInicio, horaFin);

        consulta.setHoraInicio(horaInicio);
        consulta.setHoraFin(horaFin);
        return consultaRepository.save(consulta);
    }

    private void asignarDatos(Consulta consulta, ConsultaRequest request) {
        LocalTime horaInicio = LocalTime.parse(request.getHoraInicio());
        LocalTime horaFin = LocalTime.parse(request.getHoraFin());
        validarHorario(horaInicio, horaFin);
        validarConsultorio(request.getNumeroConsultorio());

        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new EntityNotFoundException("Medico no encontrado"));
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        validarCruces(consulta.getId(), request.getNumeroConsultorio(), medico, horaInicio, horaFin);

        consulta.setNombrePaciente(request.getNombrePaciente().trim());
        consulta.setMotivoConsulta(request.getMotivoConsulta().trim());
        consulta.setNumeroConsultorio(request.getNumeroConsultorio());
        consulta.setHoraInicio(horaInicio);
        consulta.setHoraFin(horaFin);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
    }

    private void validarConsultorio(Integer consultorio) {
        if (consultorio == null || consultorio < 1 || consultorio > 20) {
            throw new IllegalArgumentException("El numero de consultorio debe estar entre 1 y 20");
        }
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("La hora de finalizacion debe ser mayor a la hora de inicio");
        }
    }

    private void validarCruces(Long consultaId, Integer consultorio, Medico medico, LocalTime horaInicio, LocalTime horaFin) {
        for (Consulta existente : consultaRepository.findAll()) {
            if (consultaId != null && consultaId.equals(existente.getId())) {
                continue;
            }

            boolean seCruza = existente.getHoraInicio().isBefore(horaFin)
                    && horaInicio.isBefore(existente.getHoraFin());

            if (!seCruza) {
                continue;
            }

            if (existente.getNumeroConsultorio().equals(consultorio)) {
                throw new IllegalArgumentException("El consultorio ya tiene una consulta en ese horario");
            }

            if (existente.getMedico().getId().equals(medico.getId())) {
                throw new IllegalArgumentException("El medico ya tiene una consulta en ese horario");
            }
        }
    }

    private Medico medicoActual(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return medicoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Medico no encontrado para el usuario actual"));
    }

    private Paciente pacienteActual(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return pacienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado para el usuario actual"));
    }
}

