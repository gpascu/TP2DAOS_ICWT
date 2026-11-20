package tuti.daos.icwt.presentacion.pagos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import tuti.daos.icwt.entidades.MedioPago;
import tuti.daos.icwt.entidades.Pago;

public class PagoResponseDTO extends RepresentationModel<PagoResponseDTO> {

	private Long id;
	private Long idFactura;
	private LocalDate fechaPago;
	private MedioPago medio;
	private BigDecimal importePagado;
	private BigDecimal interes;

	public PagoResponseDTO(Pago pojo) {
		this.id = pojo.getId();
		this.idFactura = pojo.getFactura().getId();
		this.fechaPago = pojo.getFechaPago();
		this.medio = pojo.getMedio();
		this.importePagado = pojo.getImportePagado();
		this.interes = pojo.getInteres();
	}

	public Long getId() {
		return id;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public MedioPago getMedio() {
		return medio;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public BigDecimal getInteres() {
		return interes;
	}
}
