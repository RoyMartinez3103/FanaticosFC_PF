package mx.unam.fanaticosfc.controller.venta;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mx.unam.fanaticosfc.dto.VentaCreditoDTO;
import mx.unam.fanaticosfc.dto.VentaDTO;
import mx.unam.fanaticosfc.model.*;
import mx.unam.fanaticosfc.service.detalleVenta.DetalleVentaServiceImpl;
import mx.unam.fanaticosfc.service.deudor.DeudorServiceImpl;
import mx.unam.fanaticosfc.service.estatusVenta.EstatusVentaServiceImpl;
import mx.unam.fanaticosfc.service.playera.PlayeraServiceImpl;
import mx.unam.fanaticosfc.service.venta.VentaServiceImpl;
import mx.unam.fanaticosfc.service.ventaCredito.VentaCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/venta")
public class VentaController {
    @Autowired
    VentaCreditoServiceImpl ventaCrService;
    @Autowired
    VentaServiceImpl ventaService;
    @Autowired
    EstatusVentaServiceImpl estatusVentaService;
    @Autowired
    PlayeraServiceImpl playeraService;
    @Autowired
    DetalleVentaServiceImpl detalleService;
    @Autowired
    DeudorServiceImpl deudorService;

    //Lista paginada de las ventas
    @GetMapping("/lista-venta")
    public String listaVenta(Model model){
        model.addAttribute("venta",ventaService.listarTodos());
        return "/venta/lista-venta"; //se retorna el html
    }

    //Se despliega la vista de las playeras a seleccionar
    @GetMapping("/seleccionar-tipo")
    public String mostrarSeleccionTipoVenta(HttpSession session, Model model) {
        session.removeAttribute("selectedJerseys");

        List<Playera> playeras = playeraService.listarTodos();
        Set<String> uniqueJerseys = new HashSet<>();
        List<Playera> filteredJerseys = playeras.stream()
                .filter(playera -> uniqueJerseys.add(playera.getColor() + "-" + playera.getEquipo().getIdEquipo() + "-" + playera.getTipoManga()))
                .toList();
        model.addAttribute("jerseys",filteredJerseys);
        return "/venta/seleccion-playeras";
    }

    //Con las playeras seleccionadas, se dirige a la venta seleccionada
    @PostMapping("/seleccionar-tipo")
    public String tipoVenta(@Valid @RequestParam("selectedJerseys") List<Playera> selectedJerseys,
                            @RequestParam("tipo") String tipo,
                            HttpSession session,
                            Model model) {

        if (selectedJerseys == null || selectedJerseys.isEmpty()) {
            model.addAttribute("error", "Debe seleccionar al menos una playera");
            return "/venta/seleccion-playeras";
        }
        session.setAttribute("selectedJerseys", selectedJerseys);

        if ("contado".equals(tipo)) {
            return "redirect:/venta/alta-venta-contado";
        } else {
            return "redirect:/venta/alta-venta-credito";
        }

    }

    //Despliega el formulario de venta contado
    @GetMapping("/alta-venta-contado")
    public String altaVentaContado(HttpSession session,Model model){

        List<Playera> selectedJerseys = (List<Playera>) session.getAttribute("selectedJerseys");
        System.out.println(selectedJerseys);
        VentaDTO ventadto = new VentaDTO();
        List<DetalleVenta> detalleVentas = selectedJerseys.stream()
                .map(playera -> {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setPlayera(playera);
                    detalle.setCantidadPlayeras(1);
                    return detalle;
                }).toList();
        ventadto.setDetalles(detalleVentas);

        Map<Integer, Map<String, Integer>> tallasDisponibles = new HashMap<>();
        for (Playera playera : selectedJerseys) {
            List<Map<String, Object>> resultados = playeraService.obtenerTallas(playera.getEquipo().getIdEquipo());
            Map<String,Integer> mapaTallasIds = resultados.stream()
                    .collect(Collectors.toMap(
                            resultado -> (String) resultado.get("talla"),  // La talla como clave
                            resultado -> (Integer) resultado.get("idPlayera") // El ID de la playera como valor
                    ));
            tallasDisponibles.put(playera.getIdPlayera(), mapaTallasIds);
        }

        model.addAttribute("ventaDTO",ventadto);
        model.addAttribute("selectedJerseys",selectedJerseys);
        model.addAttribute("tallasDisponibles",tallasDisponibles);

        return "/venta/registrar-venta-contado";
    }

    @GetMapping("/alta-venta-credito")
    public String altaVentaCredito(HttpSession session,Model model){


        List<Playera> selectedJerseys = (List<Playera>) session.getAttribute("selectedJerseys");
        System.out.println(selectedJerseys);
        VentaCreditoDTO ventaCreditoDTO = new VentaCreditoDTO();
        List<Deudor> deudores = deudorService.listarTodos();

        List<DetalleVenta> detalleVentas = selectedJerseys.stream()
                .map(playera -> {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setPlayera(playera);
                    detalle.setCantidadPlayeras(1);
                    return detalle;
                }).toList();
        ventaCreditoDTO.setDetalles(detalleVentas);

        Map<Integer, Map<String, Integer>> tallasDisponibles = new HashMap<>();
        for (Playera playera : selectedJerseys) {
            List<Map<String, Object>> resultados = playeraService.obtenerTallas(playera.getEquipo().getIdEquipo());
            Map<String,Integer> mapaTallasIds = resultados.stream()
                    .collect(Collectors.toMap(
                            resultado -> (String) resultado.get("talla"),  // La talla como clave
                            resultado -> (Integer) resultado.get("idPlayera") // El ID de la playera como valor
                    ));
            tallasDisponibles.put(playera.getIdPlayera(), mapaTallasIds);
        }

        model.addAttribute("deudor",deudores);
        model.addAttribute("ventaDTO",ventaCreditoDTO);
        model.addAttribute("selectedJerseys",selectedJerseys);
        model.addAttribute("tallasDisponibles",tallasDisponibles);

        return "/venta/registrar-venta-credito";
    }


    @PostMapping("/cancelar-venta/{id}")
    public String cancelarVenta(@PathVariable Integer id, RedirectAttributes flash){
        Venta venta = ventaService.buscarPorId(id);
        venta.setEstatusVenta(estatusVentaService.buscarPorId(3));

        ventaService.guardar(venta);

        flash.addFlashAttribute("success","La venta se canceló correctamente.");

        return "redirect:/venta/lista-venta";
    }

    @PostMapping("/salvar-ventadto")
    public String salvarVentadto(@ModelAttribute VentaDTO ventaDTO, RedirectAttributes flash){
        Venta venta = new Venta();
        BigDecimal montoTotal = BigDecimal.ZERO;

        venta.setFechaVenta(LocalDateTime.now());
        System.out.println("MontoTotal get: "+venta.getMontoTotal());
        try{
            for (DetalleVenta detalleVenta: ventaDTO.getDetalles()){
                BigDecimal subTotal = detalleVenta.getPlayera().getPrecioVenta().multiply(BigDecimal.valueOf(detalleVenta.getCantidadPlayeras()));
                montoTotal=montoTotal.add(subTotal);

            }
            venta.setMontoTotal(montoTotal);
            ventaDTO.setMontoTotal(montoTotal);
            System.out.println("MontoTotal for: "+montoTotal);
            ventaService.guardar(venta);
            flash.addFlashAttribute("success", "Venta registrada correctamente.");

            for (DetalleVenta detalleDTO : ventaDTO.getDetalles()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(venta);
                detalleVenta.setPlayera(detalleDTO.getPlayera());
                detalleVenta.setCantidadPlayeras(detalleDTO.getCantidadPlayeras());
                detalleService.guardar(detalleVenta);

                Playera playeraVendida = playeraService.buscarPorId(detalleVenta.getPlayera().getIdPlayera());
                playeraVendida.setStock(playeraVendida.getStock()-detalleVenta.getCantidadPlayeras());
                playeraService.guardar(playeraVendida);
            }
            return "redirect:/venta/lista-venta";

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la venta.");
            return "redirect:/venta/alta-venta-contado";
        }

    }

    @PostMapping("/salvar-ventaCredito")
    public String salvarVentaDTOCredito(@ModelAttribute VentaCreditoDTO ventaDTO, RedirectAttributes flash){
        Venta venta = new Venta();
        VentaCredito ventaCredito = new VentaCredito();
        Deudor deudor = deudorService.buscarPorId(ventaDTO.getDeudor().getIdDeudor());
        System.out.println(deudor);

        BigDecimal montoTotal = BigDecimal.ZERO;

        venta.setFechaVenta(LocalDateTime.now());
        try{
            for (DetalleVenta detalleVenta: ventaDTO.getDetalles()){
                BigDecimal subTotal = detalleVenta.getPlayera().getPrecioVenta().multiply(BigDecimal.valueOf(detalleVenta.getCantidadPlayeras()));
                montoTotal=montoTotal.add(subTotal);
            }

            venta.setMontoTotal(montoTotal);
            venta.setVentaCredito(true);
            System.out.println("MontoTotal for: "+montoTotal);
            ventaService.guardar(venta);

            ventaCredito.setMontoRestante(montoTotal);
            ventaCredito.setDeudor(deudor);
            ventaCredito.setPagosRealizados(0);
            ventaCredito.setVenta(venta);

            ventaCrService.guardar(ventaCredito);


            flash.addFlashAttribute("success", "Venta registrada correctamente.");

            for (DetalleVenta detalleDTO : ventaDTO.getDetalles()){
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(venta);
                detalleVenta.setPlayera(detalleDTO.getPlayera());
                detalleVenta.setCantidadPlayeras(detalleDTO.getCantidadPlayeras());
                detalleService.guardar(detalleVenta);

                Playera playeraVendida = playeraService.buscarPorId(detalleVenta.getPlayera().getIdPlayera());
                playeraVendida.setStock(playeraVendida.getStock()-detalleVenta.getCantidadPlayeras());
                playeraService.guardar(playeraVendida);
            }
            return "redirect:/venta/lista-venta";

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la venta.");
            return "redirect:/venta/alta-venta-credito";
        }

    }

}
