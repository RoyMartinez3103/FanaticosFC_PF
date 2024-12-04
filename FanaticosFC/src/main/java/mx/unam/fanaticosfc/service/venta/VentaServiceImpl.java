package mx.unam.fanaticosfc.service.venta;

import mx.unam.fanaticosfc.model.*;
import mx.unam.fanaticosfc.repository.*;
import mx.unam.fanaticosfc.service.GenericService;
import mx.unam.fanaticosfc.service.usuario.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private UsuarioServiceImpl usuarioService;

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
    @Transactional
    public void guardar(Venta venta) {
        Usuario usuario = (usuarioService.buscarPorId(2));

        venta.setUsuario(usuario);
        usuario.setVentasRealizadas(usuario.getVentasRealizadas()+1);

        if(venta.getVentaCredito()){
            venta.setEstatusVenta(estatusRepository.findByIdEstatusVenta(2));
        }else
            venta.setEstatusVenta(estatusRepository.findByIdEstatusVenta(1));
        ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void borrar(Integer id) {
        ventaRepository.deleteById(id);
    }
}
