package mx.unam.fanaticosfc.service.playera;

import mx.unam.fanaticosfc.model.Playera;
import mx.unam.fanaticosfc.repository.PlayeraRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayeraServiceImpl implements GenericService<Playera,Integer> {
    
    @Autowired
    PlayeraRepository playeraRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Playera> listarTodos() {
        return playeraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Playera buscarPorId(Integer id) {
        Optional<Playera> opPlayera = playeraRepository.findById(id);
        return opPlayera.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Playera playera) {
        playeraRepository.save(playera);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        playeraRepository.deleteById(id);
    }

    public Playera findAllById(Iterable<Integer> playeras){
     return (Playera) playeraRepository.findAllById(playeras);
    }

    public List<Map<String,Object>> obtenerTallas(Integer idEquipo, String color){
        return playeraRepository.findTallasByEquipoId(idEquipo,color);
    }


}
