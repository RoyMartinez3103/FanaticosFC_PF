package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;
    @Column(name = "monto_total")
    private BigDecimal montoTotal;
    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(name = "es_venta_credito")
    private Boolean ventaCredito=false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estatus_venta")
    private EstatusVenta estatusVenta;

    public Venta() {
    }

    public Venta(BigDecimal montoTotal, LocalDateTime fechaVenta, Boolean ventaCredito, Usuario usuario, EstatusVenta estatusVenta) {
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
