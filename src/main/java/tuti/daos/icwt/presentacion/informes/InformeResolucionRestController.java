
package tuti.daos.icwt.presentacion.informes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import tuti.daos.icwt.entidades.InformeResolucion;
import tuti.daos.icwt.error.ErrorAtributo;
import tuti.daos.icwt.servicios.InformeResolucionService;

// S04: Registro y consulta de informes de resolución.

@RestController
@RequestMapping("/informes-resolucion")
@Tag(
    name = "Informes de resolución",
    description = "Registro y consulta de informes de resolución"
)
public class InformeResolucionRestController {

    @Autowired
    private InformeResolucionService service;

    // Lista todos los informes o filtra por incidente.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista informes de resolución")
    public ResponseEntity<List<InformeResolucionResponseDTO>> listar(
            @RequestParam(required = false) Long incidenteId) {

        List<InformeResolucionResponseDTO> respuesta =
                new ArrayList<>();

        for (InformeResolucion informe :
                service.listarInformes(incidenteId)) {

            respuesta.add(buildResponseBody(informe));
        }

        return ResponseEntity.ok(respuesta);
    }

    // Consulta un informe por su ID.
    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Consulta un informe por ID")
    public ResponseEntity<InformeResolucionResponseDTO> getById(
            @PathVariable Long id) {

        InformeResolucion informe = service.obtenerPorId(id);

        return ResponseEntity.ok(buildResponseBody(informe));
    }

    // Registra un informe y resuelve el incidente.
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Registra un informe de resolución")
    public ResponseEntity<Object> crear(
            @Valid @RequestBody InformeResolucionDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(formatearErrores(result));
        }

        InformeResolucion nuevo = service.crearInforme(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevo.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Construye la respuesta y agrega enlaces HATEOAS.
    private InformeResolucionResponseDTO buildResponseBody(
            InformeResolucion informe) {

        InformeResolucionResponseDTO dto =
                new InformeResolucionResponseDTO(informe);

        // Enlace al propio informe.
        Link selfLink = WebMvcLinkBuilder
                .linkTo(InformeResolucionRestController.class)
                .slash(informe.getId())
                .withSelfRel();

        dto.add(selfLink);

        // Enlace al incidente relacionado.
        String href = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/incidentes/{id}")
                .buildAndExpand(informe.getIncidente().getId())
                .toUriString();

        dto.add(Link.of(href).withRel("incidente"));

        return dto;
    }

    // Convierte los errores de validación en una respuesta.
    private List<ErrorAtributo> formatearErrores(
            BindingResult result) {

        List<ErrorAtributo> respuesta = new ArrayList<>();

        for (FieldError error : result.getFieldErrors()) {

            respuesta.add(new ErrorAtributo(
                    error.getField(),
                    error.getDefaultMessage()
            ));
        }

        return respuesta;
    }
}
