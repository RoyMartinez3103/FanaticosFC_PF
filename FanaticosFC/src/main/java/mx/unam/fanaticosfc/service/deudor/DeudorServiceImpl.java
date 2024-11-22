package mx.unam.fanaticosfc.service.deudor;

import mx.unam.fanaticosfc.model.Deudor;
import mx.unam.fanaticosfc.repository.DeudorRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeudorServiceImpl implements GenericService<Deudor,Integer> {
    
    @Autowired
    DeudorRepository deudorRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Deudor> listarTodos() {
        return deudorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Deudor buscarPorId(Integer id) {
        Optional<Deudor> opDeudor = deudorRepository.findById(id);
        return opDeudor.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Deudor deudor) {
        deudorRepository.save(deudor);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        deudorRepository.deleteById(id);
    }
}
