package mx.unam.fanaticosfc.service.pagosCredito;

import mx.unam.fanaticosfc.model.PagosCredito;
import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.repository.PagosCreditoRepository;
import mx.unam.fanaticosfc.repository.VentaCreditoRepository;
import mx.unam.fanaticosfc.service.GenericService;
import mx.unam.fanaticosfc.service.ventaCredito.VentaCreditoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagosCreditoServiceImpl implements GenericService<PagosCredito,Integer> {
    @Autowired
    PagosCreditoRepository pagosRepository;
    @Autowired
    VentaCreditoServiceImpl creditoService;

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

        VentaCredito ventaCredito = creditoService.buscarPorId(pago.getVentaCredito().getIdVentaCredito());

        if(pago.getMonto().compareTo(ventaCredito.getMontoRestante()) > 0){
            throw new IllegalArgumentException("El pago no puede ser mayor al monto restante");
        }

        BigDecimal nuevoRestante = ventaCredito.getMontoRestante().subtract(pago.getMonto());
        ventaCredito.setMontoRestante(nuevoRestante);

        ventaCredito.setPagosRealizados(ventaCredito.getPagosRealizados()+1);
        pago.setFechaPago(LocalDateTime.now());

        pagosRepository.save(pago);
        creditoService.guardar(ventaCredito);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        pagosRepository.deleteById(id);
    }
}
