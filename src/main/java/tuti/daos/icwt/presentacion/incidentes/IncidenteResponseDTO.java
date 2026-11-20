package tuti.daos.icwt.presentacion.incidentes;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

import tuti.daos.icwt.entidades.CategoriaIncidente;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.PrioridadIncidente;

@Relation(collectionRelation = "incidentes")
public record IncidenteResponseDTO(
		Long id,
		Long contratoId,
		String titulo,
		String descripcion,
		CategoriaIncidente categoria,
		LocalDateTime fechaHoraAlta,
		PrioridadIncidente prioridad,
		EstadoIncidente estado) {

	public IncidenteResponseDTO(Incidente incidente) {
		this(incidente.getId(), incidente.getContrato().getId(), incidente.getTitulo(), incidente.getDescripcion(),
				incidente.getCategoria(), incidente.getFechaHoraAlta(), incidente.getPrioridad(), incidente.getEstado());
	}
}
