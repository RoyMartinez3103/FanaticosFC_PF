package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
}

