package mx.unam.fanaticosfc.security.service;

import lombok.AllArgsConstructor;
import mx.unam.fanaticosfc.controller.equipo.EquipoController;
import mx.unam.fanaticosfc.model.Usuario;
import mx.unam.fanaticosfc.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(EquipoController.class);
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario: " + username + " no encontrado"));
        logger.info("Usuario autenticado: {} con rol: {}",usuario.getUsername(),usuario.getRol());
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getContrasena())
                .authorities("ROLE_"+usuario.getRol())
                .build();
    }
}
