package tuti.daos.icwt.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.Incidente;

//Repositorio encargado de las operaciones de acceso a datos de la entidad Incidente.
@Repository
public interface IIncidenteRepo extends JpaRepository<Incidente, Long> {

}
