package mx.unam.fanaticosfc.controller.venta;

import mx.unam.fanaticosfc.model.Venta;
import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.service.estatusVenta.EstatusVentaServiceImpl;
import mx.unam.fanaticosfc.service.venta.VentaServiceImpl;
import mx.unam.fanaticosfc.util.RenderPagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String salvarVenta(@ModelAttribute Venta venta, @ModelAttribute VentaCredito ventaCredito, RedirectAttributes flash){
        try{
            ventaService.guardar(venta);
            flash.addFlashAttribute("success", "Venta registrada correctamente.");
            return "redirect:/venta/lista-venta";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la venta.");
            return "redirect:/venta/alta-venta";
        }
    }


}
