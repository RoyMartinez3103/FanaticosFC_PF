package mx.unam.fanaticosfc.controller.playera;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Equipo;
import mx.unam.fanaticosfc.model.Marca;
import mx.unam.fanaticosfc.model.Playera;
import mx.unam.fanaticosfc.service.equipo.EquipoServiceImpl;
import mx.unam.fanaticosfc.service.marca.MarcaServiceImpl;
import mx.unam.fanaticosfc.service.playera.PlayeraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/playera")
public class PlayeraController {

    @Autowired
    PlayeraServiceImpl playeraService;
    @Autowired
    MarcaServiceImpl marcaService;
    @Autowired
    EquipoServiceImpl equipoService;

    @GetMapping("/lista-playera")
    public String listaPlayera(Model model){
        model.addAttribute("playera",playeraService.listarTodos());
        return "/playera/lista-playera"; //se retorna el html
    }

    @GetMapping("/alta-playera")
    public String altaPlayera(Model model){
        Playera playera = new Playera();
        List<Equipo> equipos = equipoService.listarTodos();
        List<Marca> marcas = marcaService.listarTodos();

        model.addAttribute("equipo",equipos);
        model.addAttribute("marca",marcas);
        model.addAttribute("playera",playera);
        model.addAttribute("contenido","Agregar nueva playera");
        return "/playera/alta-playera";
    }

    @PostMapping("/salvar-playera")
    public String salvarPlayera(@Valid @ModelAttribute("playera") Playera playera,
                              BindingResult result,
                              Model model,
                              RedirectAttributes flash) {
        List<Equipo> equipos = equipoService.listarTodos();
        List<Marca> marcas = marcaService.listarTodos();

        model.addAttribute("equipo",equipos);
        model.addAttribute("marca",marcas);

        if (result.hasErrors()) {
            model.addAttribute("contenido", "ERROR. No debe ser vacío");
            return "/playera/alta-playera";
        }

        playeraService.guardar(playera);
        System.out.println(playera);
        flash.addFlashAttribute("success", "La playera se guardó correctamente");

        return "redirect:/playera/lista-playera";
    }

    @GetMapping("/eliminar-playera/{id}")
    public String eliminarPlayera(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            playeraService.borrar(id);
            flash.addFlashAttribute("success", "La playera se borró correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar la playera.");
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar la playera.");
        }
        return "redirect:/playera/lista-playera";
    }

    @GetMapping("/editar-playera/{id}")
    public String modificarPlayera(@PathVariable Integer id, Model model){
        Playera playera = playeraService.buscarPorId(id);
        List<Equipo> equipos = equipoService.listarTodos();
        List<Marca> marcas = marcaService.listarTodos();

        model.addAttribute("equipo",equipos);
        model.addAttribute("marca",marcas);
        model.addAttribute("playera",playera);
        model.addAttribute("contenido","Modificar Playera");
        model.addAttribute("subtitulo","Formulario para modificar una playera existente.");
        return "/playera/alta-playera";
    }

    @GetMapping("/mostrar-playeras")
    public String mostrarPlayeras(Model model){
        List<Playera> playeras = playeraService.mostrar();
        // Crear un Set para almacenar combinaciones únicas de color e idEquipo
        Set<String> uniqueJerseys = new HashSet<>();

        // Filtrar la lista
        List<Playera> filteredJerseys = playeras.stream()
                .filter(playera -> uniqueJerseys.add(playera.getColor() + "-" + playera.getEquipo().getIdEquipo() + "-" + playera.getTipoManga()))
                .toList();
        model.addAttribute("jerseys",filteredJerseys);
        return "/playera/mostrar-playeras";
    }
    
}
