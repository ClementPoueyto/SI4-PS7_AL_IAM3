package com.aliam3.polyvilleactive.model.deserializer;


import com.aliam3.polyvilleactive.model.location.Position;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PositionDeserializer extends StdDeserializer<Position> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4512027881116113658L;

	public PositionDeserializer() {
        this(null);
    }

    public PositionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Position deserialize(JsonParser parser, DeserializationContext deserializer) {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = null;
        try {
            node = codec.readTree(parser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try catch block
        JsonNode latitudeNode = node.get("latitude");
        double latitude = latitudeNode.asDouble();

        JsonNode longitudeNode = node.get("longitude");
        double longitude = longitudeNode.asDouble();
        return new Position(latitude,longitude);
    }
}