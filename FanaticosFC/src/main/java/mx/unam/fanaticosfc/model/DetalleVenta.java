package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Detalle_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Integer idDetalleVenta;
    @Column(name ="cantidad_playeras")
    private Integer cantidadPlayeras;

    @ManyToOne
    @JoinColumn(name = "idVenta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "idPlayera")
    private Playera playera;

    public DetalleVenta() {
    }

    public DetalleVenta(Integer cantidadPlayeras, Venta venta, Playera playera) {
        this.cantidadPlayeras = cantidadPlayeras;
        this.venta = venta;
        this.playera = playera;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "id_detalle_venta=" + idDetalleVenta +
                ", cantidadPlayeras=" + cantidadPlayeras +
                ", venta=" + venta +
                ", playera=" + playera +
                '}';
    }
}
