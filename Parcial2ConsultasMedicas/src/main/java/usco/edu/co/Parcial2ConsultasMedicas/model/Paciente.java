package usco.edu.co.Parcial2ConsultasMedicas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    @Column(nullable = false, unique = true)
    private String documento;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    public String getNombreCompleto() {
        String nombres = nombre == null ? "" : nombre;
        String apellidos = apellido == null ? "" : apellido;
        return (nombres + " " + apellidos).trim();
    }

}
