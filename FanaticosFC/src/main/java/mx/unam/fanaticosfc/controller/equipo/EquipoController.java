package mx.unam.fanaticosfc.controller.equipo;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Equipo;
import mx.unam.fanaticosfc.service.equipo.EquipoServiceImpl;
import mx.unam.fanaticosfc.util.RenderPagina;
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
@RequestMapping("/equipo")
public class EquipoController {

    @Autowired
    EquipoServiceImpl equipoService;

    @GetMapping("/lista-equipo")
    public String listaEquipo(Model model){
        model.addAttribute("equipo",equipoService.listarTodos());
        return "/equipo/lista-equipo"; //se retorna el html
    }

    @GetMapping("/alta-equipo")
    public String altaEquipo(Model model){
        Equipo equipo = new Equipo();
        model.addAttribute("contenido","Agregar nuevo equipo");

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
        System.out.println(equipo);
        flash.addFlashAttribute("success", "El equipo se guardó correctamente");

        return "redirect:/equipo/lista-equipo";
    }

    @GetMapping("/eliminar-equipo/{id}")
    public String eliminarEquipo(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            equipoService.borrar(id);
            flash.addFlashAttribute("success", "El equipo se borró correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                        "No se puede eliminar el equipo porque está registrado en una playera.");
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el equipo.");
        }
        return "redirect:/equipo/lista-equipo";
    }

    @GetMapping("/editar-equipo/{id}")
    public String modificarEquipo(@PathVariable Integer id, Model model){
        Equipo equipo = equipoService.buscarPorId(id);
        model.addAttribute("equipo",equipo);
        model.addAttribute("contenido","Modificar Equipo");
        model.addAttribute("subtitulo","Formulario para modificar un equipo existente.");
        return "/equipo/alta-equipo";
    }


}
