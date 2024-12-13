package mx.unam.fanaticosfc.controller.equipo;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Equipo;
import mx.unam.fanaticosfc.service.equipo.EquipoServiceImpl;
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
@RequestMapping("/equipo")
public class EquipoController {
    private static final Logger logger = LoggerFactory.getLogger(EquipoController.class);

    @Autowired
    EquipoServiceImpl equipoService;

    @GetMapping("/lista-equipo")
    public String listaEquipo(Model model){
        model.addAttribute("equipo",equipoService.listarTodos());
        model.addAttribute("contenido","Lista de Equipos");
        model.addAttribute("subtitulo", "Equipos registrados en el sistema.");
        return "/equipo/lista-equipo";
    }

    @GetMapping("/alta-equipo")
    public String altaEquipo(Model model){
        Equipo equipo = new Equipo();
        model.addAttribute("contenido","Agregar nuevo equipo");
        model.addAttribute("subtitulo", "Ingresar los datos del nuevo equipo");
        model.addAttribute("equipo",equipo);
        return "/equipo/alta-equipo";
    }

    @PostMapping("/salvar-equipo")
    public String salvarEquipo(@Valid @ModelAttribute("equipo") Equipo equipo,
                               BindingResult result,
                               Model model,
                               RedirectAttributes flash){
        if(result.hasErrors()){
            model.addAttribute("contenido","ERROR");
            return "/equipo/alta-equipo";
        }

        equipoService.guardar(equipo);
        flash.addFlashAttribute("success", "El equipo se guardó correctamente");
        logger.info("El equipo con ID {} se guardó correctamente.",equipo.getIdEquipo());

        return "redirect:/equipo/lista-equipo";
    }

    @GetMapping("/eliminar-equipo/{id}")
    public String eliminarEquipo(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            equipoService.borrar(id);
            flash.addFlashAttribute("success", "El equipo se borró correctamente.");
            logger.info("El equipo se borró con ID {} se borró correctamente.",id);

        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                        "No se puede eliminar el equipo porque está registrado en una playera.");
            logger.error("Error al borrar el equipo con ID {} debido porque está ligado a una playera",id);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el equipo.");
            logger.error("Ocurrió un error al eliminar el equipo con id {}: {}",id,e.getMessage());
        }
        return "redirect:/equipo/lista-equipo";
    }

    @GetMapping("/editar-equipo/{id}")
    public String modificarEquipo(@PathVariable Integer id, Model model){

        Equipo equipo = equipoService.buscarPorId(id);
        model.addAttribute("equipo",equipo);
        model.addAttribute("contenido","Modificar Equipo");
        model.addAttribute("subtitulo","Formulario para modificar un equipo existente.");

        logger.info("El equipo con ID: {} se modificó correctamente.",id);
        return "/equipo/alta-equipo";
    }


}
