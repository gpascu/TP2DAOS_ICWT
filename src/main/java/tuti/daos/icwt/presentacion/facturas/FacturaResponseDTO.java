package tuti.daos.icwt.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;

/** Devuelve los datos de una factura y permite agregar enlaces a la factura, al contrato y al pago si existe. */
public class FacturaResponseDTO extends RepresentationModel<FacturaResponseDTO> {

    private Long id;
    private Long idContrato;
    private String periodo;
    private String concepto;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal importe;
    private EstadoFactura estado;
    private String tipo;

    // Copia los datos de la factura al objeto que se devuelve en la respuesta.
    public FacturaResponseDTO(Factura pojo) {
        this.id = pojo.getId();
        this.idContrato = pojo.getContrato().getId();
        this.periodo = pojo.getPeriodo();
        this.concepto = pojo.getConcepto();
        this.fechaEmision = pojo.getFechaEmision();
        this.fechaVencimiento = pojo.getFechaVencimiento();
        this.importe = pojo.getImporte();

        // Obtiene el estado actual y comprueba si la factura está vencida al consultarla.
        this.estado = pojo.estadoActual();

        // Si la factura es mensual, usa "MENSUAL"; si no, usa "ADICIONAL".
        this.tipo = pojo.isMensual() ? "MENSUAL" : "ADICIONAL";
    }

    public Long getId() {
        return id;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getConcepto() {
        return concepto;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public String getTipo() {
        return tipo;
    }
}