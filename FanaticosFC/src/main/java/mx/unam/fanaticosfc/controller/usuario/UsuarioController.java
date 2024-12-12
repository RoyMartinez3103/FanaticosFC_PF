package mx.unam.fanaticosfc.controller.usuario;

import jakarta.validation.Valid;
import mx.unam.fanaticosfc.model.Usuario;
import mx.unam.fanaticosfc.service.usuario.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    UsuarioServiceImpl usuarioService;

    @GetMapping("/lista-usuario")
    public String listaUsuario(Model model){
        model.addAttribute("usuario",usuarioService.listarTodos());
        return "/usuario/lista-usuario";
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
                              RedirectAttributes flash) {

        if (usuario.getRol() == null) {
            usuario.setRol("USER");
        }

        usuarioService.guardar(usuario);
        flash.addFlashAttribute("success", "El usuario se guardó correctamente");
        logger.info("Usuario {} registrado con éxito",usuario.getUsername());
        return "redirect:/usuario/lista-usuario";
    }

    @GetMapping("/eliminar-usuario/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            usuarioService.borrar(id);
            flash.addFlashAttribute("success", "El usuario se borró correctamente.");
            logger.info("El usuario con ID {} se borró correctamente",id);
        } catch (DataIntegrityViolationException e) {
            // Este error ocurre cuando hay violación de integridad referencial
            flash.addFlashAttribute("error",
                    "No se puede eliminar el usuario.");
            logger.error("Error al intentar borrar el usuario con id {}",id);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            flash.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el usuario.");
        }
        return "redirect:/usuario/lista-usuario";
    }

    @GetMapping("/editar-usuario/{id}")
    public String modificarUsuario(@Valid@PathVariable Integer id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario",usuario);
        model.addAttribute("contenido","Modificar Usuario");
        model.addAttribute("subtitulo","Formulario para modificar un usuario existente.");
        logger.info("El usuario con ID {} se modificó correctamente",id);
        return "/usuario/alta-usuario";
    }


}

