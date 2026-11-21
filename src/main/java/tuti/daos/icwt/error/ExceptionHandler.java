package tuti.daos.icwt.error;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;


import tuti.daos.icwt.excepciones.Excepcion;


/** Unifica las respuestas de error de la API con atributo y mensaje. */
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Excepcion.class)
    public ResponseEntity<ErrorAtributo> errorDeNegocio(Excepcion e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorAtributo(e.getAtributo(), e.getMensaje()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorAtributo>> datosInvalidos(MethodArgumentNotValidException e) {
        List<ErrorAtributo> errores = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorAtributo(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(errores.isEmpty()
                ? List.of(new ErrorAtributo("body", "La solicitud tiene datos invalidos.")) : errores);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorAtributo> jsonInvalido(HttpMessageNotReadableException e) {
        // Incluye JSON mal formado, fechas incorrectas, tipos y enums no reconocidos.
        // No exponemos mensajes internos de Jackson ni detalles de implementacion.
        return ResponseEntity.badRequest().body(new ErrorAtributo("body",
                "JSON mal formado o valor con formato invalido (fecha, numero o enumeracion)."));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorAtributo> parametroInvalido(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(new ErrorAtributo(e.getName(),
                "El valor del parametro de ruta o filtro tiene un formato invalido."));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorAtributo> parametroFaltante(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(new ErrorAtributo(e.getParameterName(),
                "El parametro es obligatorio."));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorAtributo> validacionDeParametros(HandlerMethodValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorAtributo("parametros",
                "Hay valores de ruta o filtros fuera del rango permitido."));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorAtributo> errorConEstado(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorAtributo("operacion",
                e.getReason() != null ? e.getReason() : "No se pudo completar la operacion."));
    }
}

