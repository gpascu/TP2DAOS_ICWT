package tuti.daos.icwt.presentacion.pagos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import tuti.daos.icwt.entidades.MedioPago;

public class PagoDTO {

	@NotNull(message = "La factura es obligatoria")
	private Long idFactura;

	@PastOrPresent(message = "La fecha de pago no puede ser futura")
	private LocalDate fechaPago;

	@NotNull(message = "El medio de pago es obligatorio")
	private MedioPago medio;

	@NotNull(message = "El importe pagado es obligatorio")
	@Positive(message = "El importe pagado debe ser positivo")
	@Digits(integer = 36, fraction = 2, message = "El importe pagado admite hasta dos decimales")
	private BigDecimal importePagado;

	@NotNull(message = "El interes es obligatorio")
	@PositiveOrZero(message = "El interes no puede ser negativo")
	@Digits(integer = 36, fraction = 2, message = "El interes admite hasta dos decimales")
	private BigDecimal interes;

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public MedioPago getMedio() {
		return medio;
	}

	public void setMedio(MedioPago medio) {
		this.medio = medio;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
}
