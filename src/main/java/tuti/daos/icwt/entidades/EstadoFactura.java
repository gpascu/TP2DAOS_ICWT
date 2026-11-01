package tuti.daos.icwt.entidades;

public enum EstadoFactura {
	/*
	 * En la base solo se guardan PENDIENTE, PAGADA y ANULADA.
	 * El estado VENCIDA no se persiste: se calcula al consultar,
	 * una factura pendiente pasa a considerarse vencida al dia siguiente de su vencimiento.
	 */
	PENDIENTE, VENCIDA, PAGADA, ANULADA
}
