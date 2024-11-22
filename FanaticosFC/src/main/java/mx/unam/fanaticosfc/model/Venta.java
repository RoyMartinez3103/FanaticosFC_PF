package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;
    @Column(name = "monto_total")
    private Double montoTotal;
    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;
    @Column(name = "es_venta_credito")
    private Boolean ventaCredito;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estatus_venta")
    private EstatusVenta estatusVenta;

    public Venta() {
    }

    public Venta(Double montoTotal, LocalDate fechaVenta, Boolean ventaCredito, Usuario usuario, EstatusVenta estatusVenta) {
        this.montoTotal = montoTotal;
        this.fechaVenta = fechaVenta;
        this.ventaCredito = ventaCredito;
        this.usuario = usuario;
        this.estatusVenta = estatusVenta;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", montoTotal=" + montoTotal +
                ", fechaVenta=" + fechaVenta +
                ", ventaCredito=" + ventaCredito +
                ", usuario=" + usuario +
                ", estatusVenta=" + estatusVenta +
                '}';
    }
}
