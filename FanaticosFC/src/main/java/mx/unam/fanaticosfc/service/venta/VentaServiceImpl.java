package mx.unam.fanaticosfc.service.venta;

import mx.unam.fanaticosfc.model.Deudor;
import mx.unam.fanaticosfc.model.EstatusVenta;
import mx.unam.fanaticosfc.model.Venta;
import mx.unam.fanaticosfc.model.VentaCredito;
import mx.unam.fanaticosfc.repository.DeudorRepository;
import mx.unam.fanaticosfc.repository.EstatusVentaRepository;
import mx.unam.fanaticosfc.repository.VentaCreditoRepository;
import mx.unam.fanaticosfc.repository.VentaRepository;
import mx.unam.fanaticosfc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements GenericService<Venta,Integer> {
    
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    private VentaCreditoRepository ventaCreditoRepository;
    @Autowired
    private DeudorRepository deudorRepository;
    @Autowired
    private EstatusVentaRepository estatusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Venta buscarPorId(Integer id) {
        Optional<Venta> opVenta = ventaRepository.findById(id);
        return opVenta.orElse(null);
    }

    @Override
    public void guardar(Venta venta) {
        venta.setMontoTotal(venta.getMontoTotal());
        System.out.println("monto "+venta.getMontoTotal());
        venta.setFechaVenta(LocalDate.now());
        if(venta.getVentaCredito()){
            venta.setEstatusVenta(estatusRepository.findByIdEstatusVenta(2));
        }else
            venta.setEstatusVenta(estatusRepository.findByIdEstatusVenta(1));
        System.out.println("Credito "+venta.getVentaCredito());
        ventaRepository.save(venta);
    }


    @Transactional
    public void guardarVenta(Venta venta) {
        Venta ventaNueva = new Venta();
        ventaNueva.setMontoTotal(venta.getMontoTotal());
        ventaNueva.setFechaVenta(LocalDate.now());
        ventaNueva.setVentaCredito(venta.getVentaCredito());
        if(venta.getVentaCredito()){
            ventaNueva.setEstatusVenta(estatusRepository.findByIdEstatusVenta(2));
        }else
            ventaNueva.setEstatusVenta(estatusRepository.findByIdEstatusVenta(1));
        ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        ventaRepository.deleteById(id);
    }
}
