package tuti.daos.icwt.servicios;

import java.util.List;
import java.util.Optional;

import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.contratos.ContratoDTO;

//Permite consultar, crear y eliminar contratos usados para emitir facturas y registrar incidentes. 
public interface ContratoService {

	//Devuelve todos los contratos guardados.
    List<Contrato> getAll();

    //Busca por ID; el Optional contiene el contrato si existe o queda vacío si no se encuentra. 
    Optional<Contrato> getById(Long id);

    //Crea un contrato con los datos recibidos y devuelve su ID.
    Long insert(ContratoDTO dto) throws Excepcion;

    //Marca el contrato como eliminado, pero lo mantiene en la base de datos.
    void eliminar(Long id) throws Excepcion;
}