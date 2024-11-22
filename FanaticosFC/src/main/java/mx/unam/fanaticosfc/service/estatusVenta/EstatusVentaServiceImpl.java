package mx.unam.fanaticosfc.service.estatusVenta;

import mx.unam.fanaticosfc.model.EstatusVenta;
import mx.unam.fanaticosfc.repository.EstatusVentaRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstatusVentaServiceImpl implements GenericService<EstatusVenta,Integer> {
    
    @Autowired
    EstatusVentaRepository estatusVentaRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<EstatusVenta> listarTodos() {
        return estatusVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EstatusVenta buscarPorId(Integer id) {
        Optional<EstatusVenta> opEstatusVenta = estatusVentaRepository.findById(id);
        return opEstatusVenta.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(EstatusVenta estatusVenta) {
        estatusVentaRepository.save(estatusVenta);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        estatusVentaRepository.deleteById(id);
    }

}
