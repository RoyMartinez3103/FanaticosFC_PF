package mx.unam.fanaticosfc.controller.graficas;

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
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class ChartController {
    @Autowired
    VentaRepository ventaRepository;

    @GetMapping("/ventas-por-usuario")
    public String getVentasPorUsuario(@RequestParam(defaultValue = "2024") Integer anio,Model model){
        List<Object[]> usuarioVentas = ventaRepository.getUsuariosMasVentas();
        System.out.println("Cantidad de registros: " + usuarioVentas.size());

        List<String> nombres = new ArrayList<>();
        List<Long> ventas = new ArrayList<>();

        for (Object[] usuarioVenta : usuarioVentas){
            nombres.add((String) usuarioVenta[0]);
            ventas.add((Long) usuarioVenta[1]);
        }

        System.out.println("Nombres: " + nombres);
        System.out.println("Ventas: " + ventas);

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

        System.out.println("Meses: " + meses);
        System.out.println("Ventas: " + ganancias);

        model.addAttribute("meses",meses);
        model.addAttribute("ganancias",ganancias);

        // GRAFICA DE DONA //

        // TARJETAS //
        String gananciaMes = ventaRepository.gananciaMes().toString();
        model.addAttribute("gananciaMes",gananciaMes);




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
