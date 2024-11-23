package mx.unam.fanaticosfc.service.ventaCredito;

import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.repository.VentaCreditoRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaCreditoServiceImpl implements GenericService<VentaCredito,Integer> {
    @Autowired
    VentaCreditoRepository ventaCreditoRepository;

    @Override
    public List<VentaCredito> listarTodos() {
        return ventaCreditoRepository.findAll();
    }

    @Override
    public VentaCredito buscarPorId(Integer id) {
        Optional<VentaCredito> ventaCredito = ventaCreditoRepository.findById(id);
        return ventaCredito.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(VentaCredito ventaCredito) {
        ventaCreditoRepository.save(ventaCredito);

    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        ventaCreditoRepository.deleteById(id);
    }
}
