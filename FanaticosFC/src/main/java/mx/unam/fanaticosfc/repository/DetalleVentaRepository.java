package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Integer> {

    @Query(value = "SELECT e.nombre, p.color, SUM(d.CANTIDAD_PLAYERAS) AS TOTAL_VENDIDAS FROM detalle_venta d JOIN playera p ON d.id_playera=p.id_playera JOIN equipo e ON p.id_equipo = e.id_equipo JOIN venta v ON d.id_venta = v.id_venta WHERE MONTH(v.fecha_venta) = MONTH(CURDATE()) GROUP BY d.ID_PLAYERA ORDER BY TOTAL_VENDIDAS DESC LIMIT 1",nativeQuery = true)
    List<Object[]> getPlayeraMasVendida();

    @Query(value = "SELECT p.talla, SUM(d.CANTIDAD_PLAYERAS) AS TOTAL_VENDIDAS " +
            "FROM detalle_venta d " +
            "JOIN playera p ON d.id_playera=p.id_playera " +
            "GROUP BY p.talla ORDER BY TOTAL_VENDIDAS DESC",nativeQuery = true)
    List<Object[]> getTallasVendidas();

    @Query(value = "SELECT e.nombre, SUM(d.CANTIDAD_PLAYERAS) AS TOTAL_VENDIDAS " +
            "FROM detalle_venta d " +
            "JOIN playera p ON d.id_playera=p.id_playera " +
            "JOIN equipo e ON p.id_equipo = e.id_equipo " +
            "GROUP BY e.id_equipo ORDER BY TOTAL_VENDIDAS DESC LIMIT 5",nativeQuery = true)
    List<Object[]> getTop5Playeras();

    @Query(value = "SELECT E.NOMBRE, M.NOMBRE, P.COLOR,P.TALLA, D.CANTIDAD_PLAYERAS, P.PRECIO_VENTA, V.MONTO_TOTAL,U.USERNAME,V.ES_VENTA_CREDITO,V.FECHA_VENTA " +
            "FROM DETALLE_VENTA D  " +
            "JOIN PLAYERA P ON D.ID_PLAYERA = P.ID_PLAYERA " +
            "JOIN VENTA V ON D.ID_VENTA = V.ID_VENTA " +
            "JOIN EQUIPO E ON P.ID_EQUIPO = E.ID_EQUIPO " +
            "JOIN MARCA M ON M.ID_MARCA = P.ID_MARCA " +
            "JOIN USUARIO U ON V.ID_USUARIO = U.ID_USUARIO " +
            "WHERE D.ID_VENTA = :idVenta " +
            "GROUP BY D.ID_DETALLE_VENTA",nativeQuery = true)
    List<Object[]> getDetallesTicket(@Param("idVenta") Integer idVenta);

}
