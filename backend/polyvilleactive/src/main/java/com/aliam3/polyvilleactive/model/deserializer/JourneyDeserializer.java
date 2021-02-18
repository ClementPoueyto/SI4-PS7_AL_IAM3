package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JourneyDeserializer extends StdDeserializer<Journey> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5878973964305830729L;


	public JourneyDeserializer(Class<?> vc) {
        super(vc);
    }

    public JourneyDeserializer() {
        this(null);
    }


    @Override
    public Journey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Journey journey = new Journey();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = null;
        try {
            node = codec.readTree(jsonParser);
        } catch (IOException e) {
            e.printStackTrace();
        }



        JsonNode nbTransfers = node.get("nb_transfers");
        journey.setNbTransfers(nbTransfers.asInt());

        JsonNode emission = node.get("co2_emission").get("value");
        journey.setCo2emission(emission.asDouble());

        JsonNode arrivalTime = node.get("arrival_date_time");
        journey.setDateArrival( LocalDateTime.parse(arrivalTime.asText(),DateTimeFormatter.ofPattern("yyyMMdd'T'HHmmss")));

        JsonNode departureTime = node.get("departure_date_time");
        journey.setDateDeparture( LocalDateTime.parse(departureTime.asText(),DateTimeFormatter.ofPattern("yyyMMdd'T'HHmmss")));

        journey.setDuration(Duration.between(journey.getDateDeparture(), journey.getDateArrival()).getSeconds());

        if(node.get("fare").hasNonNull("total")) {
            JsonNode fare = node.get("fare").get("total").get("value");
            journey.setFare(fare.asDouble());
        }
        else{
            journey.setFare(0);
        }


        List<Section> sections = objectMapper.readValue( node.get("sections").toPrettyString(), new TypeReference<List<Section>>(){});

        journey.setSections(sections);

        Map<ModeTransport,Long> transports = new HashMap<>();
        for(Section sect : journey.getSections()){
            if(!transports.containsKey(sect.getTransport().getModeTransport())){
                transports.put(sect.getTransport().getModeTransport(), sect.getDuration());
            }
            else{
                transports.put(sect.getTransport().getModeTransport(), transports.get(sect.getTransport().getModeTransport())+sect.getDuration());
            }
        }

        journey.setTransports(transports);

        journey.setDeparture(journey.getSections().get(0).getFrom());
        journey.setArrival(journey.getSections().get(journey.getSections().size()-1).getTo());

        return journey;
    }
}
