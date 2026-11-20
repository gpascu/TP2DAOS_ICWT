package tuti.daos.icwt.presentacion.incidentes;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

final class IncidenteJson {

	private IncidenteJson() {
	}

	// Así un ID como 6.7 no se convierte silenciosamente en 6.
	public static class Entero extends JsonDeserializer<Long> {
		@Override
		public Long deserialize(JsonParser parser, DeserializationContext contexto) throws IOException {
			if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
				return parser.getLongValue();
			}
			return (Long) contexto.handleUnexpectedToken(Long.class, parser);
		}
	}

	public static class Texto extends JsonDeserializer<String> {
		@Override
		public String deserialize(JsonParser parser, DeserializationContext contexto) throws IOException {
			if (parser.hasToken(JsonToken.VALUE_STRING)) {
				return parser.getText();
			}
			return (String) contexto.handleUnexpectedToken(String.class, parser);
		}
	}
}
