package mx.unam.fanaticosfc.controller.graficas;

import mx.unam.fanaticosfc.repository.DetalleVentaRepository;
import mx.unam.fanaticosfc.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
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

        model.addAttribute("contenido","Estadísticas");
        model.addAttribute("subtitulo","Se muestran estadísticas mensuales e históricas");
        model.addAttribute("nombres",nombres);
        model.addAttribute("montos",ventas);

        // GRAFICA DE AREA //
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

        List<Object[]> tallasVendidas = detalleRepository.getTallasVendidas();
        List<String> tallas = new ArrayList<>();
        List<BigDecimal> tvendidas = new ArrayList<>();

        for(Object[] objs : tallasVendidas){
            String talla = (String) objs[0];
            BigDecimal cant = (BigDecimal) objs[1];

            tallas.add(talla);
            tvendidas.add(cant);
        }

        model.addAttribute("tallas",tallas);
        model.addAttribute("tvendidas",tvendidas);

        // GRAFICA DE DONA 2 //
        List<Object[]> tiposDeVentas = ventaRepository.getTiposVentas();
        List<String> tipos = new ArrayList<>();
        List<Long> vecesRealizadas = new ArrayList<>();

        for(Object[] objs : tiposDeVentas){
            String tipoV = (String) objs[0];
            Long veces = (Long) objs[1];

            tipos.add(tipoV);
            vecesRealizadas.add(veces);
        }

        model.addAttribute("tiposVentas",tipos);
        model.addAttribute("veces",vecesRealizadas);

        // TARJETAS //
        BigDecimal gananciaMes = ventaRepository.gananciaMes();
        model.addAttribute("gananciaMes",gananciaMes);

        Integer ventasMes = ventaRepository.ventasMes();
        model.addAttribute("ventasMes",ventasMes);

        List<Object[]> playeraMasVendida = detalleRepository.getPlayeraMasVendida();
        String equipo;
        String color;
        BigDecimal cantidad;

        if(!playeraMasVendida.isEmpty()) {
            Object[] fila = playeraMasVendida.get(0);
            equipo = (String) fila[0];
            color = (String) fila[1];
            cantidad = (BigDecimal) fila[2];
        }else {
            equipo = "Sin";
            color = "Playeras";
            cantidad = BigDecimal.valueOf(0);
        }

        model.addAttribute("equipo",equipo);
        model.addAttribute("color",color);
        model.addAttribute("cantidad",cantidad);

        List<Object[]> empleadoMes = ventaRepository.getEmpleadoDelMes();
        String nombre,paterno;
        Long ventasReal;

        if(!empleadoMes.isEmpty()) {
            Object[] empleado = empleadoMes.get(0);
            nombre = (String) empleado[0];
            paterno = (String) empleado[1];
            ventasReal = (Long) empleado[2];
        }else {
            nombre = "Sin";
            paterno = "Nombre";
            ventasReal = 0L;
        }

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
