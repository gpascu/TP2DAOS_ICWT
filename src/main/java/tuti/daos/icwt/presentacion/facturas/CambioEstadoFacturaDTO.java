package tuti.daos.icwt.presentacion.facturas;

import jakarta.validation.constraints.NotNull;
import tuti.daos.icwt.entidades.EstadoFactura;

// Pedido del cambio de estado de una factura. En esta etapa, la unica operacion permitida es anularla.

public class CambioEstadoFacturaDTO {

	@NotNull(message = "El nuevo estado es obligatorio")
	private EstadoFactura estado;

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}
}
