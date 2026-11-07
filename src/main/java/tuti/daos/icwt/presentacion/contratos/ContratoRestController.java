package tuti.daos.icwt.presentacion.contratos;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tuti.daos.icwt.entidades.Contrato;
import tuti.daos.icwt.error.ErrorAtributo;
import tuti.daos.icwt.excepciones.Excepcion;
import tuti.daos.icwt.servicios.ContratoService;

//Recibe las solicitudes para consultar, crear y eliminar contratos usados en facturas e incidentes.
@RestController
@RequestMapping("/contratos")
@Tag(name = "Contratos", description = "Consulta y carga de contratos de alquiler")
public class ContratoRestController {

    // Asigna el servicio que maneja los contratos.
    @Autowired
    private ContratoService service;

    /** Lista todos los contratos. Ejemplo: GET http://localhost:8083/contratos */
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Lista todos los contratos registrados")
    public ResponseEntity<List<ContratoResponseDTO>> listar() throws Excepcion {
        List<ContratoResponseDTO> dtos = new ArrayList<ContratoResponseDTO>();

        // Convierte cada contrato en una respuesta con sus datos y enlaces.
        for (Contrato c : service.getAll()) {
            dtos.add(buildResponseBody(c));
        }

        // Devuelve la lista con el código 200.
        return new ResponseEntity<List<ContratoResponseDTO>>(dtos, HttpStatus.OK);
    }

    // Busca un contrato por ID. Ejemplo: get http://localhost:8083/contratos/1 
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Consulta un contrato por su id")
    public ResponseEntity<ContratoResponseDTO> getById(@PathVariable Long id) throws Excepcion {
        Optional<Contrato> rta = service.getById(id);

        // Si existe, devuelve el contrato con código 200; si no, devuelve 404.
        if (rta.isPresent())
            return new ResponseEntity<ContratoResponseDTO>(buildResponseBody(rta.get()), HttpStatus.OK);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //Crea un contrato con los datos JSON recibidos. Ejemplo: post http://localhost:8083/contratos 
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Da de alta un contrato de alquiler")
    public ResponseEntity<Object> insertar(@Valid @RequestBody ContratoDTO dto, BindingResult result)
            throws Excepcion {

        // Si los datos no son válidos, devuelve los errores con código 400.
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatearErrores(result));

        Long idNuevo = service.insert(dto);

        // Crea la dirección del contrato nuevo usando el ID generado.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNuevo).toUri();

        // Devuelve el código 201 y la dirección del contrato creado.
        return ResponseEntity.created(location).build();
    }

    //Marca el contrato como eliminado sin borrarlo de la base. Ejemplo: DELETE http://localhost:8083/contratos/1 
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina logicamente un contrato")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) throws Excepcion {
        service.eliminar(id);

        // Devuelve el código 204, sin contenido en la respuesta.
        return ResponseEntity.noContent().build();
    }

    // Métodos de apoyo para preparar las respuestas y los errores.

    private ContratoResponseDTO buildResponseBody(Contrato pojo) throws Excepcion {
        try {
            ContratoResponseDTO dto = new ContratoResponseDTO(pojo);

            // Crea y agrega un enlace para consultar este mismo contrato.
            Link selfLink = WebMvcLinkBuilder.linkTo(ContratoRestController.class).slash(pojo.getId()).withSelfRel();
            dto.add(selfLink);

            return dto;
        } catch (Exception e) {
            // Si falla la creación de la respuesta, lanza un error con código 500.
            throw new Excepcion("Contrato", "Error al construir los enlaces HATEOAS: " + e.getMessage(), 500);
        }
    }

    // Convierte los errores de validación en una lista con el nombre del campo y su mensaje.
    private List<ErrorAtributo> formatearErrores(BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        List<ErrorAtributo> respuesta = new ArrayList<ErrorAtributo>();

        for (FieldError error : errors) {
            respuesta.add(new ErrorAtributo(error.getField(), error.getDefaultMessage()));
        }

        return respuesta;
    }
}