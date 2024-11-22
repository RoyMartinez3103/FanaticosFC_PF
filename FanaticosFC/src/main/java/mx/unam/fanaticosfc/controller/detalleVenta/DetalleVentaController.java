package mx.unam.fanaticosfc.controller.detalleVenta;


import mx.unam.fanaticosfc.model.DetalleVenta;
import mx.unam.fanaticosfc.model.Venta;
import mx.unam.fanaticosfc.service.detalleVenta.DetalleVentaServiceImpl;
import mx.unam.fanaticosfc.service.venta.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    @Autowired
    DetalleVentaServiceImpl detalleVentaService;
    @Autowired
    VentaServiceImpl ventaService;


    @GetMapping("/ticket/{id}")
    public String mostrarVentas(@PathVariable Integer id,Model model) {

        // Obtener todas las ventas
        List<DetalleVenta> detallesVenta = detalleVentaService.listarTodos();
        Venta venta = ventaService.buscarPorId(id);

        // Obtener ventas únicas
        List<Venta> ventasUnicas = detallesVenta.stream()
                .map(DetalleVenta::getVenta)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("ventasUnicas", ventasUnicas);
        model.addAttribute("detallesVenta", detallesVenta);
        model.addAttribute("venta", venta);
        return "/ticket";
    }
}
