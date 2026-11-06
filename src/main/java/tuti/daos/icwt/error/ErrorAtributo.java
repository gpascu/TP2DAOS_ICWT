package tuti.daos.icwt.error;

public class ErrorAtributo {

	private String atributo;
	private String mensaje;

	public ErrorAtributo() {
	}

	public ErrorAtributo(String atributo, String mensaje) {
		this.atributo = atributo;
		this.mensaje = mensaje;
	}

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
