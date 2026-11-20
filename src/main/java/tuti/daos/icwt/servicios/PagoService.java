package tuti.daos.icwt.servicios;

import java.util.List;
import java.util.Optional;

import tuti.daos.icwt.entidades.Pago;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.pagos.PagoDTO;

public interface PagoService {

Long registrar(PagoDTO dto) throws Excepcion;

Optional<Pago> getById(Long id);

List<Pago> listar(Long idFactura);
}
