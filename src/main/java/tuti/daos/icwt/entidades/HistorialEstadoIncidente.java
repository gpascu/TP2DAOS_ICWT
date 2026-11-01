package tuti.daos.icwt.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/*
 * Cada entrada del historial guarda estado anterior, estado nuevo y fecha/hora.
 * En el alta del incidente el estado anterior queda vacio.
 */
@Entity
public class HistorialEstadoIncidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Incidente incidente;

	@Enumerated(EnumType.STRING)
	private EstadoIncidente estadoAnterior;

	@Enumerated(EnumType.STRING)
	private EstadoIncidente estadoNuevo;

	private LocalDateTime fechaHora;

	public HistorialEstadoIncidente() {
	}

	public HistorialEstadoIncidente(Incidente incidente, EstadoIncidente estadoAnterior, EstadoIncidente estadoNuevo,
			LocalDateTime fechaHora) {
		this.incidente = incidente;
		this.estadoAnterior = estadoAnterior;
		this.estadoNuevo = estadoNuevo;
		this.fechaHora = fechaHora;
	}

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

	public EstadoIncidente getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(EstadoIncidente estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public EstadoIncidente getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(EstadoIncidente estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
}
