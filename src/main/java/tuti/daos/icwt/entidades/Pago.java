package tuti.daos.icwt.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * Cada factura puede tener como maximo un pago,
	 * la restriccion tambien queda protegida en la base con el unique=true
	 */
	@OneToOne(optional = false)
	@JoinColumn(name = "factura_id", unique = true)
	private Factura factura;

	private LocalDate fechaPago;

	@Enumerated(EnumType.STRING)
	private MedioPago medio;

	// total abonado, incluye el interes
	private BigDecimal importePagado;

	// importe del interes, cero cuando no corresponde
	private BigDecimal interes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
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
