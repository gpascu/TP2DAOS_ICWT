package tuti.daos.icwt.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.daos.icwt.accesoDatos.IPagoRepo;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;
import tuti.daos.icwt.entidades.Pago;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.pagos.PagoDTO;

@Service
public class PagoServiceImpl implements PagoService {

	@Autowired
	private IPagoRepo repo;

	@Autowired
	private FacturaService facturaService;

	// sin rollbackFor, una falla después del INSERT dejaría el pago con la factura sin pagar.
	@Override
	@Transactional(rollbackFor = Excepcion.class)
	public Long registrar(PagoDTO dto) throws Excepcion {
		// Con la factura bloqueada, un segundo pago simultáneo espera y la encuentra pagada.
		// Sin el bloqueo MySQL los traba entre sí y responde 500.
		Optional<Factura> rta = facturaService.getByIdParaPago(dto.getIdFactura());

		if (!rta.isPresent())
			throw new Excepcion("idFactura", "La factura indicada no existe.", 404);

		Factura f = rta.get();

		EstadoFactura estado = f.estadoActual();

		if (estado == EstadoFactura.ANULADA)
			throw new Excepcion("idFactura", "No se puede pagar una factura anulada.", 409);

		if (estado == EstadoFactura.PAGADA)
			throw new Excepcion("idFactura", "La factura ya se encuentra pagada.", 409);

		if (repo.findByFacturaId(f.getId()).isPresent())
			throw new Excepcion("idFactura", "La factura ya tiene un pago registrado.", 409);

		LocalDate fechaPago = dto.getFechaPago() != null
				? dto.getFechaPago()
				: LocalDate.now();

		if (fechaPago.isBefore(f.getFechaEmision()))
			throw new Excepcion("fechaPago", "La fecha de pago no puede ser anterior a la emision de la factura.", 409);

		// R8 y R9: importe pagado - interés = importe de la factura.
		BigDecimal importeSinInteres = dto.getImportePagado().subtract(dto.getInteres());

		if (importeSinInteres.compareTo(f.getImporte()) != 0)
			throw new Excepcion("importePagado",
					"El importe pagado menos el interes debe ser igual al importe de la factura (" + f.getImporte()
							+ "). No se admiten pagos parciales.",
					409);

		Pago p = new Pago();
		p.setFactura(f);
		p.setFechaPago(fechaPago);
		p.setMedio(dto.getMedio());
		p.setImportePagado(dto.getImportePagado());
		p.setInteres(dto.getInteres());

		// El unique de factura_id es la última defensa de R4.
		try {
			p = repo.saveAndFlush(p);
		} catch (DataIntegrityViolationException e) {
			throw new Excepcion("idFactura", "La factura ya tiene un pago registrado.", 409);
		}

		facturaService.marcarPagada(f, p);

		return p.getId();
	}

	@Override
	public Optional<Pago> getById(Long id) {
		return repo.findById(id);
	}

	@Override
	public List<Pago> listar(Long idFactura) {
		if (idFactura == null)
			return repo.findAll();

		List<Pago> resultado = new ArrayList<Pago>();
		repo.findByFacturaId(idFactura).ifPresent(resultado::add);
		return resultado;
	}
}
