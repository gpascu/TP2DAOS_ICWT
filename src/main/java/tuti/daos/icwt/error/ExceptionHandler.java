package tuti.daos.icwt.error;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import tuti.daos.icwt.excepciones.Excepcion;


@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(Excepcion.class)
	public ResponseEntity<ErrorAtributo> errorDeNegocio(HttpServletRequest request, Excepcion e) {
		return ResponseEntity.status(e.getStatusCode()).body(new ErrorAtributo(e.getAtributo(), e.getMensaje()));
	}

	// Generar el de json mal formado, fechas con formato incorrecto o valores de enum invalidos en el body
	

	
}
