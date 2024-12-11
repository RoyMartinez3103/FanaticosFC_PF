package mx.unam.fanaticosfc.repository;

import mx.unam.fanaticosfc.model.Playera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PlayeraRepository extends JpaRepository<Playera,Integer>{
    @Query("SELECT p.id AS idPlayera, p.talla AS talla, e.nombre AS equipo " +
            "FROM Playera p JOIN p.equipo e WHERE e.id = :idEquipo AND p.color = :color")
    List<Map<String, Object>> findTallasByEquipoId(@Param("idEquipo") Integer idEquipo, @Param("color") String color);

    @Query(value = "SELECT * FROM playera WHERE id_equipo = :idEquipo", nativeQuery = true)
    List<Playera> obtenerPlayerasPorEquipo(@Param("idEquipo") Integer idEquipo);



}


