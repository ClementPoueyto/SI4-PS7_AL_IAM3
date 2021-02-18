package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.model.incidents.weather.Rain;
import com.aliam3.polyvilleactive.model.incidents.weather.Snow;
import com.aliam3.polyvilleactive.model.incidents.weather.Sunny;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class IncidentWeatherDeserializer extends JsonDeserializer<IncidentWeather> {
    @Override
    public IncidentWeather deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = null;
        try {
            node = codec.readTree(jsonParser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode type = node.get("type");
        JsonNode line= node.get("line");

        JsonNode modetransport= node.get("mode_transport");
        ModeTransport modeTransport=null;
        if(modetransport.asText().equals("BUS")){
            modeTransport=ModeTransport.BUS;
        }
        if(modetransport.asText().equals("METRO")){
            modeTransport=ModeTransport.METRO;
        }
        if(modetransport.asText().equals("TRAIN")){
            modeTransport=ModeTransport.TRAIN;
        }
        JsonNode num=node.get("num");
        if(type.asText().equals("Rain")){

            return new Rain(line.asText(),modeTransport,num.asText());
        }
        if(type.asText().equals("Snow")){
            return new Snow(line.asText(),modeTransport,num.asText());
        }
        if(type.asText().equals("Sunny")){
            return new Sunny(line.asText(),modeTransport,num.asText());
        }
        return null;
    }
}
