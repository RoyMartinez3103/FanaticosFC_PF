package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@NamedQuery(
        name = "Usuario.searchByFechaNacimientoAlfabetico",
        query = "SELECT a FROM Usuario a " +
                "WHERE a.fechaNac  BETWEEN :fechaIni AND :fechaFin "+
                "ORDER BY a.nombre"
)
@NamedQuery(
        name = "Usuario.contarPorRfcContenga",
        query = "SELECT COUNT(a) FROM Usuario a WHERE a.rfc LIKE CONCAT('%', :substring, '%')"
)
@NamedQuery(
        name = "Usuario.buscarPorRfcContenga",
        query = "SELECT a FROM Usuario a WHERE a.rfc LIKE CONCAT('%', :substring, '%')"
)


@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    private String nombre;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    @Column(name = "apellido_pat")
    private String apellidoPat;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    @Column(name = "apellido_mat")
    private String apellidoMat;
    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    @Column(name = "fecha_nac")
    private LocalDate fechaNac;
    @Size(min = 13, max = 13, message = "El RFC debe tener exactamente 13 caracteres")
    private String rfc;
    @NotBlank(message = "El correo no puede estar vacío")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "El correo debe ser válido"
    )
    private String mail;
    private String username;
    @Size(min = 8, max = 13, message = "La contraseña debe tener entre 8 y 12 caracteres")
    @Column(name = "Contraseña")
    private String contrasena;
    @Column(name = "rol")
    private String rol;
    @Column(name = "ventas_realizadas")
    private Integer ventasRealizadas=0;

    public Usuario() {
    }

    public Usuario(String nombre, String apellidoPat, String apellidoMat, LocalDate fechaNac, String rfc, String mail, String username, String contrasena, String rol, Integer ventasRealizadas) {
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.fechaNac = fechaNac;
        this.rfc = rfc;
        this.mail = mail;
        this.username = username;
        this.contrasena = contrasena;
        this.rol = rol;
        this.ventasRealizadas = ventasRealizadas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellidoPat='" + apellidoPat + '\'' +
                ", apellidoMat='" + apellidoMat + '\'' +
                ", fechaNac=" + fechaNac +
                ", rfc='" + rfc + '\'' +
                ", mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", rol=" + rol +
                ", ventasRealizadas=" + ventasRealizadas +
                '}';
    }

    public boolean isAdmin() {
        return "ADMIN".equals(rol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUsuario);
    }
}

