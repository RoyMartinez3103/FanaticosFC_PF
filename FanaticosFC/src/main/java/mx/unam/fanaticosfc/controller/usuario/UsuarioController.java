package mx.unam.fanaticosfc.controller.usuario;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Usuario;
import mx.unam.fanaticosfc.service.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServiceImpl usuarioService;

    @GetMapping("/lista-usuario")
    public String listaUsuario(Model model){
        model.addAttribute("usuario",usuarioService.listarTodos());
        return "/usuario/lista-usuario"; //se retorna el html
    }

    @GetMapping("/alta-usuario")
    public String altaUsuario(Model model){
        Usuario usuario = new Usuario();
        model.addAttribute("contenido","Agregar nuevo usuario");
        model.addAttribute("usuario",usuario);
        return "/usuario/alta-usuario";
    }

    @PostMapping("/salvar-usuario")
    public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                              BindingResult result,
                              Model model,
                              RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("contenido", "ERROR");
            return "/usuario/alta-usuario";
        }
        if (usuario.getRol() == null) {
            usuario.setRol("USER");
        }

        usuarioService.guardar(usuario);
        flash.addFlashAttribute("success", "El usuario se guardó correctamente");

        return "redirect:/usuario/lista-usuario";
    }

    @GetMapping("/eliminar-usuario/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            usuarioService.borrar(id);
            flash.addFlashAttribute("success", "El usuario se borró correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar el usuario.");
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el usuario.");
        }
        return "redirect:/usuario/lista-usuario";
    }

    @GetMapping("/editar-usuario/{id}")
    public String modificarUsuario(@PathVariable Integer id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario",usuario);
        model.addAttribute("contenido","Modificar Usuario");
        model.addAttribute("subtitulo","Formulario para modificar un usuario existente.");
        return "/usuario/alta-usuario";
    }


}

