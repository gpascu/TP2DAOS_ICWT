package tuti.daos.icwt.presentacion.facturas;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tuti.daos.icwt.entidades.EstadoFactura;
import tuti.daos.icwt.entidades.Factura;
import tuti.daos.icwt.error.ErrorAtributo;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.contratos.ContratoRestController;
import tuti.daos.icwt.servicios.FacturaService;

// Facturas (S01): emision, consulta y anulacion de facturas de alquiler

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Emision, consulta y anulacion de facturas de alquiler")
public class FacturaRestController {

	@Autowired
	private FacturaService service;

	/**
	 * Lista y filtra facturas.
	 * Ejemplos:
	 *  GET 'http://localhost:8083/facturas'
	 *  GET 'http://localhost:8083/facturas?contratoId=1&periodo=2026-11'
	 *  GET 'http://localhost:8083/facturas?estado=PENDIENTE'
	 */
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Lista y filtra facturas (por contrato, periodo o estado)")
	public ResponseEntity<List<FacturaResponseDTO>> listar(@RequestParam(required = false) Long contratoId,
			@RequestParam(required = false) String periodo, @RequestParam(required = false) EstadoFactura estado)
			throws Excepcion {
		List<FacturaResponseDTO> dtos = new ArrayList<FacturaResponseDTO>();
		for (Factura f : service.listar(contratoId, periodo, estado)) {
			dtos.add(buildResponseBody(f));
		}
		return new ResponseEntity<List<FacturaResponseDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Consulta una factura por id.
	 *  Ejemplo: GET 'http://localhost:8083/facturas/1'
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Consulta una factura por su id")
	public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Optional<Factura> rta = service.getById(id);
		if (rta.isPresent())
			return new ResponseEntity<FacturaResponseDTO>(buildResponseBody(rta.get()), HttpStatus.OK);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	/**
	 * Emite una factura. Con solo contrato y periodo se emite la facturacion mensual.
	 * Ejemplo POST 'http://localhost:8080/facturas' \
		 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Emite una factura (facturacion mensual o adicional)")
	public ResponseEntity<Object> emitir(@Valid @RequestBody FacturaDTO dto, BindingResult result) throws Excepcion {
		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

		Long idNueva = service.emitir(dto);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNueva).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * Anula una factura pendiente o vencida.
	 * Ejemplo PATCH 'http://localhost:8080/facturas/1/estado' \
	 */
	@PatchMapping("/{id}/estado")
	@Operation(summary = "Anula una factura pendiente o vencida")
	public ResponseEntity<Object> anular(@PathVariable Long id, @Valid @RequestBody CambioEstadoFacturaDTO dto,
			BindingResult result) throws Excepcion {
		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

		// por ahora la unica transicion que permite esta operacion es anular
		if (dto.getEstado() != EstadoFactura.ANULADA)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorAtributo("estado", "Esta operacion solo permite anular la factura"));

		service.anular(id);
		return ResponseEntity.ok().build();
	}

	// METODOS Auxiliares

	private FacturaResponseDTO buildResponseBody(Factura pojo) throws Excepcion {
		try {
			FacturaResponseDTO dto = new FacturaResponseDTO(pojo);

			// self link
			Link selfLink = WebMvcLinkBuilder.linkTo(FacturaRestController.class).slash(pojo.getId()).withSelfRel();
			dto.add(selfLink);

			// link al contrato asociado
			Link contratoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ContratoRestController.class)
					.getById(pojo.getContrato().getId())).withRel("contrato");
			dto.add(contratoLink);

			// link al pago asociado, unicamente cuando existe IMPORTANTE: RECORDAR DE MODIFICARLO A MEDIDA QUE AVANCE EL PROYECTO PORQUE ESPERAMOS EL PAGO CONTROLLER
			if (pojo.getPago() != null) {
			    String href = ServletUriComponentsBuilder.fromCurrentContextPath()
			            .path("/pagos/{id}")
			            .buildAndExpand(pojo.getPago().getId())
			            .toUriString();
			    dto.add(Link.of(href).withRel("pago"));
			}

			return dto;
		} catch (Exception e) {
			throw new Excepcion("Factura", "Error al construir los enlaces HATEOAS: " + e.getMessage(), 500);
		}
	}

	private List<ErrorAtributo> formatearErrores(BindingResult result) {
		List<FieldError> errors = result.getFieldErrors();
		List<ErrorAtributo> respuesta = new ArrayList<ErrorAtributo>();
		for (FieldError error : errors) {
			respuesta.add(new ErrorAtributo(error.getField(), error.getDefaultMessage()));
		}
		return respuesta;
	}
}
