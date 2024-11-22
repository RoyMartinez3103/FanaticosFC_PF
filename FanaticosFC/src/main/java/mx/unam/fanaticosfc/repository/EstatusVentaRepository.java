package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.EstatusVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatusVentaRepository extends JpaRepository<EstatusVenta,Integer> {
    EstatusVenta findByIdEstatusVenta(Integer id);
}
