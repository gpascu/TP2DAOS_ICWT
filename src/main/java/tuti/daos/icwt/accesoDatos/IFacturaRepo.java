package tuti.daos.icwt.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;

@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {

	// sirve para controlar que no haya dos facturas de facturacion mensual sin anular para el mismo contrato y periodo
	List<Factura> findByContratoIdAndPeriodoAndMensualAndEstadoNot(Long idContrato, String periodo, boolean mensual,
			EstadoFactura estado);

	// SELECT ... FOR UPDATE para que dos pagos de la misma factura se procesan de a uno.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Factura> findParaPagoById(Long id);

}
