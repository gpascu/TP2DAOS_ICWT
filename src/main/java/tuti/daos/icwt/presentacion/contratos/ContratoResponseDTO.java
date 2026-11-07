package tuti.daos.icwt.presentacion.contratos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.entidades.EstadoContrato;

//Devuelve los datos de un contrato para consultarlo
public class ContratoResponseDTO extends RepresentationModel<ContratoResponseDTO> {

    private Long id;
    private String propietario;
    private String inquilino;
    private String direccionPropiedad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer duracionMeses;
    private BigDecimal importeMensual;
    private Integer diaVencimiento;
    private String descripcion;
    private EstadoContrato estado;

    // Copia los datos del contrato al objeto que se devuelve en la respuesta.
    public ContratoResponseDTO(Contrato pojo) {
        this.id = pojo.getId();
        this.propietario = pojo.getPropietario();
        this.inquilino = pojo.getInquilino();
        this.direccionPropiedad = pojo.getDireccionPropiedad();
        this.fechaInicio = pojo.getFechaInicio();

        // Obtiene la fecha de fin mediante el método del contrato.
        this.fechaFin = pojo.fechaFin();

        this.duracionMeses = pojo.getDuracionMeses();
        this.importeMensual = pojo.getImporteMensual();
        this.diaVencimiento = pojo.getDiaVencimiento();
        this.descripcion = pojo.getDescripcion();
        this.estado = pojo.getEstado();
    }

    public Long getId() {
        return id;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getInquilino() {
        return inquilino;
    }

    public String getDireccionPropiedad() {
        return direccionPropiedad;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public Integer getDiaVencimiento() {
        return diaVencimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoContrato getEstado() {
        return estado;
    }
}