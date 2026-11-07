package tuti.daos.icwt.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.HistorialEstadoIncidente;

@Repository
public interface IHistorialIncidenteRepo extends JpaRepository<HistorialEstadoIncidente, Long> {

	// el historial se devuelve de solo lectura y ordenado cronologicamente
	List<HistorialEstadoIncidente> findByIncidenteIdOrderByFechaHoraAsc(Long idIncidente);

}
