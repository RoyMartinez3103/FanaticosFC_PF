package mx.unam.fanaticosfc.controller.marca;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Marca;
import mx.unam.fanaticosfc.service.marca.MarcaServiceImpl;
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
@RequestMapping("/marca")
public class MarcaController {

    private static final Logger logger = LoggerFactory.getLogger(MarcaController.class);
    
    @Autowired
    MarcaServiceImpl marcaService;
    
    @GetMapping("/lista-marca")
    public String listaMarca(Model model){
        model.addAttribute("marca",marcaService.listarTodos());
        model.addAttribute("contenido","Lista de Marcas");
        model.addAttribute("subtitulo","Se muestran las marcas registradas en el sistema.");
        return "/marca/lista-marca"; //se retorna el html
    }

    @GetMapping("/alta-marca")
    public String altaMarca(Model model){
        Marca marca = new Marca();
        model.addAttribute("contenido","Agregar nueva marca");
        model.addAttribute("subtitulo","Ingresar los datos correspondientes de la nueva Marca.");
        model.addAttribute("marca",marca);
        return "/marca/alta-marca";
    }

    @PostMapping("/salvar-marca")
    public String salvarMarca(@Valid @ModelAttribute("marca") Marca marca,
                               BindingResult result,
                               Model model,
                               RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("contenido", "ERROR. No debe ser vacío");
            return "/marca/alta-marca";
        }

        marcaService.guardar(marca);
        flash.addFlashAttribute("success", "La marca se guardó correctamente");
        logger.info("Se guardó una nueva Marca con ID: " + marca.getIdMarca());

        return "redirect:/marca/lista-marca";
    }

    @GetMapping("/eliminar-marca/{id}")
    public String eliminarMarca(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            marcaService.borrar(id);
            flash.addFlashAttribute("success", "La marca se borró correctamente.");
            logger.info("Se borró la Marca con ID: " + id);
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar la marca porque está registrada con una playera.");
            logger.error("Error al eliminar la Marca con ID: {} porque está ligada a una playera",id);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar la marca.");
        }
        return "redirect:/marca/lista-marca";
    }

    @GetMapping("/editar-marca/{id}")
    public String modificarMarca(@PathVariable Integer id, Model model){
        Marca marca = marcaService.buscarPorId(id);
        model.addAttribute("marca",marca);
        model.addAttribute("contenido","Modificar Marca");
        model.addAttribute("subtitulo","Formulario para modificar una marca existente.");
        logger.info("Se editó la Marca con ID: "+id);
        return "/marca/alta-marca";
    }
}
