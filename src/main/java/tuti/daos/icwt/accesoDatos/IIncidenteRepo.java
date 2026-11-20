
package tuti.daos.icwt.accesoDatos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import tuti.daos.icwt.entidades.Incidente;

@Repository
public interface IIncidenteRepo extends JpaRepository<Incidente, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Incidente i WHERE i.id = :id")
    Optional<Incidente> buscarPorIdConBloqueo(@Param("id") Long id);

}
