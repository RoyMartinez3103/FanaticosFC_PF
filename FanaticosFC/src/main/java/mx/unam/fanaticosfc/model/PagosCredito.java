package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pago_credito")
public class PagosCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Digits(integer = 4,fraction = 2,message = "Valor incorrecto, se esparaba [4].[2] dígitos.")
    @DecimalMin(value = "0.0",inclusive = false, message = "El monto debe ser mayor a cero")
    @Column(name = "monto_pago")
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "id_venta_credito")
    private VentaCredito ventaCredito;

    public PagosCredito() {
    }

    public PagosCredito(LocalDateTime fechaPago, BigDecimal monto, VentaCredito ventaCredito) {
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.ventaCredito = ventaCredito;
    }

}
