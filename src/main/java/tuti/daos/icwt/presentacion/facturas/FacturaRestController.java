package tuti.daos.icwt.presentacion.facturas;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import tuti.daos.icwt.presentacion.pagos.PagoRestController;
import tuti.daos.icwt.servicios.FacturaService;

// Facturas (S01): emision, consulta y anulacion de facturas de alquiler
@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Emision, consulta y anulacion de facturas de alquiler")
public class FacturaRestController {

	@Autowired
	private FacturaService service;

	// Lista y filtra facturas. El listado se devuelve como coleccion HAL con su self link.
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Lista y filtra facturas (por contrato, periodo o estado)")
	public ResponseEntity<CollectionModel<FacturaResponseDTO>> listar(
			@RequestParam(required = false) Long contratoId,
			@RequestParam(required = false) String periodo,
			@RequestParam(required = false) EstadoFactura estado) throws Excepcion {

		List<FacturaResponseDTO> dtos = new ArrayList<>();
		for (Factura f : service.listar(contratoId, periodo, estado)) {
			dtos.add(buildResponseBody(f));
		}

		// Self del listado, conservando los filtros aplicados en la URL.
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()).withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(dtos, selfLink));
	}

	// Consulta una factura por id. Ejemplo: GET 'http://localhost:8083/facturas/1'
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Consulta una factura por su id")
	public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Optional<Factura> rta = service.getById(id);
		if (rta.isPresent())
			return new ResponseEntity<>(buildResponseBody(rta.get()), HttpStatus.OK);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// Emite una factura. Con solo contrato y periodo se emite la facturacion mensual.
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Emite una factura (facturacion mensual o adicional)")
	public ResponseEntity<?> emitir(@Valid @RequestBody FacturaDTO dto, BindingResult result) throws Excepcion {
		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

		Long idNueva = service.emitir(dto);
		Factura factura = service.getById(idNueva)
				.orElseThrow(() -> new Excepcion("id", "No se pudo recuperar la factura emitida.", 500));

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNueva).toUri();

		// 201 con Location y con la factura creada en el body: el cliente ve de una los
		// datos calculados por el sistema (emision, vencimiento, importe) y los links.
		return ResponseEntity.created(location).body(buildResponseBody(factura));
	}

	// Anula una factura pendiente o vencida.
	@PatchMapping(value = "/{id}/estado", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Anula una factura pendiente o vencida")
	public ResponseEntity<?> anular(@PathVariable Long id, @Valid @RequestBody CambioEstadoFacturaDTO dto,
			BindingResult result) throws Excepcion {
		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

		// Por ahora la unica transicion que permite esta operacion es anular.
		if (dto.getEstado() != EstadoFactura.ANULADA)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorAtributo("estado", "Esta operacion solo permite anular la factura"));

		service.anular(id);
		Factura factura = service.getById(id)
				.orElseThrow(() -> new Excepcion("id", "No se encuentra la factura.", 404));

		// 200 con la factura ya anulada y sus links.
		return ResponseEntity.ok(buildResponseBody(factura));
	}

	// METODOS AUXILIARES

	private FacturaResponseDTO buildResponseBody(Factura pojo) throws Excepcion {
		try {
			FacturaResponseDTO dto = new FacturaResponseDTO(pojo);

			// Self link.
			Link selfLink = WebMvcLinkBuilder.linkTo(FacturaRestController.class).slash(pojo.getId()).withSelfRel();
			dto.add(selfLink);

			// Link al contrato asociado.
			Link contratoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ContratoRestController.class)
					.getById(pojo.getContrato().getId())).withRel("contrato");
			dto.add(contratoLink);

			// Link al pago asociado, unicamente cuando existe.
			if (pojo.getPago() != null) {
				Link pagoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagoRestController.class)
						.getById(pojo.getPago().getId())).withRel("pago");
				dto.add(pagoLink);
			}

			return dto;
		} catch (Exception e) {
			throw new Excepcion("Factura", "Error al construir los enlaces HATEOAS: " + e.getMessage(), 500);
		}
	}

	private List<ErrorAtributo> formatearErrores(BindingResult result) {
		List<FieldError> errors = result.getFieldErrors();
		List<ErrorAtributo> respuesta = new ArrayList<>();
		for (FieldError error : errors) {
			respuesta.add(new ErrorAtributo(error.getField(), error.getDefaultMessage()));
		}
		return respuesta;
	}
}