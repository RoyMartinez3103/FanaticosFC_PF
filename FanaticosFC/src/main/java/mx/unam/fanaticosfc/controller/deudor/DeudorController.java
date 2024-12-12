package mx.unam.fanaticosfc.controller.deudor;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.controller.equipo.EquipoController;
import mx.unam.fanaticosfc.model.Deudor;
import mx.unam.fanaticosfc.service.deudor.DeudorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/deudor")
public class DeudorController {

    private static final Logger logger = LoggerFactory.getLogger(DeudorController.class);

    @Autowired
    DeudorServiceImpl deudorService;

    @GetMapping("/lista-deudor")
    public String listaDeudor(Model model){
        model.addAttribute("deudor",deudorService.listarTodos());
        return "/deudor/lista-deudor";
    }

    @GetMapping("/alta-deudor")
    public String altaDeudor(Model model){
        Deudor deudor = new Deudor();
        model.addAttribute("contenido","Agregar nuevo deudor");
        model.addAttribute("deudor",deudor);
        return "/deudor/alta-deudor";
    }

    @PostMapping("/salvar-deudor")
    public String salvarDeudor(@Valid @ModelAttribute("deudor") Deudor deudor,
                              BindingResult result,
                              Model model,
                              RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("contenido", "ERROR");
            return "/deudor/alta-deudor";
        }

        deudorService.guardar(deudor);
        flash.addFlashAttribute("success", "El deudor se guardó correctamente");
        logger.info("Se guardó un nuevo Deudor con ID: " + deudor.getIdDeudor());

        return "redirect:/deudor/lista-deudor";
    }

    @GetMapping("/eliminar-deudor/{id}")
    public String eliminarDeudor(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            deudorService.borrar(id);
            flash.addFlashAttribute("success", "El deudor se borró correctamente.");
            logger.info("Se eliminó el Deudor con ID: " + id);
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar el deudor porque tiene pagos pendientes.");
            logger.error("Error al eliminar el Deudor con ID: {} por pagos pendientes",id);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el deudor.");
        }
        return "redirect:/deudor/lista-deudor";
    }

    @GetMapping("/editar-deudor/{id}")
    public String modificarDeudor(@PathVariable Integer id, Model model){
        Deudor deudor = deudorService.buscarPorId(id);
        model.addAttribute("deudor",deudor);
        model.addAttribute("contenido","Modificar Deudor");
        model.addAttribute("subtitulo","Formulario para modificar una deudor existente.");
        logger.info("Se editó el Deudor con ID: " + id);
        return "/deudor/alta-deudor";
    }
}
