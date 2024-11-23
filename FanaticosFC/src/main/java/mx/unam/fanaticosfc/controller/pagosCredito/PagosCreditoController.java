package mx.unam.fanaticosfc.controller.pagosCredito;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.PagosCredito;
import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.service.pagosCredito.PagosCreditoServiceImpl;
import mx.unam.fanaticosfc.service.ventaCredito.VentaCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
public class PagosCreditoController {

    @Autowired
    PagosCreditoServiceImpl pagosService;
    @Autowired
    VentaCreditoServiceImpl creditoService;

    @GetMapping("/lista-pagos")
    public String listaPagos(Model model){
        model.addAttribute("pago",pagosService.listarTodos());
        return "/pagos/lista-pagos";
    }

    @GetMapping("/form-pago/{id}")
    public String formPago(@Valid @PathVariable Integer id, Model model){
        VentaCredito ventaCredito = creditoService.buscarPorId(id);

        PagosCredito pago = new PagosCredito();
        pago.setVentaCredito(ventaCredito);

        model.addAttribute("contenido","Registrar un pago");
        model.addAttribute("pago",pago);
        model.addAttribute("ventaCredito",ventaCredito);
        return "/pagos/form-pago";
    }

    @PostMapping("/salvar-pago")
    public String salvarPago(@Valid @ModelAttribute("pago") PagosCredito pago,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash){

        if(result.hasErrors()){
            model.addAttribute("contenido","ERROR");
            return "/pagos/form-pago";
        }

        VentaCredito ventaCredito = pago.getVentaCredito();
        if (ventaCredito == null || pago.getMonto().compareTo(ventaCredito.getMontoRestante()) > 0) {
            result.rejectValue("monto", "error.pago", "El monto no puede ser mayor al monto restante de la venta");
            model.addAttribute("contenido", "Registrar un pago");
            return "/pagos/form-pago";
        }

        pagosService.guardar(pago);
        flash.addFlashAttribute("success", "El pago se guardó correctamente");

        return "redirect:/pagos/lista-pagos";

    }
}
