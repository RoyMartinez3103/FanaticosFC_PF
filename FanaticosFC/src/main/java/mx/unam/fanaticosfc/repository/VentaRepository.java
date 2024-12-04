package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
@Repository
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
    BigDecimal gananciaMes();

    @Query(value = "SELECT u.nombre, u.apellido_pat, COUNT(v.ID_USUARIO) as ventas " +
            "FROM venta v " +
            "JOIN usuario u ON v.ID_USUARIO=u.ID_USUARIO " +
            "WHERE MONTH(fecha_venta) = MONTH(CURDATE()) " +
            "GROUP BY u.nombre " +
            "ORDER BY ventas DESC LIMIT 1",nativeQuery = true)
    List<Object[]> getEmpleadoDelMes();

    @Query("SELECT COUNT(*) " +
            "FROM Venta " +
            "WHERE MONTH(fechaVenta) = MONTH(CURDATE())")
    Integer ventasMes();


}
