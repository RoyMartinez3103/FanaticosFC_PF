package mx.unam.fanaticosfc.controller.graficas;

import mx.unam.fanaticosfc.repository.DetalleVentaRepository;
import mx.unam.fanaticosfc.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class ChartController {
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaRepository detalleRepository;

    @GetMapping
    public String getVentasPorUsuario(@RequestParam(defaultValue = "2024") Integer anio,Model model){
        List<Object[]> usuarioVentas = ventaRepository.getUsuariosMasVentas();

        List<String> nombres = new ArrayList<>();
        List<Long> ventas = new ArrayList<>();

        for (Object[] usuarioVenta : usuarioVentas){
            nombres.add((String) usuarioVenta[0]);
            ventas.add((Long) usuarioVenta[1]);
        }

        model.addAttribute("contenido","Gráficas");
        model.addAttribute("nombres",nombres);
        model.addAttribute("montos",ventas);

        // GRAFICA DE AREA //

        //anio= LocalDateTime.now().getYear();
        List<Object[]> gananciasMensuales = ventaRepository.getGananciasMes(anio);

        List<String> meses = new ArrayList<>();
        List<BigDecimal> ganancias = new ArrayList<>();

        for (Object[] dato : gananciasMensuales) {
            Integer mes = (Integer) dato[0];
            BigDecimal total = (BigDecimal) dato[1];

            meses.add(getNombreMes(mes));
            ganancias.add(total);
        }

        model.addAttribute("meses",meses);
        model.addAttribute("ganancias",ganancias);

        // GRAFICA DE DONA //

        // TARJETAS //
        BigDecimal gananciaMes = ventaRepository.gananciaMes();
        model.addAttribute("gananciaMes",gananciaMes);

        Integer ventasMes = ventaRepository.ventasMes();
        model.addAttribute("ventasMes",ventasMes);

        List<Object[]> playeraMasVendida = detalleRepository.getPlayeraMasVendida();

        Object[] fila = playeraMasVendida.get(0);
            String equipo = (String) fila[0];
            String color = (String) fila[1];
            BigDecimal cantidad = (BigDecimal) fila[2];

        model.addAttribute("equipo",equipo);
        model.addAttribute("color",color);
        model.addAttribute("cantidad",cantidad);

        List<Object[]> empleadoMes = ventaRepository.getEmpleadoDelMes();
        Object[] empleado = empleadoMes.get(0);
        String nombre = (String) empleado[0];
        String paterno = (String) empleado[1];
        Long ventasReal = (Long) empleado[2];

        System.out.println(nombre);
        System.out.println(paterno);
        System.out.println(ventasReal);

        model.addAttribute("nombre",nombre);
        model.addAttribute("paterno",paterno);
        model.addAttribute("ventasReal",ventasReal);



        return "/graficas/charts";
    }

    private String getNombreMes(int mes) {
        return switch (mes) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "Desconocido";
        };
    }
}
