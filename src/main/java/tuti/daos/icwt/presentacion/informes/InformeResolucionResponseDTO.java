
package tuti.daos.icwt.presentacion.informes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import tuti.daos.icwt.entidades.InformeResolucion;

/**
 * Devuelve los datos de un informe de resolución
 * y permite agregar enlaces HATEOAS.
 */
public class InformeResolucionResponseDTO
        extends RepresentationModel<InformeResolucionResponseDTO> {

    private Long id;
    private Long incidenteId;
    private String descripcionTrabajo;
    private LocalDateTime fechaHoraResolucion;
    private BigDecimal costo;
    private String observaciones;
    private String responsable;

    // Convierte una entidad en una respuesta para la API.
    public InformeResolucionResponseDTO(InformeResolucion pojo) {

        this.id = pojo.getId();
        this.incidenteId = pojo.getIncidente().getId();
        this.descripcionTrabajo = pojo.getDescripcionTrabajo();
        this.fechaHoraResolucion = pojo.getFechaHoraResolucion();
        this.costo = pojo.getCosto();
        this.observaciones = pojo.getObservaciones();
        this.responsable = pojo.getResponsable();
    }

    public Long getId() {
        return id;
    }

    public Long getIncidenteId() {
        return incidenteId;
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public LocalDateTime getFechaHoraResolucion() {
        return fechaHoraResolucion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getResponsable() {
        return responsable;
    }
}
