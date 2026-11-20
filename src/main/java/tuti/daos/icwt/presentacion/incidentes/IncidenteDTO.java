package tuti.daos.icwt.presentacion.incidentes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record IncidenteDTO(
		@JsonDeserialize(using = IncidenteJson.Entero.class) @NotNull @Positive Long contratoId,
		@JsonDeserialize(using = IncidenteJson.Texto.class) @NotBlank @Size(max = 255) String titulo,
		@JsonDeserialize(using = IncidenteJson.Texto.class) @NotBlank @Size(max = 255) String descripcion,
		@JsonDeserialize(using = IncidenteJson.Texto.class) @NotBlank String categoria,
		@JsonDeserialize(using = IncidenteJson.Texto.class) @NotBlank String prioridad) {
}
