package mx.unam.fanaticosfc.controller.venta;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.unam.fanaticosfc.model.*;
import mx.unam.fanaticosfc.service.deudor.DeudorServiceImpl;
import mx.unam.fanaticosfc.service.estatusVenta.EstatusVentaServiceImpl;
import mx.unam.fanaticosfc.service.venta.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/venta")
public class VentaController {
    @Autowired
    VentaServiceImpl ventaService;
    @Autowired
    EstatusVentaServiceImpl estatusVentaService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DeudorServiceImpl deudorService;



    @GetMapping("/lista-venta")
    public String listaVenta(Model model){
        model.addAttribute("venta",ventaService.listarTodos());
        return "/venta/lista-venta"; //se retorna el html
    }

    @PostMapping("/cancelar-venta/{id}")
    public String cancelarVenta(@PathVariable Integer id, RedirectAttributes flash){
        Venta venta = ventaService.buscarPorId(id);
        venta.setEstatusVenta(estatusVentaService.buscarPorId(3));

        ventaService.guardar(venta);

        flash.addFlashAttribute("success","La venta se canceló correctamente.");

        return "redirect:/venta/lista-venta";
    }

    @GetMapping("/alta-venta")
    public String altaVenta(Model model){
        Venta venta = new Venta();
        model.addAttribute("contenido","Registrar una Venta");
        model.addAttribute("venta",venta);

        return "/venta/registrar-venta";
    }

    @PostMapping("/salvar-venta")
    public String salvarVenta(@ModelAttribute Venta venta, RedirectAttributes flash){
        try{
            ventaService.guardar(venta);
            flash.addFlashAttribute("success", "Venta registrada correctamente.");
            return "redirect:/venta/lista-venta";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la venta.");
            return "redirect:/venta/alta-venta";
        }
    }

//    @Autowired
//    VentaDTOServiceImpl ventaDTOService;
//
//    @Autowired
//    PlayeraServiceImpl playeraService;
//
//    @GetMapping("/alta-venta")
//    public String altaVenta(Model model, HttpSession session) {
//        List<Integer> playerasId = (List<Integer>) session.getAttribute("selectedJerseyIds");
//
//        List<Playera> playerasel = (List<Playera>) playeraService.findAllById(playerasId);
//        return  "/venta/registrar-venta";
//    }
//
//    @PostMapping("/salvar-venta")
//    public String procesarVenta(@ModelAttribute VentaDTO ventaDTO,RedirectAttributes flash){
//        try {
//            ventaDTOService.procesarVenta(ventaDTO);
//            flash.addFlashAttribute("success", "Venta registrada correctamente.");
//            return "redirect: /venta/lista-venta";
//        }catch (Exception e){
//            flash.addFlashAttribute("error", "Error al guardar la venta.");
//            return "redirect:/venta/alta-venta";
//        }
//    }

//    @PostMapping("/salvar-venta")
//    public String salvarVenta(@RequestParam("montoTotal")BigDecimal monto, @RequestParam("jerseyData") String jerseyData){
//        ObjectMapper mapper = new ObjectMapper();
//        List<VentaDTO> ventaDTO;
//        try{
//            ventaDTO = mapper.readValue(jerseyData, new TypeReference<>() {
//            });
//        }catch (JsonProcessingException e){
//            e.printStackTrace();
//            throw new RuntimeException("Error al procesar los datos");
//        }
//
//        System.out.println("Monto total: " + monto);
//        ventaDTO.forEach(detalle -> {
//            System.out.println("Playera ID: " + detalle.getPlayerasId());
//            System.out.println("Cantidad: " + detalle.getCantidadPlayeras());
//            System.out.println("Precio Unitario: " + detalle.getMontoTotal());
//        });
//
//        return "redirect:/venta/lista-venta";
//    }

}
