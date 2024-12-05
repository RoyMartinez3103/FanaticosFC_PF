package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Integer> {

    @Query(value = "SELECT e.nombre, p.color, SUM(d.CANTIDAD_PLAYERAS) AS TOTAL_VENDIDAS FROM detalle_venta d JOIN playera p ON d.id_playera=p.id_playera JOIN equipo e ON p.id_equipo = e.id_equipo JOIN venta v ON d.id_venta = v.id_venta WHERE MONTH(v.fecha_venta) = MONTH(CURDATE()) GROUP BY d.ID_PLAYERA ORDER BY TOTAL_VENDIDAS DESC LIMIT 1",nativeQuery = true)
    List<Object[]> getPlayeraMasVendida();

    @Query(value = "SELECT p.talla, SUM(d.CANTIDAD_PLAYERAS) AS TOTAL_VENDIDAS " +
            "FROM detalle_venta d " +
            "JOIN playera p ON d.id_playera=p.id_playera " +
            "GROUP BY p.talla ORDER BY TOTAL_VENDIDAS DESC",nativeQuery = true)
    List<Object[]> getTallasVendidas();
}
