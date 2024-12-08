package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByIdUsuario(Integer id);
    Optional<Usuario> findByUsername(String username);
}

