package mx.unam.fanaticosfc.controller.ventaCredito;

import mx.unam.fanaticosfc.service.ventaCredito.VentaCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ventaCredito")
public class VentaCreditoController {
    @Autowired
    VentaCreditoServiceImpl creditoService;

    @GetMapping("/lista-creditos")
    public String listaCreditos(Model model){
        model.addAttribute("credito",creditoService.listarTodos());
        return "/credito/lista-creditos";
    }
}
