package tuti.daos.icwt.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/** Contiene los datos para crear una factura. Sin importe es mensual; con importe es adicional. */
public class FacturaDTO {

    // El ID del contrato es obligatorio.
    @NotNull(message = "El contrato es obligatorio")
    private Long idContrato;

    // El período es obligatorio y debe tener cuatro dígitos para el año y un mes entre 01 y 12.
    @NotNull(message = "El periodo es obligatorio")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener formato AAAA-MM")
    private String periodo;

    private String concepto;

    // Si no se indica, se usa la fecha de hoy.
    private LocalDate fechaEmision;

    // Si no se indica, se calcula con el día de vencimiento del contrato.
    private LocalDate fechaVencimiento;

    // Si no se indica, se usa el importe mensual del contrato; si se indica, debe ser mayor que cero.
    @Positive(message = "El importe debe ser positivo")
    private BigDecimal importe;

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
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
}