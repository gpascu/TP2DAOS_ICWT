package tuti.daos.icwt.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import tuti.daos.icwt.entidades.CategoriaIncidente;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.PrioridadIncidente;

//Repositorio encargado de las operaciones de acceso a datos de la entidad Incidente.
@Repository
public interface IIncidenteRepo extends JpaRepository<Incidente, Long> {

	@Query("""
			select i from Incidente i
			where (:contratoId is null or i.contrato.id = :contratoId)
				and (:estado is null or i.estado = :estado)
				and (:categoria is null or i.categoria = :categoria)
				and (:prioridad is null or i.prioridad = :prioridad)
			order by i.id
			""")
	List<Incidente> listar(@Param("contratoId") Long contratoId, @Param("estado") EstadoIncidente estado,
			@Param("categoria") CategoriaIncidente categoria, @Param("prioridad") PrioridadIncidente prioridad);

	// S03 y S04 usan este bloqueo para decidir sobre el estado que quedó guardado.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select i from Incidente i where i.id = :id")
	Optional<Incidente> buscarPorIdConBloqueo(@Param("id") Long id);

}
