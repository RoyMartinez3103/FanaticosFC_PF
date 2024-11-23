package mx.unam.fanaticosfc.controller.pagosCredito;

import mx.unam.fanaticosfc.service.pagosCredito.PagosCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pagos")
public class PagosCreditoController {

    @Autowired
    PagosCreditoServiceImpl pagosService;

    @GetMapping
    public String listaPagos(Model model){
        model.addAttribute("pago",pagosService.listarTodos());
        return "/pagos/lista-pagos";
    }
}
