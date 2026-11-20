package tuti.daos.icwt.presentacion.incidentes;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import jakarta.validation.constraints.Positive;
import tuti.daos.icwt.entidades.CategoriaIncidente;
import tuti.daos.icwt.entidades.EstadoIncidente;
import tuti.daos.icwt.entidades.Incidente;
import tuti.daos.icwt.entidades.PrioridadIncidente;
import tuti.daos.icwt.error.ErrorAtributo;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.presentacion.contratos.ContratoRestController;
import tuti.daos.icwt.servicios.IncidenteService;

@RestController
@RequestMapping("/incidentes")
@Tag(name = "Incidentes", description = "Alta, consultas y seguimiento de incidentes")
public class IncidenteRestController {

	private final IncidenteService service;

	public IncidenteRestController(IncidenteService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Registra un incidente abierto")
	public ResponseEntity<EntityModel<IncidenteResponseDTO>> registrar(@Valid @RequestBody IncidenteDTO dto)
			throws Excepcion {
		CategoriaIncidente categoria = leerEnum(CategoriaIncidente.class, dto.categoria(), "categoria");
		PrioridadIncidente prioridad = leerEnum(PrioridadIncidente.class, dto.prioridad(), "prioridad");
		Incidente incidente = service.registrar(dto.contratoId(), dto.titulo(), dto.descripcion(), categoria, prioridad);
		URI location = linkTo(IncidenteRestController.class).slash(incidente.getId()).toUri();
		return ResponseEntity.created(location).body(respuesta(incidente));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consulta un incidente por ID")
	public EntityModel<IncidenteResponseDTO> getById(@PathVariable @Positive Long id) throws Excepcion {
		return respuesta(service.getById(id));
	}

	@GetMapping
	@Operation(summary = "Lista y filtra incidentes por contrato, estado, categoría o prioridad")
	public CollectionModel<EntityModel<IncidenteResponseDTO>> listar(
			@RequestParam(required = false) @Positive Long contratoId,
			@RequestParam(required = false) EstadoIncidente estado,
			@RequestParam(required = false) CategoriaIncidente categoria,
			@RequestParam(required = false) PrioridadIncidente prioridad) {
		List<EntityModel<IncidenteResponseDTO>> incidentes = new ArrayList<>();
		for (Incidente incidente : service.listar(contratoId, estado, categoria, prioridad)) {
			incidentes.add(respuesta(incidente));
		}
		return CollectionModel.of(incidentes, enlaceSolicitudActual())
				.withFallbackType(EntityModel.class, IncidenteResponseDTO.class);
	}

	@PatchMapping("/{id}/estado")
	@Operation(summary = "Actualiza el estado según las transiciones de seguimiento")
	public EntityModel<IncidenteResponseDTO> cambiarEstado(@PathVariable @Positive Long id,
			@Valid @RequestBody CambioEstadoIncidenteDTO dto) throws Excepcion {
		EstadoIncidente estado = leerEnum(EstadoIncidente.class, dto.estado(), "estado");
		return respuesta(service.cambiarEstado(id, estado));
	}

	@GetMapping("/{id}/historial")
	@Operation(summary = "Consulta el historial en orden cronológico")
	public CollectionModel<HistorialEstadoIncidenteDTO> historial(@PathVariable @Positive Long id) throws Excepcion {
		List<HistorialEstadoIncidenteDTO> entradas = service.consultarHistorial(id).stream()
				.map(HistorialEstadoIncidenteDTO::new)
				.toList();
		return CollectionModel.of(entradas, enlaceSolicitudActual()).withFallbackType(HistorialEstadoIncidenteDTO.class);
	}

	private EntityModel<IncidenteResponseDTO> respuesta(Incidente incidente) {
		var rutaIncidente = linkTo(IncidenteRestController.class).slash(incidente.getId());
		Link self = rutaIncidente.withSelfRel();
		Link contrato = linkTo(ContratoRestController.class).slash(incidente.getContrato().getId()).withRel("contrato");
		// S04 lista los informes del incidente con el filtro incidenteId.
		String informes = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/informes-resolucion")
				.queryParam("incidenteId", incidente.getId())
				.toUriString();
		return EntityModel.of(new IncidenteResponseDTO(incidente), self, contrato,
				Link.of(informes, "informes"), rutaIncidente.slash("historial").withRel("historial"));
	}

	private Link enlaceSolicitudActual() {
		return Link.of(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()).withSelfRel();
	}

	private <T extends Enum<T>> T leerEnum(Class<T> tipo, String valor, String atributo) throws Excepcion {
		// Aceptamos los nombres tal como aparecen en el enum.
		try {
			return Enum.valueOf(tipo, valor);
		} catch (IllegalArgumentException e) {
			throw new Excepcion(atributo, "Valores admitidos: " + Arrays.toString(tipo.getEnumConstants()), 400);
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorAtributo>> datosInvalidos(MethodArgumentNotValidException e) {
		List<ErrorAtributo> errores = e.getBindingResult().getFieldErrors().stream()
				.map(error -> new ErrorAtributo(error.getField(), error.getDefaultMessage()))
				.toList();
		return ResponseEntity.badRequest().body(errores);
	}
}
