package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "venta_credito")
public class VentaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta_credito")
    private Integer idVentaCredito;

    @DecimalMin(value = "0.0",inclusive = false, message = "El monto debe ser menor a cero")
    @Column(name = "monto_restante")
    private BigDecimal montoRestante;

    @Column(name = "pagos_realizados")
    private Integer pagosRealizados;

    @OneToOne
    @JoinColumn(name="id_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_deudor")
    private Deudor deudor;

    public VentaCredito() {
    }

    public VentaCredito(BigDecimal montoRestante, Integer pagosRealizados, Venta venta, Deudor deudor) {
        this.montoRestante = montoRestante;
        this.pagosRealizados = pagosRealizados;
        this.venta = venta;
        this.deudor = deudor;
    }
}
