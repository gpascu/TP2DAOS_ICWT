package tuti.daos.icwt.presentacion.pagos;

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
import tuti.daos.icwt.entidades.Pago;
import tuti.daos.icwt.error.ErrorAtributo;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.facturas.FacturaRestController;
import tuti.daos.icwt.servicios.PagoService;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "Registro y consulta de pagos de facturas")
public class PagoRestController {

	@Autowired
	private PagoService service;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Lista pagos o consulta el pago de una factura")
	public ResponseEntity<List<PagoResponseDTO>> listar(@RequestParam(required = false) Long facturaId)
			throws Excepcion {
		List<PagoResponseDTO> dtos = new ArrayList<PagoResponseDTO>();
		for (Pago p : service.listar(facturaId)) {
			dtos.add(buildResponseBody(p));
		}
		return new ResponseEntity<List<PagoResponseDTO>>(dtos, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Consulta un pago por su id")
	public ResponseEntity<PagoResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Optional<Pago> rta = service.getById(id);
		if (rta.isPresent())
			return new ResponseEntity<PagoResponseDTO>(buildResponseBody(rta.get()), HttpStatus.OK);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Registra el pago total de una factura pendiente o vencida")
	public ResponseEntity<Object> registrar(@Valid @RequestBody PagoDTO dto, BindingResult result) throws Excepcion {
		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

		Long idNuevo = service.registrar(dto);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNuevo).toUri();
		return ResponseEntity.created(location).build();
	}

	private PagoResponseDTO buildResponseBody(Pago pojo) throws Excepcion {
		try {
			PagoResponseDTO dto = new PagoResponseDTO(pojo);

			Link selfLink = WebMvcLinkBuilder.linkTo(PagoRestController.class).slash(pojo.getId()).withSelfRel();
			dto.add(selfLink);

			Link facturaLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FacturaRestController.class)
					.getById(pojo.getFactura().getId())).withRel("factura");
			dto.add(facturaLink);

			return dto;
		} catch (Exception e) {
			throw new Excepcion("Pago", "Error al construir los enlaces HATEOAS: " + e.getMessage(), 500);
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
