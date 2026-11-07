package tuti.daos.icwt.servicios;

import java.util.List;
import java.util.Optional;

import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.facturas.FacturaDTO;

//Permite crear, buscar y anular facturas de contratos de alquiler. 
public interface FacturaService {

//Crea una factura y devuelve su ID. Sin importe, usa el del contrato y es mensual; con importe, es adicional. 
Long emitir(FacturaDTO dto) throws Excepcion;

//Busca por ID. El Optional contiene la factura si existe o queda vacío si no se encuentra. 
Optional<Factura> getById(Long id);

//Devuelve las facturas según el contrato, el período y el estado indicados. */
List<Factura> listar(Long idContrato, String periodo, EstadoFactura estado);

//Anula la factura con el ID indicado. 
void anular(Long id) throws Excepcion;
}