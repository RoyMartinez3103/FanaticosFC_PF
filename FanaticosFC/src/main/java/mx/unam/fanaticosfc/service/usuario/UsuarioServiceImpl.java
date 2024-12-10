package mx.unam.fanaticosfc.service.usuario;

import mx.unam.fanaticosfc.model.Usuario;
import mx.unam.fanaticosfc.repository.UsuarioRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements GenericService<Usuario,Integer> {

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        Optional<Usuario> opUsuario = usuarioRepository.findById(id);
        return opUsuario.orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        Optional<Usuario> opUsuario = usuarioRepository.findByUsername(username);
        return opUsuario.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        String encryptedPass = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(encryptedPass);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        usuarioRepository.deleteById(id);
    }

}

