package tuti.daos.icwt.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String propietario;

	private String inquilino;

	private String direccionPropiedad;

	private LocalDate fechaInicio;

	/*
	 * El contrato debería guardar fecha de inicio y duración en meses,
	 * la fecha de fin se calcula sumando la duración y restando un dia.
	 */
	private Integer duracionMeses;

	private BigDecimal importeMensual;

	// dia del mes en que vence la factura (ej: 10 quiere decir que vence el 10 de cada mes)
	private Integer diaVencimiento;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private EstadoContrato estado;

	// Borrado a nivel lógico, no se elimina fisicamente
	private boolean eliminado;
	
	//Getters y setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public String getInquilino() {
		return inquilino;
	}

	public void setInquilino(String inquilino) {
		this.inquilino = inquilino;
	}

	public String getDireccionPropiedad() {
		return direccionPropiedad;
	}

	public void setDireccionPropiedad(String direccionPropiedad) {
		this.direccionPropiedad = direccionPropiedad;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimiento() {
		return diaVencimiento;
	}

	public void setDiaVencimiento(Integer diaVencimiento) {
		this.diaVencimiento = diaVencimiento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	//Aclaración del mètodo fecha de fin = fecha de inicio + duracion en meses - 1 dia
	public LocalDate fechaFin() {
		return fechaInicio.plusMonths(duracionMeses).minusDays(1);
	}
}
