package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.user.Form;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.aliam3.polyvilleactive.model.deserializer.SectionDeserializer.stripAccents;

public class FormDeserializer extends StdDeserializer<Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9207394735238415809L;

	public FormDeserializer(Class<?> vc) {
		super(vc);
	}

	public FormDeserializer() {
		this(null);
	}

	@Override
	public Form deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		Form form = new Form();
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = null;
		try {
			node = codec.readTree(jsonParser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonNode filtersJson = node.get("filters");
		JsonNode green = node.get("green");

		List<String> filters = objectMapper.readValue(filtersJson.toPrettyString(), new TypeReference<List<String>>() {
		});
		List<ModeTransport> transports = new ArrayList<>();
		filters.forEach(element -> transports.add(ModeTransport.valueOf(stripAccents(element.toUpperCase()))));
		form.setFilters(transports);
		form.setGreen(green.asBoolean());
		return form;
	}

}
