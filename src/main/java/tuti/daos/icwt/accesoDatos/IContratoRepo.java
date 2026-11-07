package tuti.daos.icwt.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.Contrato;

//Permite realizar las operaciones CRUD sobre la entidad Contrato
@Repository
public interface IContratoRepo extends JpaRepository<Contrato, Long> {

}
