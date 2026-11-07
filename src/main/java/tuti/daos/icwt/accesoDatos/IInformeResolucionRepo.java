package tuti.daos.icwt.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.InformeResolucion;

@Repository
public interface IInformeResolucionRepo extends JpaRepository<InformeResolucion, Long> {
	// Recupera todos los informes de resolución correspondientes al incidente indicado.
	List<InformeResolucion> findByIncidenteIdOrderByFechaHoraResolucionAsc(Long idIncidente);

}
