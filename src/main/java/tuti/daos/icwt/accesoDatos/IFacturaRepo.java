package tuti.daos.icwt.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;

@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {

	// sirve para controlar que no haya dos facturas de facturacion mensual sin anular para el mismo contrato y periodo
	List<Factura> findByContratoIdAndPeriodoAndMensualAndEstadoNot(Long idContrato, String periodo, boolean mensual,
			EstadoFactura estado);

}
