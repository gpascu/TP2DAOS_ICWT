package tuti.daos.icwt.presentacion.contratos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tuti.daos.icwt.entidades.Contrato;

//Contiene los datos necesarios para crear un contrato
public class ContratoDTO {

    // El propietario es obligatorio y no puede tener solo espacios.
    @NotBlank(message = "El propietario es obligatorio")
    private String propietario;

    // El inquilino es obligatorio y no puede tener solo espacios.
    @NotBlank(message = "El inquilino es obligatorio")
    private String inquilino;

    // La dirección es obligatoria y no puede tener solo espacios.
    @NotBlank(message = "La direccion de la propiedad es obligatoria")
    private String direccionPropiedad;

    // La fecha de inicio es obligatoria.
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    // La duración es obligatoria y debe ser de al menos un mes.
    @NotNull(message = "La duracion en meses es obligatoria")
    @Min(value = 1, message = "La duracion debe ser de al menos 1 mes")
    private Integer duracionMeses;

    // El importe mensual es obligatorio y debe ser mayor que cero.
    @NotNull(message = "El importe mensual es obligatorio")
    @Positive(message = "El importe mensual debe ser positivo")
    private BigDecimal importeMensual;

    // El día de vencimiento es obligatorio y debe estar entre 1 y 31.
    @NotNull(message = "El dia de vencimiento es obligatorio")
    @Min(value = 1, message = "El dia de vencimiento debe estar entre 1 y 31")
    @Max(value = 31, message = "El dia de vencimiento debe estar entre 1 y 31")
    private Integer diaVencimiento;

    private String descripcion;

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

    // Crea un objeto Contrato y le copia los datos de este DTO.
    public Contrato toPojo() {
        Contrato c = new Contrato();
        c.setPropietario(this.getPropietario());
        c.setInquilino(this.getInquilino());
        c.setDireccionPropiedad(this.getDireccionPropiedad());
        c.setFechaInicio(this.getFechaInicio());
        c.setDuracionMeses(this.getDuracionMeses());
        c.setImporteMensual(this.getImporteMensual());
        c.setDiaVencimiento(this.getDiaVencimiento());
        c.setDescripcion(this.getDescripcion());
        return c;
    }
}