package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Entity
@Data
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer idMarca;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo se permiten letras, incluyendo acentos y ñ")
    private String nombre;

    public Marca() {
    }


    public Marca(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Marca{" +
                "id_marca=" + idMarca +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
