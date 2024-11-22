package mx.unam.fanaticosfc.service.marca;

import mx.unam.fanaticosfc.model.Marca;
import mx.unam.fanaticosfc.repository.MarcaRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaServiceImpl implements GenericService<Marca,Integer> {

    @Autowired
    MarcaRepository marcaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Marca> listarTodos( ) {
        return marcaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarPorId(Integer id) {
        Optional<Marca> opMarca = marcaRepository.findById(id);
        return opMarca.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Marca marca) {
        marcaRepository.save(marca);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        marcaRepository.deleteById(id);
    }

    public List<Marca> mostrar() {
        return marcaRepository.findAll();
    }
}
