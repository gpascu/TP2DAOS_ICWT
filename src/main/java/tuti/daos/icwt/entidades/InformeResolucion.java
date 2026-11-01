package tuti.daos.icwt.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class InformeResolucion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Incidente incidente;

	// que se hizo para resolver el incidente
	private String descripcionTrabajo;

	// la genera el sistema, coincide con la fecha/hora de la transicion en el historial
	private LocalDateTime fechaHoraResolucion;

	// cero cuando no corresponde
	private BigDecimal costo;

	private String observaciones;

	// texto simple, no hace falta una entidad Proveedor
	private String responsable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Incidente getIncidente() {
		return incidente;
	}

	public void setIncidente(Incidente incidente) {
		this.incidente = incidente;
	}

	public String getDescripcionTrabajo() {
		return descripcionTrabajo;
	}

	public void setDescripcionTrabajo(String descripcionTrabajo) {
		this.descripcionTrabajo = descripcionTrabajo;
	}

	public LocalDateTime getFechaHoraResolucion() {
		return fechaHoraResolucion;
	}

	public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) {
		this.fechaHoraResolucion = fechaHoraResolucion;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
}
