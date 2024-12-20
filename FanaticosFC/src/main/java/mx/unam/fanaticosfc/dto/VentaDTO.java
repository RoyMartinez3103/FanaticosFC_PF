package mx.unam.fanaticosfc.dto;

import lombok.Data;
import mx.unam.fanaticosfc.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDTO {
    //Tabla Venta
    private BigDecimal montoTotal;
    private LocalDateTime fechaVenta;
    private Boolean ventaCredito=false;
    private Usuario usuario;
    private EstatusVenta estatusVenta;
    private List<DetalleVenta> detalles;

}
