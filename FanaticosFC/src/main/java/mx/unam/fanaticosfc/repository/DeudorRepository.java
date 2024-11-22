package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.Deudor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeudorRepository extends JpaRepository<Deudor,Integer> {
   Optional<Deudor> findByTelefono(String telefono);
}
