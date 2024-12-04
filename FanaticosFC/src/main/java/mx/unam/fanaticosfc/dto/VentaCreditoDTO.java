package mx.unam.fanaticosfc.dto;

import mx.unam.fanaticosfc.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaCreditoDTO {
    //Tabla Venta
    private BigDecimal montoTotal;
    private LocalDateTime fechaVenta;
    private Boolean ventaCredito=true;
    private Usuario usuario;
    private EstatusVenta estatusVenta;
    private List<DetalleVenta> detalles;

    //Lista Credito
    private BigDecimal montoRestante;
    private Integer pagosRealizados;
    private Deudor deudor;
    private Venta venta;
}
