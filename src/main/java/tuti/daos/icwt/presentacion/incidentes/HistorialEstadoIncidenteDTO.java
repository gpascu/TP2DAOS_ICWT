package tuti.daos.icwt.presentacion.incidentes;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.HistorialEstadoIncidente;

@Relation(collectionRelation = "historial")
public record HistorialEstadoIncidenteDTO(
		EstadoIncidente estadoAnterior,
		EstadoIncidente estadoNuevo,
		LocalDateTime fechaHora) {

	public HistorialEstadoIncidenteDTO(HistorialEstadoIncidente entrada) {
		this(entrada.getEstadoAnterior(), entrada.getEstadoNuevo(), entrada.getFechaHora());
	}
}
