package tuti.daos.icwt.accesoDatos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import tuti.daos.icwt.entidades.Contrato;

//Permite realizar las operaciones CRUD sobre la entidad Contrato
@Repository
public interface IContratoRepo extends JpaRepository<Contrato, Long> {
	// Serializo la comprobacion y el alta de facturas mensuales del mismo contrato.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Contrato c where c.id = :id")
    Optional<Contrato> buscarPorIdParaFacturacion(@Param("id") Long id);

}
