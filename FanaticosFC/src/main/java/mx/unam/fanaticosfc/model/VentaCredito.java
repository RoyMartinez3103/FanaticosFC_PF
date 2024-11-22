package mx.unam.fanaticosfc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "venta_credito")
public class VentaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_venta_credito;
    private Integer plazo;

    @OneToOne
    @JoinColumn(name="id_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_deudor")
    private Deudor deudor;

    public VentaCredito() {
    }

    public VentaCredito(Integer plazo, Venta venta, Deudor deudor) {
        this.plazo = plazo;
        this.venta = venta;
        this.deudor = deudor;
    }

    @Override
    public String toString() {
        return "VentaCredito{" +
                "id_venta_credito=" + id_venta_credito +
                ", plazo=" + plazo +
                ", venta=" + venta +
                ", deudor=" + deudor +
                '}';
    }
}
