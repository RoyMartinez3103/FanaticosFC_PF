package mx.unam.fanaticosfc.controller.ventaCredito;

import mx.unam.fanaticosfc.model.Deudor;
import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.service.deudor.DeudorServiceImpl;
import mx.unam.fanaticosfc.service.ventaCredito.VentaCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ventaCredito")
public class VentaCreditoController {
    @Autowired
    VentaCreditoServiceImpl creditoService;
    @Autowired
    DeudorServiceImpl deudorService;

    @GetMapping("/lista-creditos")
    public String listaCreditos(Model model){
        model.addAttribute("credito",creditoService.listarTodos());
        return "/credito/lista-creditos";
    }

    @GetMapping("/alta-credito")
    public String altaCredito(Model model){
        VentaCredito ventaCredito = new VentaCredito();
        List<Deudor> deudores = deudorService.listarTodos();
        System.out.println("Lista deu:" + deudores);

        model.addAttribute("deudor",deudores);
        model.addAttribute("credito",ventaCredito);
        model.addAttribute("contenido","Registrar Venta a Crédito");
        model.addAttribute("subtitulo","Formulario para registrar una venta a crédito.");

        return "/credito/alta-credito";
    }
}
