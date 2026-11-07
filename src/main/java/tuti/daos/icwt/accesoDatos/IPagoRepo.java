package tuti.daos.icwt.accesoDatos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.Pago;

@Repository
public interface IPagoRepo extends JpaRepository<Pago, Long> {

	//Cada factura puede tener como maximo un pago
	Optional<Pago> findByFacturaId(Long idFactura);

}
