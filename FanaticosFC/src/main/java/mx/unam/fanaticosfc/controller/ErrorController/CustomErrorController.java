package mx.unam.fanaticosfc.controller.ErrorController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMessage = "Ocurrió un error inesperado";

        if (statusCode != null) {
            switch (statusCode) {
                case 400:
                    errorMessage = "Solicitud incorrecta.";
                    break;
                case 404:
                    errorMessage = "Recurso no encontrado.";
                    break;
                case 500:
                    errorMessage = "Error interno del servidor.";
                    break;
                default:
                    errorMessage = "Error desconocido.";
            }
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("mensaje", errorMessage);
        return "error-page";
    }
}
