package mx.unam.fanaticosfc.service.detalleVenta;


import mx.unam.fanaticosfc.model.DetalleVenta;
import mx.unam.fanaticosfc.repository.DetalleVentaRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements GenericService<DetalleVenta,Integer> {

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleVenta buscarPorId(Integer id) {
        Optional<DetalleVenta> opDetalleVenta = detalleVentaRepository.findById(id);
        return opDetalleVenta.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(DetalleVenta detalleDetalleVenta) {
        detalleVentaRepository.save(detalleDetalleVenta);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        detalleVentaRepository.deleteById(id);
    }
}
