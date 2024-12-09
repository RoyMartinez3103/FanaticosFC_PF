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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
                              @RequestParam("imagen") MultipartFile imageFile,
                              Model model,
                              RedirectAttributes flash) throws IOException {
        try {
            if (!imageFile.isEmpty()) {
                String directorioUploads = "C:/uploads/";
                Path rutaDirectorio = Paths.get(directorioUploads);
                if (!Files.exists(rutaDirectorio)) {
                    Files.createDirectories(rutaDirectorio);
                }

                String nombreArchivo = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
                Files.copy(imageFile.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo guardado en: " + rutaArchivo.toAbsolutePath());

                playera.setImagenRuta("/uploads/" + nombreArchivo);
            }
            if (!imageFile.isEmpty()) {
                System.out.println("Archivo recibido: " + imageFile.getOriginalFilename());
            } else {
                System.out.println("El archivo no se recibió correctamente.");
            }


            List<Equipo> equipos = equipoService.listarTodos();
            List<Marca> marcas = marcaService.listarTodos();

            model.addAttribute("equipo", equipos);
            model.addAttribute("marca", marcas);

            if (result.hasErrors()) {
                model.addAttribute("contenido", "ERROR.");
                System.out.println("errores: " + result.getAllErrors());
                return "/playera/alta-playera";
            }
            System.out.println("Ruta almacenada "+ playera.getImagenRuta());

            playeraService.guardar(playera);
            flash.addFlashAttribute("success", "La playera se guardó correctamente");
            return "redirect:/playera/lista-playera";

        }catch (Exception e){
            e.printStackTrace();
        }
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
        List<Playera> playeras = playeraService.listarTodos();
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
