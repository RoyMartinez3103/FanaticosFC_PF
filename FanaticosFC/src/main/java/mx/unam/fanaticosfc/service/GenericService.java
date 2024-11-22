package mx.unam.fanaticosfc.service;

import java.util.List;

public interface GenericService <T, ID> {
    List<T> listarTodos();
    T buscarPorId(ID id);
    void guardar(T entidad);
    void borrar(ID id);
}
