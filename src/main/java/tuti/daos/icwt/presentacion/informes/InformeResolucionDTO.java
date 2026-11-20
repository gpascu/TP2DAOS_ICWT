
package tuti.daos.icwt.presentacion.informes;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InformeResolucionDTO {

    @NotNull
    private Long incidenteId;

    @NotBlank
    private String descripcionTrabajo;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal costo;

    private String observaciones;

    @NotBlank
    private String responsable;

    public Long getIncidenteId() {
        return incidenteId;
    }

    public void setIncidenteId(Long incidenteId) {
        this.incidenteId = incidenteId;
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public void setDescripcionTrabajo(String descripcionTrabajo) {
        this.descripcionTrabajo = descripcionTrabajo;
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
