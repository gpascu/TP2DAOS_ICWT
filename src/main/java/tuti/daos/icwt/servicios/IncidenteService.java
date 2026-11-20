package tuti.daos.icwt.servicios;

import java.util.List;

import tuti.daos.icwt.entidades.CategoriaIncidente;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.HistorialEstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.PrioridadIncidente;
import tuti.daos.icwt.excepciones.Excepcion;

public interface IncidenteService {

	Incidente registrar(Long contratoId, String titulo, String descripcion, CategoriaIncidente categoria,
			PrioridadIncidente prioridad) throws Excepcion;

	Incidente getById(Long id) throws Excepcion;

	List<Incidente> listar(Long contratoId, EstadoIncidente estado, CategoriaIncidente categoria,
			PrioridadIncidente prioridad);

	Incidente cambiarEstado(Long id, EstadoIncidente nuevoEstado) throws Excepcion;

	List<HistorialEstadoIncidente> consultarHistorial(Long id) throws Excepcion;
}
