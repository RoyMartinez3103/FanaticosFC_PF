package mx.unam.fanaticosfc.controller.marca;


import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Marca;
import mx.unam.fanaticosfc.service.marca.MarcaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/marca")
public class MarcaController {
    
    @Autowired
    MarcaServiceImpl marcaService;
    
    @GetMapping("/lista-marca")
    public String listaMarca(Model model){
        model.addAttribute("marca",marcaService.listarTodos());
        return "/marca/lista-marca"; //se retorna el html
    }

    @GetMapping("/alta-marca")
    public String altaMarca(Model model){
        Marca marca = new Marca();
        model.addAttribute("contenido","Agregar nueva marca");
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
        System.out.println(marca);
        flash.addFlashAttribute("success", "La marca se guardó correctamente");

        return "redirect:/marca/lista-marca";
    }

    @GetMapping("/eliminar-marca/{id}")
    public String eliminarMarca(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            marcaService.borrar(id);
            flash.addFlashAttribute("success", "La marca se borró correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar la marca porque está registrada con una playera.");
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
        return "/marca/alta-marca";
    }
}
