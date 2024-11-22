package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;


@NamedNativeQuery(
        name = "Playera.findPlayeraCostoMayorPromedio",
        query = "SELECT * FROM playera " +
                "WHERE precio_venta > (SELECT AVG(precio_venta) FROM PLAYERA) " +
                "ORDER BY precio_venta",
        resultClass = Playera.class
)
@NamedQuery(
        name="Playera.contarPlayeraColor",
        query = "SELECT COUNT(p) from Playera p WHERE p.color = :color"
)
@NamedQuery(
        name = "Playera.findByColor",
        query = "SELECT p FROM Playera p WHERE p.color = :color"
)
@NamedQuery(
        name = "Playera.findRangoPrecios",
        query = "SELECT p FROM Playera p WHERE p.precioVenta BETWEEN :precioMin AND :precioMax " +
                "ORDER BY p.precioVenta"
)

@Entity
@Data
public class Playera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playera")
    private Integer idPlayera;

    private String color;
    @Pattern(regexp = "CH|M|G|XG", message = "La talla debe ser CH, M, G o XG")
    private String talla;

    @Column(name = "tipo_manga")
    private String tipoManga;

    @Digits(integer = 4,fraction = 2,message = "Valor incorrecto, se esparaba [4].[2] dígitos.")
    @DecimalMin(value = "0.0",inclusive = false)
    @Column(name = "precio_real")
    private BigDecimal precioReal;

    private Integer stock;

    @Digits(integer = 4,fraction = 2,message = "Valor incorrecto, se esparaba [4].[2] dígitos.")
    @DecimalMin(value = "0.0",inclusive = false)
    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;

    public Playera() {
    }

    public Playera(String color, String talla, String tipoManga, BigDecimal precioReal, Integer stock, BigDecimal precioVenta, Marca marca, Equipo equipo) {
        this.color = color;
        this.talla = talla;
        this.tipoManga = tipoManga;
        this.precioReal = precioReal;
        this.stock = stock;
        this.precioVenta = precioVenta;
        this.marca = marca;
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Playera{" +
                "idPlayera=" + idPlayera +
                ", color='" + color + '\'' +
                ", talla='" + talla + '\'' +
                ", tipoManga='" + tipoManga + '\'' +
                ", precioReal=" + precioReal +
                ", stock=" + stock +
                ", precioVenta=" + precioVenta +
                ", marca=" + marca +
                ", equipo=" + equipo +
                '}';
    }

    public String mostrarEnTicket(){
        StringBuilder stringTicket = new StringBuilder();

        stringTicket.append(equipo.getNombre());
        stringTicket.append(" - ");
        stringTicket.append(marca.getNombre());
        stringTicket.append(" / ");
        stringTicket.append(color);
        stringTicket.append(" / ");
        stringTicket.append(talla);

        return stringTicket.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playera playera)) return false;
        return Objects.equals(idPlayera, playera.idPlayera) && Objects.equals(color, playera.color) && Objects.equals(talla, playera.talla) && Objects.equals(tipoManga, playera.tipoManga) && Objects.equals(precioReal, playera.precioReal) && Objects.equals(stock, playera.stock) && Objects.equals(precioVenta, playera.precioVenta) && Objects.equals(marca, playera.marca) && Objects.equals(equipo, playera.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlayera, color, talla, tipoManga, precioReal, stock, precioVenta, marca, equipo);
    }
}
