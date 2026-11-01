package tuti.daos.icwt.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Incidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Contrato contrato;

	private String titulo;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private CategoriaIncidente categoria;

	// fecha y hora de alta, la genera el sistema
	private LocalDateTime fechaHoraAlta;

	@Enumerated(EnumType.STRING)
	private PrioridadIncidente prioridad;

	@Enumerated(EnumType.STRING)
	private EstadoIncidente estado;

	// historial de estados, de solo lectura, ordenado cronologicamente
	@OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL)
	@OrderBy("fechaHora ASC")
	private List<HistorialEstadoIncidente> historial = new ArrayList<HistorialEstadoIncidente>();

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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public CategoriaIncidente getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaIncidente categoria) {
		this.categoria = categoria;
	}

	public LocalDateTime getFechaHoraAlta() {
		return fechaHoraAlta;
	}

	public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) {
		this.fechaHoraAlta = fechaHoraAlta;
	}

	public PrioridadIncidente getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(PrioridadIncidente prioridad) {
		this.prioridad = prioridad;
	}

	public EstadoIncidente getEstado() {
		return estado;
	}

	public void setEstado(EstadoIncidente estado) {
		this.estado = estado;
	}

	public List<HistorialEstadoIncidente> getHistorial() {
		return historial;
	}

	public void setHistorial(List<HistorialEstadoIncidente> historial) {
		this.historial = historial;
	}
}
