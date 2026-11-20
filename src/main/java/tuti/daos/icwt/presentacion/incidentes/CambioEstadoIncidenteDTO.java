package tuti.daos.icwt.presentacion.incidentes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;

public record CambioEstadoIncidenteDTO(
		@JsonDeserialize(using = IncidenteJson.Texto.class) @NotBlank String estado) {
}
