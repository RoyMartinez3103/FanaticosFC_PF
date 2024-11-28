package mx.unam.fanaticosfc.repository;

import jakarta.persistence.NamedNativeQuery;
import mx.unam.fanaticosfc.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;



public interface VentaRepository extends JpaRepository<Venta,Integer> {
    @Query("SELECT u.nombre, COUNT(v.usuario.idUsuario) " +
            "FROM Venta v " +
            "INNER JOIN Usuario u ON v.usuario.idUsuario=u.idUsuario " +
            "GROUP BY u.nombre " +
            "ORDER BY COUNT(v.usuario.idUsuario) DESC")
    List<Object[]> getUsuariosMasVentas();

    @Query("SELECT MONTH(fechaVenta),SUM(montoTotal) " +
            "FROM Venta v WHERE YEAR(fechaVenta) = :anio " +
            "GROUP BY MONTH(fechaVenta) ORDER BY MONTH(fechaVenta)")
    List<Object[]> getGananciasMes(@Param("anio")Integer anio);

    @Query("SELECT SUM(montoTotal) " +
            "FROM Venta " +
            "WHERE YEAR(fechaVenta) = YEAR(CURDATE()) AND MONTH(fechaVenta) = MONTH(CURDATE()) " +
            "GROUP BY MONTH(fechaVenta)")
    String gananciaMes();
}
