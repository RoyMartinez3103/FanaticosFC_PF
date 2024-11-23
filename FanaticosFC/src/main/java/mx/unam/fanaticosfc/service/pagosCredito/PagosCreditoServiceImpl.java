package mx.unam.fanaticosfc.service.pagosCredito;

import mx.unam.fanaticosfc.model.PagosCredito;
import mx.unam.fanaticosfc.repository.PagosCreditoRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PagosCreditoServiceImpl implements GenericService<PagosCredito,Integer> {
    @Autowired
    PagosCreditoRepository pagosRepository;

    @Override
    public List<PagosCredito> listarTodos() {
        return pagosRepository.findAll();
    }

    @Override
    public PagosCredito buscarPorId(Integer id) {
        Optional<PagosCredito> pago = pagosRepository.findById(id);
        return pago.orElse(null);
    }

    @Override
    @Transactional
    public void guardar(PagosCredito pago) {
        pagosRepository.save(pago);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        pagosRepository.deleteById(id);
    }
}
