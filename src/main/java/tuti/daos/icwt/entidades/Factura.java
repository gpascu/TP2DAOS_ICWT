package tuti.daos.icwt.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Contrato contrato;

	// periodo facturado en formato AAAA-MM, ej: "2026-03"
	private String periodo;

	private String concepto;

	private LocalDate fechaEmision;

	private LocalDate fechaVencimiento;

	private BigDecimal importe;

	@Enumerated(EnumType.STRING)
	private EstadoFactura estado;

	/*
	 * true cuando es una factura de la facturacion mensual (importe calculado
	 * por el sistema a partir del contrato), false cuando es una factura
	 * adicional cargada con todos sus datos (ej: reparacion a cargo del inquilino)
	 */
	private boolean mensual;

	@OneToOne(mappedBy = "factura")
	private Pago pago;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public boolean isMensual() {
		return mensual;
	}

	public void setMensual(boolean mensual) {
		this.mensual = mensual;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	/*
	 * Estado real de la factura al momento de consultarla:
	 * una factura sin pago y no anulada es pendiente hasta su vencimiento,
	 * y vencida a partir del dia siguiente.
	 */
	public EstadoFactura estadoActual() {
		if (estado == EstadoFactura.PENDIENTE && LocalDate.now().isAfter(fechaVencimiento))
			return EstadoFactura.VENCIDA;
		return estado;
	}
}
