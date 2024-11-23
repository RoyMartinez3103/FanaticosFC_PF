package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "pago_credito")
public class PagosCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPago")
    private Integer id_pago;
    @Column(name = "fecha_pago")
    private Date fechaPago;
    @Column(name = "monto_pago")
    private Double monto;
    @Column(name = "monto_restante")
    private double montoRestante;

    @ManyToOne
    @JoinColumn(name = "id_venta_credito")
    private VentaCredito ventaCredito;

    public PagosCredito() {
    }

    public PagosCredito(Date fechaPago, Double monto, double montoRestante, VentaCredito ventaCredito) {
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.montoRestante = montoRestante;
        this.ventaCredito = ventaCredito;
    }

    @Override
    public String toString() {
        return "PagosCredito{" +
                "id_pago=" + id_pago +
                ", fechaPago=" + fechaPago +
                ", monto=" + monto +
                ", montoRestante=" + montoRestante +
                ", ventaCredito=" + ventaCredito +
                '}';
    }
}
