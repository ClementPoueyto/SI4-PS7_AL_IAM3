package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.location.Place;
import com.aliam3.polyvilleactive.model.location.Position;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.aliam3.polyvilleactive.model.transport.Transport;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.text.Normalizer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SectionDeserializer extends StdDeserializer<Section> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4675289218866170320L;

	public SectionDeserializer(Class<?> vc) {
        super(vc);
    }

    public SectionDeserializer() {
        this(null);
    }

    @Override
    public Section deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Section section = new Section();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = null;
        try {
            node = codec.readTree(jsonParser);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonNode transport = node.get("type");
        String modeTransport="";
        String numero="";
        String line="";
        if(transport.asText().equals("public_transport")){
        	String display= "display_informations";
            if(node.get(display).hasNonNull("commercial_mode")) {
                modeTransport = node.get(display).get("commercial_mode").asText();
            }
            if(node.get(display).hasNonNull("label")) {
                numero = node.get(display).get("label").asText();
            }
            if(node.get(display).hasNonNull("name")) {
                line = node.get(display).get("name").asText();
            }
        }
        if(transport.asText().equals("transfer")){
            modeTransport = node.get("transfer_type").asText();
        }
        if(transport.asText().equals("waiting")){
            modeTransport = "waiting";
        }
        if(transport.asText().equals("street_network")|| transport.asText().equals("crow_fly")){
            modeTransport = node.get("mode").asText();
        }
        JsonNode emission = node.get("co2_emission").get("value");
        section.setCo2emission(emission.asDouble());

        ModeTransport modet;
        if(stripAccents(modeTransport.toUpperCase()).equals("RER")) {
            modet = ModeTransport.TRAIN;
        }
        else{
            modet = ModeTransport.valueOf(stripAccents(modeTransport.toUpperCase()));

        }

        JsonNode durationNode = node.get("duration");
        long dur = durationNode.asLong();
        section.setTransport(new Transport(modet,Duration.ofSeconds(dur),numero,line));

        JsonNode arrivalTime = node.get("arrival_date_time");
        section.setArrivalDateTime( LocalDateTime.parse(arrivalTime.asText(), DateTimeFormatter.ofPattern("yyyMMdd'T'HHmmss")));

        JsonNode departureTime = node.get("departure_date_time");
        section.setDepartureDateTime( LocalDateTime.parse(departureTime.asText(),DateTimeFormatter.ofPattern("yyyMMdd'T'HHmmss")));

        section.setDuration(Duration.between(section.getDepartureDateTime(), section.getArrivalDateTime()).getSeconds());

        if(modeTransport.equals("waiting")){
            return section;
        }
        else {
            JsonNode from = node.get("from");
            String adressPlaceFrom = from.get("name").asText();

            if(from.hasNonNull("address")){
                Position positionPlaceFrom = new Position(from.get("address").get("coord").get("lat").asDouble(), from.get("address").get("coord").get("lon").asDouble());
                section.setFrom(new Place(adressPlaceFrom, positionPlaceFrom));
            }
            else{
                Position positionPlaceFrom = new Position(from.get("stop_point").get("coord").get("lat").asDouble(), from.get("stop_point").get("coord").get("lon").asDouble());
                section.setFrom(new Place(adressPlaceFrom, positionPlaceFrom));
            }

            JsonNode to = node.get("to");
            String adressPlaceTo= to.get("name").asText();

            if(to.hasNonNull("address")){
                Position positionPlaceTo = new Position(to.get("address").get("coord").get("lat").asDouble(), to.get("address").get("coord").get("lon").asDouble());
                section.setTo(new Place(adressPlaceTo, positionPlaceTo));
            }
            else{
                Position positionPlaceTo = new Position(to.get("stop_point").get("coord").get("lat").asDouble(), to.get("stop_point").get("coord").get("lon").asDouble());
                section.setTo(new Place(adressPlaceTo, positionPlaceTo));
            }

            return section;
        }

    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

}
