package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Deudor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deudor")
    private Integer idDeudor;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    private String nombre;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    @Column(name = "apellido_pat")
    private String apellidoPat;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    @Column(name = "apellido_mat")
    private String apellidoMat;
    @Size(min = 10, max = 10, message = "El telefono es de 10 dígitos")
    private String telefono;
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "El correo debe ser válido"
    )
    private String mail;

    public Deudor() {
    }

    public Deudor(String nombre, String apellidoMat, String apellidoPat, String telefono, String mail) {
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.telefono = telefono;
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Deudor{" +
                "idDeudor=" + idDeudor +
                ", nombre='" + nombre + '\'' +
                ", apellidoPat='" + apellidoPat + '\'' +
                ", apellidoMat='" + apellidoMat + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }


}
