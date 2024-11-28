package mx.unam.fanaticosfc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mx.unam.fanaticosfc.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Data
public class VentaDTO {
    //Tabla Venta
    private BigDecimal montoTotal;
    private LocalDateTime fechaVenta;
    private Boolean ventaCredito=false;
    private Usuario usuario;
    private EstatusVenta estatusVenta;

    //Tabla Detalles
    private Integer cantidadPlayeras;
    private Venta venta;
    private Playera playera;

    //Lista Credito
    private BigDecimal montoRestante;
    private Integer pagosRealizados;
    private Deudor deudor;
    //private Venta venta;








}
